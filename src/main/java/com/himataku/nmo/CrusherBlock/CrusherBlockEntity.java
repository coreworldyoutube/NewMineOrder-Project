package com.himataku.nmo.CrusherBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;

public class CrusherBlockEntity extends BlockEntity implements Container {

    public static final int INPUT_SLOT = 0;

    public static final int OUTPUT_1_SLOT = 1;
    public static final int OUTPUT_2_SLOT = 2;
    public static final int OUTPUT_3_SLOT = 3;

    public static final int INVENTORY_SIZE = 4;

    public static final int MAX_ENERGY = 10_000;
    public static final int ENERGY_PER_TICK = 100;

    public static final int PROCESS_TIME = 20;

    private final NonNullList<ItemStack> items =
            NonNullList.withSize(
                    INVENTORY_SIZE,
                    ItemStack.EMPTY
            );

    private int progress = 0;

    private final EnergyStorage energyStorage =
            new EnergyStorage(
                    MAX_ENERGY,
                    MAX_ENERGY,
                    0
            );

    private final ContainerData containerData =
            new ContainerData() {

                @Override
                public int get(int index) {
                    return switch (index) {
                        case 0 -> progress;
                        case 1 -> PROCESS_TIME;
                        case 2 -> energyStorage.getEnergyStored();
                        case 3 -> energyStorage.getMaxEnergyStored();
                        default -> 0;
                    };
                }

                @Override
                public void set(int index, int value) {
                    if (index == 0) {
                        progress = value;
                    }
                }

                @Override
                public int getCount() {
                    return 4;
                }
            };

    public CrusherBlockEntity(
            BlockPos pos,
            BlockState state
    ) {
        super(
                com.himataku.nmo.ModBlockEntities.CRUSHER.get(),
                pos,
                state
        );
    }

    public static void tick(
            Level level,
            BlockPos pos,
            BlockState state,
            CrusherBlockEntity crusher
    ) {
        if (level.isClientSide()) {
            return;
        }

        CrusherRecipe recipe = crusher.getRecipe();

        if (recipe == null) {
            crusher.progress = 0;
            return;
        }

        if (crusher.items.get(INPUT_SLOT).isEmpty()) {
            crusher.progress = 0;
            return;
        }

        if (!crusher.canOutput(recipe)) {
            crusher.progress = 0;
            return;
        }

        if (crusher.energyStorage.getEnergyStored()
                < ENERGY_PER_TICK) {
            return;
        }

        crusher.energyStorage.extractEnergy(
                ENERGY_PER_TICK,
                false
        );

        crusher.progress++;

        if (crusher.progress >= PROCESS_TIME) {
            crusher.processRecipe(recipe);
            crusher.progress = 0;
        }

        crusher.setChanged();
    }

    private CrusherRecipe getRecipe() {
        if (level == null) {
            return null;
        }

        CrusherRecipeInput input =
                new CrusherRecipeInput(
                        items.get(INPUT_SLOT)
                );

        return level.getRecipeManager()
                .getRecipeFor(
                        CrusherRecipe.Type.INSTANCE,
                        input,
                        level
                )
                .map(holder -> holder.value())
                .orElse(null);
    }

    private boolean canOutput(CrusherRecipe recipe) {

        ItemStack[] outputs =
                recipe.getResults();

        for (
                int i = 0;
                i < outputs.length && i < 3;
                i++
        ) {

            ItemStack output = outputs[i];

            if (output.isEmpty()) {
                continue;
            }

            int slot =
                    OUTPUT_1_SLOT + i;

            ItemStack current =
                    items.get(slot);

            if (current.isEmpty()) {
                continue;
            }

            if (!ItemStack.isSameItemSameComponents(
                    current,
                    output
            )) {
                return false;
            }

            if (
                    current.getCount()
                            + output.getCount()
                            > current.getMaxStackSize()
            ) {
                return false;
            }
        }

        return true;
    }

    private void processRecipe(
            CrusherRecipe recipe
    ) {

        items.get(INPUT_SLOT).shrink(1);

        ItemStack[] outputs =
                recipe.getResults();

        for (
                int i = 0;
                i < outputs.length && i < 3;
                i++
        ) {

            ItemStack output =
                    outputs[i];

            if (output.isEmpty()) {
                continue;
            }

            int slot =
                    OUTPUT_1_SLOT + i;

            ItemStack current =
                    items.get(slot);

            if (current.isEmpty()) {

                items.set(
                        slot,
                        output.copy()
                );

            } else {

                current.grow(
                        output.getCount()
                );
            }
        }
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public ContainerData getContainerData() {
        return containerData;
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public ItemStack getItem(int slot) {
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

        for (int i = 0; i < INVENTORY_SIZE; i++) {

            items.set(
                    i,
                    ItemStack.EMPTY
            );
        }

        setChanged();
    }

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
    }

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

        energyStorage.receiveEnergy(
                tag.getInt("Energy"),
                false
        );
    }
}