package com.himataku.nmo.Washing;

import com.himataku.nmo.ModMenus;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class WashingMenu extends AbstractContainerMenu {

    private final WashingBlockEntity washing;
    private final ContainerData data;

    /*
     * ============================================================
     * Block Entity Constructor
     * ============================================================
     */

    public WashingMenu(
            int id,
            Inventory inventory,
            WashingBlockEntity washing
    ) {
        this(
                id,
                inventory,
                washing,
                washing.getContainerData()
        );
    }

    /*
     * ============================================================
     * Main Constructor
     * ============================================================
     */

    private WashingMenu(
            int id,
            Inventory inventory,
            WashingBlockEntity washing,
            ContainerData data
    ) {

        super(
                ModMenus.WASHING.get(),
                id
        );

        this.washing = washing;
        this.data = data;

        /*
         * --------------------------------------------------------
         * Input
         * --------------------------------------------------------
         */

        addSlot(
                new Slot(
                        washing,
                        WashingBlockEntity.INPUT_SLOT,
                        56,
                        35
                )
        );

        /*
         * --------------------------------------------------------
         * Output 1
         * --------------------------------------------------------
         */

        addSlot(
                new Slot(
                        washing,
                        WashingBlockEntity.OUTPUT_1_SLOT,
                        116,
                        17
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
         * --------------------------------------------------------
         * Output 2
         * --------------------------------------------------------
         */

        addSlot(
                new Slot(
                        washing,
                        WashingBlockEntity.OUTPUT_2_SLOT,
                        116,
                        35
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
         * --------------------------------------------------------
         * Output 3
         * --------------------------------------------------------
         */

        addSlot(
                new Slot(
                        washing,
                        WashingBlockEntity.OUTPUT_3_SLOT,
                        116,
                        53
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
         * --------------------------------------------------------
         * Player Inventory
         * --------------------------------------------------------
         */

        for (int row = 0; row < 3; row++) {

            for (int column = 0; column < 9; column++) {

                addSlot(
                        new Slot(
                                inventory,
                                column + row * 9 + 9,
                                8 + column * 18,
                                84 + row * 18
                        )
                );
            }
        }

        /*
         * --------------------------------------------------------
         * Hotbar
         * --------------------------------------------------------
         */

        for (int column = 0; column < 9; column++) {

            addSlot(
                    new Slot(
                            inventory,
                            column,
                            8 + column * 18,
                            142
                    )
            );
        }

        addDataSlots(data);
    }

    /*
     * ============================================================
     * Network Constructor
     * ============================================================
     */

    public WashingMenu(
            int id,
            Inventory inventory,
            RegistryFriendlyByteBuf buffer
    ) {

        this(
                id,
                inventory,
                getWashing(
                        inventory,
                        buffer
                )
        );
    }

    /*
     * ============================================================
     * Get Block Entity
     * ============================================================
     */

    private static WashingBlockEntity getWashing(
            Inventory inventory,
            RegistryFriendlyByteBuf buffer
    ) {

        var blockPos = buffer.readBlockPos();

        if (
                !(inventory.player.level()
                        .getBlockEntity(blockPos)
                        instanceof WashingBlockEntity washing)
        ) {

            throw new IllegalStateException(
                    "WashingBlockEntity not found at "
                            + blockPos
            );
        }

        return washing;
    }

    /*
     * ============================================================
     * Quick Move
     * ============================================================
     */

    @Override
    public ItemStack quickMoveStack(
            Player player,
            int index
    ) {

        Slot slot = slots.get(index);

        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();

        ItemStack copy = stack.copy();

        /*
         * --------------------------------------------------------
         * Machine → Player
         * --------------------------------------------------------
         */

        if (index < 4) {

            if (
                    !moveItemStackTo(
                            stack,
                            4,
                            slots.size(),
                            true
                    )
            ) {

                return ItemStack.EMPTY;
            }

            /*
             * --------------------------------------------------------
             * Player → Machine Input
             * --------------------------------------------------------
             */

        } else {

            if (
                    !moveItemStackTo(
                            stack,
                            WashingBlockEntity.INPUT_SLOT,
                            WashingBlockEntity.INPUT_SLOT + 1,
                            false
                    )
            ) {

                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {

            slot.set(
                    ItemStack.EMPTY
            );

        } else {

            slot.setChanged();
        }

        return copy;
    }

    /*
     * ============================================================
     * Valid
     * ============================================================
     */

    @Override
    public boolean stillValid(
            Player player
    ) {

        return washing.stillValid(
                player
        );
    }

    /*
     * ============================================================
     * Progress
     * ============================================================
     */

    public int getProgress() {
        return data.get(0);
    }

    public int getProcessTime() {
        return data.get(1);
    }

    /*
     * ============================================================
     * Energy
     * ============================================================
     */

    public int getEnergy() {
        return data.get(2);
    }

    public int getMaxEnergy() {
        return data.get(3);
    }

    /*
     * ============================================================
     * Input Fluid
     * ============================================================
     */

    public int getInputFluidAmount() {
        return data.get(4);
    }

    public int getInputFluidCapacity() {
        return data.get(5);
    }

    /*
     * ============================================================
     * Dirty Fluid
     * ============================================================
     */

    public int getDirtyFluidAmount() {
        return data.get(6);
    }

    public int getDirtyFluidCapacity() {
        return data.get(7);
    }
}
