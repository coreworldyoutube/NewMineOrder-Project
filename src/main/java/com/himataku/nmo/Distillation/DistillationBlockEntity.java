package com.himataku.nmo.Distillation;

import com.himataku.nmo.CrusherBlock.ModRecipes;
import com.himataku.nmo.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;

import org.jetbrains.annotations.NotNull;

public class DistillationBlockEntity
        extends BlockEntity {

    /*
     * ============================================================
     * Tank
     * ============================================================
     */

    public static final int INPUT_TANK = 0;

    public static final int OUTPUT_TANK_1 = 1;
    public static final int OUTPUT_TANK_2 = 2;
    public static final int OUTPUT_TANK_3 = 3;
    public static final int OUTPUT_TANK_4 = 4;
    public static final int OUTPUT_TANK_5 = 5;
    public static final int OUTPUT_TANK_6 = 6;
    public static final int OUTPUT_TANK_7 = 7;
    public static final int OUTPUT_TANK_8 = 8;
    public static final int OUTPUT_TANK_9 = 9;

    public static final int TANK_COUNT = 10;

    /*
     * ============================================================
     * Item
     * ============================================================
     */

    public static final int ITEM_OUTPUT_SLOT = 0;

    /*
     * ============================================================
     * Machine
     * ============================================================
     */

    public static final int FLUID_CAPACITY = 10_000;

    public static final int ENERGY_CAPACITY = 100_000;

    public static final int ENERGY_RECEIVE = 10_000;

    /*
     * ============================================================
     * Progress
     * ============================================================
     */

    private int progress = 0;

    /*
     * ============================================================
     * Tanks
     * ============================================================
     */

    private final FluidTank[] tanks =
            new FluidTank[TANK_COUNT];

    /*
     * ============================================================
     * Item Output
     * ============================================================
     */

    private final ItemStackHandler items =
            new ItemStackHandler(1) {

                @Override
                protected void onContentsChanged(
                        int slot
                ) {
                    setChanged();
                }

                @Override
                public boolean isItemValid(
                        int slot,
                        @NotNull ItemStack stack
                ) {
                    return false;
                }
            };

    /*
     * ============================================================
     * Energy
     * ============================================================
     */

    private final EnergyStorage energy =
            new EnergyStorage(
                    ENERGY_CAPACITY,
                    ENERGY_RECEIVE,
                    ENERGY_CAPACITY
            );

    public EnergyStorage getEnergyStorage() {
        return energy;
    }

    /*
     * ============================================================
     * Active Tank Count
     * ============================================================
     *
     * 外部に公開するタンク数。
     *
     * 例：
     *
     * 2出力レシピ
     * 0 = Input
     * 1 = Output 1
     * 2 = Output 2
     *
     * つまり getTanks() = 3
     *
     * さらに、入力が空になった後でも
     * 出力液体が残っていれば、そのタンクを公開する。
     *
     * ============================================================
     */

    private int getActiveTankCount() {

        int outputCount = 0;

        /*
         * 現在のレシピが要求している出力数
         */

        DistillationRecipe recipe =
                getCurrentRecipe();

        if (recipe != null) {

            outputCount =
                    recipe.getFluidOutputs().size();
        }

        /*
         * 現在保存されている出力液体も確認する。
         *
         * 入力タンクが空になった後でも、
         * 既に作られた液体を外部から取り出せるようにする。
         */

        for (
                int i = OUTPUT_TANK_1;
                i < TANK_COUNT;
                i++
        ) {

            if (!tanks[i].getFluid().isEmpty()) {

                int count =
                        i - OUTPUT_TANK_1 + 1;

                outputCount =
                        Math.max(
                                outputCount,
                                count
                        );
            }
        }

        /*
         * Input 1個 + Outputの数
         */

        int result =
                1 + outputCount;

        /*
         * 念のため最大10タンクまで。
         */

        return Math.min(
                result,
                TANK_COUNT
        );
    }

    /*
     * ============================================================
     * Fluid Handler
     * ============================================================
     */

    private final IFluidHandler fluidHandler =
            new IFluidHandler() {

                /*
                 * ------------------------------------------------
                 * Tank Count
                 * ------------------------------------------------
                 */

                @Override
                public int getTanks() {

                    return getActiveTankCount();
                }

                /*
                 * ------------------------------------------------
                 * Fluid In Tank
                 * ------------------------------------------------
                 */

                @Override
                public FluidStack getFluidInTank(
                        int tank
                ) {

                    if (
                            tank < 0
                                    || tank >= getActiveTankCount()
                    ) {

                        return FluidStack.EMPTY;
                    }

                    return tanks[tank]
                            .getFluid()
                            .copy();
                }

                /*
                 * ------------------------------------------------
                 * Tank Capacity
                 * ------------------------------------------------
                 */

                @Override
                public int getTankCapacity(
                        int tank
                ) {

                    if (
                            tank < 0
                                    || tank >= getActiveTankCount()
                    ) {

                        return 0;
                    }

                    return tanks[tank]
                            .getCapacity();
                }

                /*
                 * ------------------------------------------------
                 * Valid Fluid
                 * ------------------------------------------------
                 *
                 * 外部から液体を入れられるのはInputだけ。
                 *
                 * ------------------------------------------------
                 */

                @Override
                public boolean isFluidValid(
                        int tank,
                        FluidStack stack
                ) {

                    return tank == INPUT_TANK;
                }

                /*
                 * ------------------------------------------------
                 * Fill
                 * ------------------------------------------------
                 *
                 * 外部から入れる液体は必ずInputへ。
                 *
                 * ------------------------------------------------
                 */

                @Override
                public int fill(
                        FluidStack resource,
                        FluidAction action
                ) {

                    if (resource.isEmpty()) {
                        return 0;
                    }

                    return tanks[INPUT_TANK]
                            .fill(
                                    resource,
                                    action
                            );
                }

                /*
                 * ------------------------------------------------
                 * Drain Specific Fluid
                 * ------------------------------------------------
                 *
                 * 現在公開されている出力タンクだけを見る。
                 *
                 * ------------------------------------------------
                 */

                @Override
                public FluidStack drain(
                        FluidStack resource,
                        FluidAction action
                ) {

                    if (resource.isEmpty()) {
                        return FluidStack.EMPTY;
                    }

                    int activeTanks =
                            getActiveTankCount();

                    for (
                            int i = OUTPUT_TANK_1;
                            i < activeTanks;
                            i++
                    ) {

                        FluidStack result =
                                tanks[i].drain(
                                        resource,
                                        action
                                );

                        if (!result.isEmpty()) {

                            return result;
                        }
                    }

                    return FluidStack.EMPTY;
                }

                /*
                 * ------------------------------------------------
                 * Drain Any Fluid
                 * ------------------------------------------------
                 *
                 * 出力1 → 出力2 → 出力3...
                 * の順番で取り出す。
                 *
                 * ------------------------------------------------
                 */

                @Override
                public FluidStack drain(
                        int maxDrain,
                        FluidAction action
                ) {

                    if (maxDrain <= 0) {
                        return FluidStack.EMPTY;
                    }

                    int activeTanks =
                            getActiveTankCount();

                    for (
                            int i = OUTPUT_TANK_1;
                            i < activeTanks;
                            i++
                    ) {

                        FluidStack result =
                                tanks[i].drain(
                                        maxDrain,
                                        action
                                );

                        if (!result.isEmpty()) {

                            return result;
                        }
                    }

                    return FluidStack.EMPTY;
                }
            };

    /*
     * ============================================================
     * Constructor
     * ============================================================
     */

    public DistillationBlockEntity(
            BlockPos pos,
            BlockState state
    ) {

        super(
                ModBlockEntities.DISTILLATION.get(),
                pos,
                state
        );

        for (
                int i = 0;
                i < TANK_COUNT;
                i++
        ) {

            tanks[i] =
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
        }
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
            DistillationBlockEntity blockEntity
    ) {

        if (level.isClientSide()) {
            return;
        }

        DistillationRecipe recipe =
                blockEntity.getCurrentRecipe();

        if (recipe == null) {

            if (blockEntity.progress != 0) {

                blockEntity.progress = 0;

                blockEntity.setChanged();
            }

            return;
        }

        if (
                blockEntity.energy.getEnergyStored()
                        < recipe.getEnergy()
        ) {

            return;
        }

        blockEntity.progress++;

        if (
                blockEntity.progress
                        >= recipe.getProcessingTime()
        ) {

            if (blockEntity.processRecipe(recipe)) {

                blockEntity.progress = 0;

                blockEntity.setChanged();

            } else {

                blockEntity.progress = 0;

                blockEntity.setChanged();
            }
        }
    }

    /*
     * ============================================================
     * Recipe
     * ============================================================
     */

    private DistillationRecipe getCurrentRecipe() {

        FluidStack input =
                tanks[INPUT_TANK]
                        .getFluid();

        if (input.isEmpty()) {

            return null;
        }

        DistillationRecipe.DistillationRecipeInput recipeInput =
                new DistillationRecipe.DistillationRecipeInput(
                        input.copy()
                );

        var recipes =
                level.getRecipeManager()
                        .getAllRecipesFor(
                                ModRecipes.DISTILLATION_TYPE.get()
                        );

        for (var holder : recipes) {

            DistillationRecipe recipe =
                    holder.value();

            if (
                    recipe.matches(
                            recipeInput,
                            level
                    )
            ) {

                return recipe;
            }
        }

        return null;
    }

    /*
     * ============================================================
     * Process
     * ============================================================
     */

    private boolean processRecipe(
            DistillationRecipe recipe
    ) {

        FluidStack input =
                tanks[INPUT_TANK]
                        .getFluid();

        /*
         * --------------------------------------------------------
         * Input Check
         * --------------------------------------------------------
         */

        if (!recipe.getInput().test(input)) {

            return false;
        }

        /*
         * --------------------------------------------------------
         * Fluid Outputs
         * --------------------------------------------------------
         */

        var outputs =
                recipe.getFluidOutputs();

        /*
         * 最大9出力
         */

        if (outputs.size() > TANK_COUNT - 1) {

            return false;
        }

        /*
         * --------------------------------------------------------
         * Check all fluid outputs first.
         * --------------------------------------------------------
         */

        for (
                int i = 0;
                i < outputs.size();
                i++
        ) {

            FluidStack output =
                    outputs.get(i);

            int outputTank =
                    OUTPUT_TANK_1 + i;

            if (
                    !canInsertFluid(
                            outputTank,
                            output
                    )
            ) {

                return false;
            }
        }

        /*
         * --------------------------------------------------------
         * Item Output Check
         * --------------------------------------------------------
         */

        ItemStack itemOutput =
                recipe.getItemOutput();

        if (!canInsertItem(itemOutput)) {

            return false;
        }

        /*
         * --------------------------------------------------------
         * Energy Check
         * --------------------------------------------------------
         */

        int storedEnergy =
                energy.getEnergyStored();

        int requiredEnergy =
                recipe.getEnergy();

        if (storedEnergy < requiredEnergy) {

            return false;
        }

        /*
         * --------------------------------------------------------
         * Everything is ready.
         * --------------------------------------------------------
         */

        int inputAmount =
                recipe.getInput().amount();

        /*
         * --------------------------------------------------------
         * Drain Input
         * --------------------------------------------------------
         */

        FluidStack drained =
                tanks[INPUT_TANK].drain(
                        inputAmount,
                        IFluidHandler.FluidAction.EXECUTE
                );

        if (drained.isEmpty()) {

            return false;
        }

        /*
         * --------------------------------------------------------
         * Insert Fluid Outputs
         * --------------------------------------------------------
         *
         * JSONの配列順にタンクへ入れる。
         *
         * fluid_outputs[0] → Tank 1
         * fluid_outputs[1] → Tank 2
         * fluid_outputs[2] → Tank 3
         * ...
         *
         * --------------------------------------------------------
         */

        for (
                int i = 0;
                i < outputs.size();
                i++
        ) {

            FluidStack output =
                    outputs.get(i);

            int tank =
                    OUTPUT_TANK_1 + i;

            tanks[tank].fill(
                    output,
                    IFluidHandler.FluidAction.EXECUTE
            );
        }

        /*
         * --------------------------------------------------------
         * Insert Item
         * --------------------------------------------------------
         */

        if (!itemOutput.isEmpty()) {

            insertItem(itemOutput);
        }

        /*
         * --------------------------------------------------------
         * Consume Energy
         * --------------------------------------------------------
         */

        energy.extractEnergy(
                requiredEnergy,
                false
        );

        setChanged();

        return true;
    }

    /*
     * ============================================================
     * Fluid Output Check
     * ============================================================
     */

    private boolean canInsertFluid(
            int tank,
            FluidStack stack
    ) {

        if (
                tank < OUTPUT_TANK_1
                        || tank >= TANK_COUNT
        ) {

            return false;
        }

        if (stack.isEmpty()) {

            return true;
        }

        FluidStack current =
                tanks[tank]
                        .getFluid();

        /*
         * 空ならそのまま入る。
         */

        if (current.isEmpty()) {

            return stack.getAmount()
                    <= tanks[tank]
                    .getCapacity();
        }

        /*
         * 同じ液体でなければ混ざらない。
         */

        if (
                !FluidStack.isSameFluidSameComponents(
                        current,
                        stack
                )
        ) {

            return false;
        }

        /*
         * 容量チェック。
         */

        return current.getAmount()
                + stack.getAmount()
                <= tanks[tank].getCapacity();
    }

    /*
     * ============================================================
     * Item Output Check
     * ============================================================
     */

    private boolean canInsertItem(
            ItemStack stack
    ) {

        if (stack.isEmpty()) {

            return true;
        }

        ItemStack current =
                items.getStackInSlot(
                        ITEM_OUTPUT_SLOT
                );

        if (current.isEmpty()) {

            return true;
        }

        if (
                !ItemStack.isSameItemSameComponents(
                        current,
                        stack
                )
        ) {

            return false;
        }

        return current.getCount()
                + stack.getCount()
                <= current.getMaxStackSize();
    }

    /*
     * ============================================================
     * Insert Item
     * ============================================================
     */

    private void insertItem(
            ItemStack stack
    ) {

        if (stack.isEmpty()) {

            return;
        }

        items.insertItem(
                ITEM_OUTPUT_SLOT,
                stack.copy(),
                false
        );
    }

    /*
     * ============================================================
     * Getters
     * ============================================================
     */

    public ItemStackHandler getItems() {

        return items;
    }

    public EnergyStorage getEnergy() {

        return energy;
    }

    public IFluidHandler getFluidHandler() {

        return fluidHandler;
    }

    public FluidTank getTank(
            int index
    ) {

        return tanks[index];
    }

    public int getFluidAmount(
            int tank
    ) {

        return tanks[tank]
                .getFluidAmount();
    }

    public int getFluidCapacity(
            int tank
    ) {

        return tanks[tank]
                .getCapacity();
    }

    public int getProgress() {

        return progress;
    }

    public int getMaxProgress() {

        DistillationRecipe recipe =
                getCurrentRecipe();

        if (recipe == null) {

            return 1;
        }

        return recipe.getProcessingTime();
    }

    public int getEnergyStored() {

        return energy.getEnergyStored();
    }

    public int getEnergyCapacity() {

        return ENERGY_CAPACITY;
    }

    /*
     * ============================================================
     * Item Output
     * ============================================================
     */

    public ItemStack getItemOutput() {

        return items.getStackInSlot(
                ITEM_OUTPUT_SLOT
        );
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

        tag.put(
                "Items",
                items.serializeNBT(
                        registries
                )
        );

        tag.putInt(
                "Progress",
                progress
        );

        tag.putInt(
                "Energy",
                energy.getEnergyStored()
        );

        for (
                int i = 0;
                i < TANK_COUNT;
                i++
        ) {

            tag.put(
                    "Fluid" + i,
                    tanks[i].writeToNBT(
                            registries,
                            new CompoundTag()
                    )
            );
        }
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

        if (tag.contains("Items")) {

            items.deserializeNBT(
                    registries,
                    tag.getCompound("Items")
            );
        }

        progress =
                tag.getInt("Progress");

        if (tag.contains("Energy")) {

            energy.receiveEnergy(
                    tag.getInt("Energy"),
                    false
            );
        }

        for (
                int i = 0;
                i < TANK_COUNT;
                i++
        ) {

            String key =
                    "Fluid" + i;

            if (tag.contains(key)) {

                tanks[i].readFromNBT(
                        registries,
                        tag.getCompound(key)
                );
            }
        }
    }

    /*
     * ============================================================
     * Valid
     * ============================================================
     */

    public boolean stillValid(
            Player player
    ) {

        return net.minecraft.world.Container
                .stillValidBlockEntity(
                        this,
                        player
                );
    }
}