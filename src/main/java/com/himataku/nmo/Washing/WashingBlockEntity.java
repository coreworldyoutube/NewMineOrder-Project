package com.himataku.nmo.Washing;

import com.himataku.nmo.ModBlockEntities;
import com.himataku.nmo.CrusherBlock.ModRecipes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.inventory.ContainerData;

import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;

public class WashingBlockEntity
        extends BlockEntity
        implements Container {

    /*
     * ============================================================
     * Item Slots
     * ============================================================
     */

    public static final int INPUT_SLOT = 0;

    public static final int OUTPUT_1_SLOT = 1;
    public static final int OUTPUT_2_SLOT = 2;
    public static final int OUTPUT_3_SLOT = 3;

    public static final int INVENTORY_SIZE = 4;

    /*
     * ============================================================
     * Fluid Tanks
     * ============================================================
     */

    public static final int INPUT_TANK = 0;
    public static final int DIRTY_FLUID_TANK = 1;

    public static final int TANK_COUNT = 2;

    public static final int FLUID_CAPACITY = 80_000;

    /*
     * ============================================================
     * Machine
     * ============================================================
     */

    public static final int ENERGY_CAPACITY = 10_000;

    public static final int ENERGY_RECEIVE = 10_000;

    /*
     * レシピamountとは別。
     *
     * タンクに最低1000mL入っていないと
     * 処理そのものを開始しない。
     * ============================================================
     */

    public static final int MINIMUM_FLUID_REQUIRED = 1_000;

    /*
     * ============================================================
     * Inventory
     * ============================================================
     */

    private final NonNullList<ItemStack> items =
            NonNullList.withSize(
                    INVENTORY_SIZE,
                    ItemStack.EMPTY
            );

    /*
     * ============================================================
     * Tanks
     * ============================================================
     */

    private final FluidTank inputTank =
            new FluidTank(
                    FLUID_CAPACITY
            ) {

                @Override
                protected void onContentsChanged() {
                    setChanged();
                }

                @Override
                public boolean isFluidValid(
                        FluidStack stack
                ) {
                    return true;
                }
            };

    private final FluidTank dirtyFluidTank =
            new FluidTank(
                    FLUID_CAPACITY
            ) {

                @Override
                protected void onContentsChanged() {
                    setChanged();
                }

                @Override
                public boolean isFluidValid(
                        FluidStack stack
                ) {
                    return true;
                }
            };

    /*
     * ============================================================
     * Energy
     * ============================================================
     */

    private final EnergyStorage energyStorage =
            new EnergyStorage(
                    ENERGY_CAPACITY,
                    ENERGY_RECEIVE,
                    ENERGY_CAPACITY
            );

    /*
     * ============================================================
     * Progress
     * ============================================================
     */

    private int progress = 0;

    /*
     * ============================================================
     * Item Handler
     * ============================================================
     */

    private final IItemHandler itemHandler =
            new IItemHandler() {

                @Override
                public int getSlots() {
                    return INVENTORY_SIZE;
                }

                @Override
                public ItemStack getStackInSlot(
                        int slot
                ) {

                    if (
                            slot < 0
                                    || slot >= INVENTORY_SIZE
                    ) {
                        return ItemStack.EMPTY;
                    }

                    return items.get(slot);
                }

                @Override
                public ItemStack insertItem(
                        int slot,
                        ItemStack stack,
                        boolean simulate
                ) {

                    /*
                     * Input以外には搬入不可
                     */

                    if (slot != INPUT_SLOT) {
                        return stack;
                    }

                    if (stack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    ItemStack current =
                            items.get(INPUT_SLOT);

                    if (
                            !current.isEmpty()
                                    && !ItemStack.isSameItemSameComponents(
                                    current,
                                    stack
                            )
                    ) {

                        return stack;
                    }

                    int maxStackSize =
                            Math.min(
                                    64,
                                    stack.getMaxStackSize()
                            );

                    int currentCount =
                            current.isEmpty()
                                    ? 0
                                    : current.getCount();

                    int space =
                            maxStackSize - currentCount;

                    if (space <= 0) {
                        return stack;
                    }

                    int insertAmount =
                            Math.min(
                                    stack.getCount(),
                                    space
                            );

                    if (!simulate) {

                        if (current.isEmpty()) {

                            ItemStack inserted =
                                    stack.copy();

                            inserted.setCount(
                                    insertAmount
                            );

                            items.set(
                                    INPUT_SLOT,
                                    inserted
                            );

                        } else {

                            current.grow(
                                    insertAmount
                            );
                        }

                        setChanged();
                    }

                    ItemStack remainder =
                            stack.copy();

                    remainder.shrink(
                            insertAmount
                    );

                    return remainder;
                }

                @Override
                public ItemStack extractItem(
                        int slot,
                        int amount,
                        boolean simulate
                ) {

                    /*
                     * Outputのみ搬出可能
                     */

                    if (
                            slot < OUTPUT_1_SLOT
                                    || slot > OUTPUT_3_SLOT
                    ) {
                        return ItemStack.EMPTY;
                    }

                    if (amount <= 0) {
                        return ItemStack.EMPTY;
                    }

                    ItemStack current =
                            items.get(slot);

                    if (current.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    int extractAmount =
                            Math.min(
                                    amount,
                                    current.getCount()
                            );

                    ItemStack result =
                            current.copy();

                    result.setCount(
                            extractAmount
                    );

                    if (!simulate) {

                        current.shrink(
                                extractAmount
                        );

                        setChanged();
                    }

                    return result;
                }

                @Override
                public int getSlotLimit(
                        int slot
                ) {
                    return 64;
                }

                @Override
                public boolean isItemValid(
                        int slot,
                        ItemStack stack
                ) {

                    return slot == INPUT_SLOT;
                }
            };

    /*
     * ============================================================
     * Fluid Handler
     * ============================================================
     *
     * fill  → Input Tank
     * drain → Dirty Fluid Tank
     *
     * 全方向から同じHandlerを公開する。
     * ============================================================
     */

    private final IFluidHandler fluidHandler =
            new IFluidHandler() {

                @Override
                public int getTanks() {
                    return TANK_COUNT;
                }

                @Override
                public FluidStack getFluidInTank(
                        int tank
                ) {

                    if (tank == INPUT_TANK) {

                        return inputTank
                                .getFluid()
                                .copy();
                    }

                    if (tank == DIRTY_FLUID_TANK) {

                        return dirtyFluidTank
                                .getFluid()
                                .copy();
                    }

                    return FluidStack.EMPTY;
                }

                @Override
                public int getTankCapacity(
                        int tank
                ) {

                    if (
                            tank == INPUT_TANK
                                    || tank == DIRTY_FLUID_TANK
                    ) {

                        return FLUID_CAPACITY;
                    }

                    return 0;
                }

                @Override
                public boolean isFluidValid(
                        int tank,
                        FluidStack stack
                ) {

                    return tank == INPUT_TANK;
                }

                @Override
                public int fill(
                        FluidStack resource,
                        FluidAction action
                ) {

                    if (resource.isEmpty()) {
                        return 0;
                    }

                    return inputTank.fill(
                            resource,
                            action
                    );
                }

                @Override
                public FluidStack drain(
                        FluidStack resource,
                        FluidAction action
                ) {

                    if (resource.isEmpty()) {
                        return FluidStack.EMPTY;
                    }

                    return dirtyFluidTank.drain(
                            resource,
                            action
                    );
                }

                @Override
                public FluidStack drain(
                        int maxDrain,
                        FluidAction action
                ) {

                    if (maxDrain <= 0) {
                        return FluidStack.EMPTY;
                    }

                    return dirtyFluidTank.drain(
                            maxDrain,
                            action
                    );
                }
            };

    /*
     * ============================================================
     * ContainerData
     * ============================================================
     */

    private final ContainerData containerData =
            new ContainerData() {

                @Override
                public int get(int index) {

                    return switch (index) {

                        case 0 ->
                                progress;

                        case 1 -> {
                            WashingRecipe recipe =
                                    getCurrentRecipe();

                            yield recipe == null
                                    ? 1
                                    : recipe.getProcessTime();
                        }

                        case 2 ->
                                energyStorage
                                        .getEnergyStored();

                        case 3 ->
                                energyStorage
                                        .getMaxEnergyStored();

                        case 4 ->
                                inputTank
                                        .getFluidAmount();

                        case 5 ->
                                inputTank
                                        .getCapacity();

                        case 6 ->
                                dirtyFluidTank
                                        .getFluidAmount();

                        case 7 ->
                                dirtyFluidTank
                                        .getCapacity();

                        default ->
                                0;
                    };
                }

                @Override
                public void set(
                        int index,
                        int value
                ) {

                    if (index == 0) {
                        progress = value;
                    }
                }

                @Override
                public int getCount() {
                    return 8;
                }
            };

    /*
     * ============================================================
     * Constructor
     * ============================================================
     */

    public WashingBlockEntity(
            BlockPos pos,
            BlockState state
    ) {

        super(
                ModBlockEntities.WASHING.get(),
                pos,
                state
        );
    }

    /*
     * ============================================================
     * Tick
     * ============================================================
     */

    public static void tick(
            Level level,
            BlockPos pos,
            BlockState state,
            WashingBlockEntity washing
    ) {

        if (level.isClientSide()) {
            return;
        }

        WashingRecipe recipe =
                washing.getCurrentRecipe();

        if (recipe == null) {

            washing.progress = 0;

            return;
        }

        /*
         * --------------------------------------------------------
         * Item
         * --------------------------------------------------------
         */

        if (
                washing.items
                        .get(INPUT_SLOT)
                        .isEmpty()
        ) {

            washing.progress = 0;

            return;
        }

        /*
         * --------------------------------------------------------
         * Fluid
         * --------------------------------------------------------
         *
         * レシピamountではなく、
         * 機械として最低1000mL必要。
         * --------------------------------------------------------
         */

        if (
                washing.inputTank
                        .getFluidAmount()
                        < MINIMUM_FLUID_REQUIRED
        ) {

            washing.progress = 0;

            return;
        }

        /*
         * --------------------------------------------------------
         * Energy
         * --------------------------------------------------------
         */

        if (
                washing.energyStorage
                        .getEnergyStored()
                        < recipe.getEnergy()
        ) {

            return;
        }

        /*
         * --------------------------------------------------------
         * Output
         * --------------------------------------------------------
         */

        if (!washing.canOutput(recipe)) {

            washing.progress = 0;

            return;
        }

        /*
         * --------------------------------------------------------
         * Progress
         * --------------------------------------------------------
         */

        washing.progress++;

        if (
                washing.progress
                        >= recipe.getProcessTime()
        ) {

            if (
                    washing.processRecipe(recipe)
            ) {

                washing.progress = 0;

            } else {

                washing.progress = 0;
            }

            washing.setChanged();
        }
    }

    /*
     * ============================================================
     * Recipe
     * ============================================================
     */

    private WashingRecipe getCurrentRecipe() {

        if (level == null) {
            return null;
        }

        ItemStack item =
                items.get(INPUT_SLOT);

        FluidStack fluid =
                inputTank.getFluid();

        if (item.isEmpty() || fluid.isEmpty()) {
            return null;
        }

        WashingRecipe.WashingRecipeInput input =
                new WashingRecipe.WashingRecipeInput(
                        item,
                        fluid
                );

        return level.getRecipeManager()
                .getAllRecipesFor(
                        ModRecipes.WASHING_TYPE.get()
                )
                .stream()
                .map(holder -> holder.value())
                .filter(recipe ->
                        recipe.matches(
                                input,
                                level
                        )
                )
                .findFirst()
                .orElse(null);
    }

    /*
     * ============================================================
     * Output Check
     * ============================================================
     */

    private boolean canOutput(
            WashingRecipe recipe
    ) {

        ItemStack output =
                recipe.getOutputItem();

        if (!canInsertItem(output)) {
            return false;
        }

        FluidStack dirty =
                recipe.getDirtyFluid();

        return canInsertDirtyFluid(
                dirty
        );
    }

    /*
     * ============================================================
     * Item Output Check
     * ============================================================
     */

    private boolean canInsertItem(
            ItemStack output
    ) {

        if (output.isEmpty()) {
            return true;
        }

        NonNullList<ItemStack> simulated =
                NonNullList.withSize(
                        3,
                        ItemStack.EMPTY
                );

        for (int i = 0; i < 3; i++) {

            simulated.set(
                    i,
                    items.get(
                            OUTPUT_1_SLOT + i
                    ).copy()
            );
        }

        /*
         * 既存スタックに入るか
         */

        for (int i = 0; i < 3; i++) {

            ItemStack current =
                    simulated.get(i);

            if (current.isEmpty()) {
                continue;
            }

            if (
                    !ItemStack.isSameItemSameComponents(
                            current,
                            output
                    )
            ) {
                continue;
            }

            if (
                    current.getCount()
                            + output.getCount()
                            <= current.getMaxStackSize()
            ) {

                return true;
            }
        }

        /*
         * 空きスロット
         */

        for (int i = 0; i < 3; i++) {

            if (simulated.get(i).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /*
     * ============================================================
     * Dirty Fluid Output Check
     * ============================================================
     */

    private boolean canInsertDirtyFluid(
            FluidStack fluid
    ) {

        if (fluid.isEmpty()) {
            return true;
        }

        FluidStack current =
                dirtyFluidTank.getFluid();

        if (current.isEmpty()) {

            return fluid.getAmount()
                    <= dirtyFluidTank.getCapacity();
        }

        if (
                !FluidStack.isSameFluidSameComponents(
                        current,
                        fluid
                )
        ) {

            return false;
        }

        return current.getAmount()
                + fluid.getAmount()
                <= dirtyFluidTank.getCapacity();
    }

    /*
     * ============================================================
     * Process
     * ============================================================
     */

    private boolean processRecipe(
            WashingRecipe recipe
    ) {

        /*
         * 最終チェック
         */

        if (
                inputTank.getFluidAmount()
                        < MINIMUM_FLUID_REQUIRED
        ) {
            return false;
        }

        if (
                energyStorage.getEnergyStored()
                        < recipe.getEnergy()
        ) {
            return false;
        }

        if (!canOutput(recipe)) {
            return false;
        }

        /*
         * --------------------------------------------------------
         * Fluid Input
         * --------------------------------------------------------
         */

        FluidStack requiredFluid =
                recipe.getInputFluid();

        FluidStack drained =
                inputTank.drain(
                        requiredFluid.getAmount(),
                        IFluidHandler.FluidAction.SIMULATE
                );

        if (
                drained.isEmpty()
                        || drained.getAmount()
                        < requiredFluid.getAmount()
        ) {

            return false;
        }

        /*
         * 液体の種類も確認
         */

        if (
                !FluidStack.isSameFluidSameComponents(
                        drained,
                        requiredFluid
                )
        ) {

            return false;
        }

        /*
         * --------------------------------------------------------
         * Input Fluid 消費
         * --------------------------------------------------------
         */

        inputTank.drain(
                requiredFluid.getAmount(),
                IFluidHandler.FluidAction.EXECUTE
        );

        /*
         * --------------------------------------------------------
         * Item Input 消費
         * --------------------------------------------------------
         */

        items.get(INPUT_SLOT)
                .shrink(1);

        /*
         * --------------------------------------------------------
         * Item Output
         * --------------------------------------------------------
         */

        insertOutputItem(
                recipe.getOutputItem()
        );

        /*
         * --------------------------------------------------------
         * Dirty Fluid Output
         * --------------------------------------------------------
         */

        dirtyFluidTank.fill(
                recipe.getDirtyFluid(),
                IFluidHandler.FluidAction.EXECUTE
        );

        /*
         * --------------------------------------------------------
         * Energy
         * --------------------------------------------------------
         */

        energyStorage.extractEnergy(
                recipe.getEnergy(),
                false
        );

        setChanged();

        return true;
    }

    /*
     * ============================================================
     * Insert Item Output
     * ============================================================
     */

    private void insertOutputItem(
            ItemStack output
    ) {

        if (output.isEmpty()) {
            return;
        }

        ItemStack remaining =
                output.copy();

        /*
         * まず既存スタックへ
         */

        for (
                int i = OUTPUT_1_SLOT;
                i <= OUTPUT_3_SLOT;
                i++
        ) {

            ItemStack current =
                    items.get(i);

            if (current.isEmpty()) {
                continue;
            }

            if (
                    !ItemStack.isSameItemSameComponents(
                            current,
                            remaining
                    )
            ) {
                continue;
            }

            int space =
                    current.getMaxStackSize()
                            - current.getCount();

            int amount =
                    Math.min(
                            space,
                            remaining.getCount()
                    );

            if (amount > 0) {

                current.grow(amount);

                remaining.shrink(amount);
            }

            if (remaining.isEmpty()) {
                return;
            }
        }

        /*
         * 空きスロットへ
         */

        for (
                int i = OUTPUT_1_SLOT;
                i <= OUTPUT_3_SLOT;
                i++
        ) {

            if (
                    !items.get(i).isEmpty()
            ) {
                continue;
            }

            items.set(
                    i,
                    remaining.copy()
            );

            return;
        }
    }

    /*
     * ============================================================
     * Getters
     * ============================================================
     */

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public IFluidHandler getFluidHandler() {
        return fluidHandler;
    }

    public ContainerData getContainerData() {
        return containerData;
    }

    public FluidTank getInputTank() {
        return inputTank;
    }

    public FluidTank getDirtyFluidTank() {
        return dirtyFluidTank;
    }

    public int getFluidAmount(
            int tank
    ) {

        if (tank == INPUT_TANK) {
            return inputTank.getFluidAmount();
        }

        if (tank == DIRTY_FLUID_TANK) {
            return dirtyFluidTank.getFluidAmount();
        }

        return 0;
    }

    public int getFluidCapacity(
            int tank
    ) {

        if (
                tank == INPUT_TANK
                        || tank == DIRTY_FLUID_TANK
        ) {

            return FLUID_CAPACITY;
        }

        return 0;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {

        WashingRecipe recipe =
                getCurrentRecipe();

        if (recipe == null) {
            return 1;
        }

        return recipe.getProcessTime();
    }

    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    public int getEnergyCapacity() {
        return ENERGY_CAPACITY;
    }

    /*
     * ============================================================
     * Container
     * ============================================================
     */

    @Override
    public ItemStack getItem(
            int slot
    ) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(
            int slot,
            int amount
    ) {

        ItemStack result =
                net.minecraft.world.ContainerHelper.removeItem(
                        items,
                        slot,
                        amount
                );

        if (!result.isEmpty()) {
            setChanged();
        }

        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(
            int slot
    ) {

        ItemStack result =
                items.get(slot);

        items.set(
                slot,
                ItemStack.EMPTY
        );

        return result;
    }

    @Override
    public void setItem(
            int slot,
            ItemStack stack
    ) {

        items.set(
                slot,
                stack
        );

        setChanged();
    }

    @Override
    public boolean stillValid(
            Player player
    ) {

        if (level == null) {
            return false;
        }

        if (
                level.getBlockEntity(
                        worldPosition
                ) != this
        ) {
            return false;
        }

        return player.distanceToSqr(
                worldPosition.getX() + 0.5,
                worldPosition.getY() + 0.5,
                worldPosition.getZ() + 0.5
        ) <= 64.0;
    }

    @Override
    public boolean canPlaceItem(
            int slot,
            ItemStack stack
    ) {

        return slot == INPUT_SLOT;
    }

    @Override
    public int getContainerSize() {
        return INVENTORY_SIZE;
    }

    @Override
    public boolean isEmpty() {

        for (ItemStack item : items) {

            if (!item.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void clearContent() {

        for (
                int i = 0;
                i < INVENTORY_SIZE;
                i++
        ) {

            items.set(
                    i,
                    ItemStack.EMPTY
            );
        }

        setChanged();
    }

    /*
     * ============================================================
     * Save
     * ============================================================
     */

    @Override
    protected void saveAdditional(
            CompoundTag tag,
            HolderLookup.Provider registries
    ) {

        super.saveAdditional(
                tag,
                registries
        );

        net.minecraft.world.ContainerHelper.saveAllItems(
                tag,
                items,
                registries
        );

        tag.putInt(
                "Progress",
                progress
        );

        tag.putInt(
                "Energy",
                energyStorage.getEnergyStored()
        );

        tag.put(
                "InputFluid",
                inputTank.writeToNBT(
                        registries,
                        new CompoundTag()
                )
        );

        tag.put(
                "DirtyFluid",
                dirtyFluidTank.writeToNBT(
                        registries,
                        new CompoundTag()
                )
        );
    }

    /*
     * ============================================================
     * Load
     * ============================================================
     */

    @Override
    protected void loadAdditional(
            CompoundTag tag,
            HolderLookup.Provider registries
    ) {

        super.loadAdditional(
                tag,
                registries
        );

        net.minecraft.world.ContainerHelper.loadAllItems(
                tag,
                items,
                registries
        );

        progress =
                tag.getInt("Progress");

        if (tag.contains("Energy")) {

            energyStorage.receiveEnergy(
                    tag.getInt("Energy"),
                    false
            );
        }

        if (tag.contains("InputFluid")) {

            inputTank.readFromNBT(
                    registries,
                    tag.getCompound(
                            "InputFluid"
                    )
            );
        }

        if (tag.contains("DirtyFluid")) {

            dirtyFluidTank.readFromNBT(
                    registries,
                    tag.getCompound(
                            "DirtyFluid"
                    )
            );
        }
    }
}