package com.himataku.nmo.Distillation;

import com.himataku.nmo.ModMenus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;

import net.neoforged.neoforge.items.SlotItemHandler;

public class DistillationMenu
        extends AbstractContainerMenu {

    private final DistillationBlockEntity blockEntity;

    private final ContainerData data;

    public DistillationMenu(
            int containerId,
            Inventory playerInventory,
            FriendlyByteBuf buffer
    ) {

        this(
                containerId,
                playerInventory,
                (DistillationBlockEntity)
                        playerInventory.player.level()
                                .getBlockEntity(
                                        buffer.readBlockPos()
                                )
        );
    }

    public DistillationMenu(
            int containerId,
            Inventory playerInventory,
            DistillationBlockEntity blockEntity
    ) {

        super(
                ModMenus.DISTILLATION.get(),
                containerId
        );

        this.blockEntity =
                blockEntity;

        /*
         * ========================================================
         * Item Output
         * ========================================================
         */

        addSlot(
                new SlotItemHandler(
                        blockEntity.getItems(),
                        DistillationBlockEntity.ITEM_OUTPUT_SLOT,
                        152,
                        76
                ) {

                    @Override
                    public boolean mayPlace(
                            ItemStack stack
                    ) {
                        return false;
                    }
                }
        );

        /*
         * ========================================================
         * Player Inventory
         * ========================================================
         */

        for (int row = 0; row < 3; row++) {

            for (int column = 0; column < 9; column++) {

                addSlot(
                        new net.minecraft.world.inventory.Slot(
                                playerInventory,
                                column + row * 9 + 9,
                                8 + column * 18,
                                84 + row * 18
                        )
                );
            }
        }

        /*
         * ========================================================
         * Hotbar
         * ========================================================
         */

        for (int column = 0; column < 9; column++) {

            addSlot(
                    new net.minecraft.world.inventory.Slot(
                            playerInventory,
                            column,
                            8 + column * 18,
                            142
                    )
            );
        }

        /*
         * ========================================================
         * Data
         * ========================================================
         *
         * 0  progress
         * 1  max progress
         *
         * 2  energy
         * 3  max energy
         *
         * 4  tank 0 amount
         * 5  tank 0 capacity
         *
         * 6  tank 1 amount
         * 7  tank 1 capacity
         *
         * ...
         *
         * 22 tank 9 amount
         * 23 tank 9 capacity
         */

        this.data =
                new ContainerData() {

                    @Override
                    public int get(
                            int index
                    ) {

                        if (index == 0) {
                            return blockEntity.getProgress();
                        }

                        if (index == 1) {
                            return blockEntity.getMaxProgress();
                        }

                        if (index == 2) {
                            return blockEntity.getEnergyStored();
                        }

                        if (index == 3) {
                            return blockEntity.getEnergyCapacity();
                        }

                        int tank =
                                (index - 4) / 2;

                        boolean amount =
                                (index - 4) % 2 == 0;

                        if (tank >= 0
                                && tank < DistillationBlockEntity.TANK_COUNT) {

                            if (amount) {
                                return blockEntity.getFluidAmount(
                                        tank
                                );
                            }

                            return blockEntity.getFluidCapacity(
                                    tank
                            );
                        }

                        return 0;
                    }

                    @Override
                    public void set(
                            int index,
                            int value
                    ) {
                    }

                    @Override
                    public int getCount() {
                        return 24;
                    }
                };

        addDataSlots(data);
    }

    public int getProgress() {
        return data.get(0);
    }

    public int getMaxProgress() {
        return data.get(1);
    }

    public int getEnergy() {
        return data.get(2);
    }

    public int getMaxEnergy() {
        return data.get(3);
    }

    public int getFluidAmount(
            int tank
    ) {
        return data.get(
                4 + tank * 2
        );
    }

    public int getFluidCapacity(
            int tank
    ) {
        return data.get(
                5 + tank * 2
        );
    }

    @Override
    public ItemStack quickMoveStack(
            Player player,
            int index
    ) {

        /*
         * Machine item slot is index 0.
         * It is output-only.
         *
         * Therefore shift-click only extracts
         * from the machine output.
         */

        if (index == 0) {

            ItemStack slotStack =
                    slots.get(index)
                            .getItem();

            if (slotStack.isEmpty()) {
                return ItemStack.EMPTY;
            }

            ItemStack copy =
                    slotStack.copy();

            if (!moveItemStackTo(
                    slotStack,
                    1,
                    slots.size(),
                    true
            )) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slots.get(index)
                        .set(ItemStack.EMPTY);
            } else {
                slots.get(index)
                        .setChanged();
            }

            return copy;
        }

        /*
         * Player inventory -> machine
         *
         * There is NO item input slot.
         */

        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(
            Player player
    ) {
        return blockEntity.stillValid(
                player
        );
    }
}