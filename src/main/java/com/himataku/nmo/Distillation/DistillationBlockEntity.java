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
     * Fluid Handler
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

                    if (tank < 0
                            || tank >= TANK_COUNT) {

                        return FluidStack.EMPTY;
                    }

                    return tanks[tank]
                            .getFluid()
                            .copy();
                }

                @Override
                public int getTankCapacity(
                        int tank
                ) {

                    if (tank < 0
                            || tank >= TANK_COUNT) {

                        return 0;
                    }

                    return tanks[tank]
                            .getCapacity();
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

                    return tanks[INPUT_TANK]
                            .fill(
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

                    for (
                            int i = OUTPUT_TANK_1;
                            i < TANK_COUNT;
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

                @Override
                public FluidStack drain(
                        int maxDrain,
                        FluidAction action
                ) {

                    if (maxDrain <= 0) {
                        return FluidStack.EMPTY;
                    }

                    for (
                            int i = OUTPUT_TANK_1;
                            i < TANK_COUNT;
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

        if (blockEntity.energy.getEnergyStored()
                < recipe.getEnergy()) {

            return;
        }

        blockEntity.progress++;

        if (blockEntity.progress
                >= recipe.getProcessingTime()) {

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

            System.out.println(
                    "[NMO] Distillation: input is empty"
            );

            return null;
        }

        System.out.println(
                "[NMO] Distillation input: "
                        + input.getFluid()
                        + " amount="
                        + input.getAmount()
        );

        DistillationRecipe.DistillationRecipeInput recipeInput =
                new DistillationRecipe.DistillationRecipeInput(
                        input.copy()
                );

        var result =
                level.getRecipeManager()
                        .getRecipeFor(
                                ModRecipes.DISTILLATION_TYPE.get(),
                                recipeInput,
                                level
                        );

        if (result.isPresent()) {

            DistillationRecipe recipe =
                    result.get().value();

            System.out.println(
                    "[NMO] Distillation recipe FOUND"
            );

            return recipe;
        }

        System.out.println(
                "[NMO] Distillation recipe NOT FOUND"
        );

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

        if (!recipe.getInput().test(input)) {
            return false;
        }

        /*
         * Check all fluid outputs first.
         */

        var outputs =
                recipe.getFluidOutputs();

        for (
                int i = 0;
                i < outputs.size();
                i++
        ) {

            FluidStack output =
                    outputs.get(i);

            int outputTank =
                    OUTPUT_TANK_1 + i;

            if (outputTank >= TANK_COUNT) {
                return false;
            }

            if (!canInsertFluid(
                    outputTank,
                    output
            )) {

                return false;
            }
        }

        /*
         * Check item output.
         */

        ItemStack itemOutput =
                recipe.getItemOutput();

        if (!canInsertItem(itemOutput)) {
            return false;
        }

        /*
         * Check energy.
         */

        if (energy.getEnergyStored()
                < recipe.getEnergy()) {

            return false;
        }

        /*
         * Everything is ready.
         */

        tanks[INPUT_TANK].drain(
                recipe.getInput().amount(),
                IFluidHandler.FluidAction.EXECUTE
        );

        /*
         * Insert fluids.
         */

        for (
                int i = 0;
                i < outputs.size();
                i++
        ) {

            FluidStack output =
                    outputs.get(i);

            tanks[OUTPUT_TANK_1 + i].fill(
                    output,
                    IFluidHandler.FluidAction.EXECUTE
            );
        }

        /*
         * Insert item.
         */

        insertItem(itemOutput);

        /*
         * Consume energy.
         */

        energy.extractEnergy(
                recipe.getEnergy(),
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

        if (tank < OUTPUT_TANK_1
                || tank >= TANK_COUNT) {

            return false;
        }

        if (stack.isEmpty()) {
            return true;
        }

        FluidStack current =
                tanks[tank]
                        .getFluid();

        if (current.isEmpty()) {

            return stack.getAmount()
                    <= tanks[tank]
                    .getCapacity();
        }

        if (!FluidStack.isSameFluidSameComponents(
                current,
                stack
        )) {

            return false;
        }

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

        if (!ItemStack.isSameItemSameComponents(
                current,
                stack
        )) {

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