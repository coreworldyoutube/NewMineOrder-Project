package com.himataku.nmo.CrusherBlock;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CrusherMenu extends AbstractContainerMenu {

    private final CrusherBlockEntity crusher;
    private final ContainerData data;

    public CrusherMenu(
            int id,
            Inventory inventory,
            CrusherBlockEntity crusher,
            ContainerData data
    ) {
        super(null, id);

        this.crusher = crusher;
        this.data = data;

        addDataSlots(data);

        // 入力
        addSlot(
                new Slot(
                        crusher,
                        CrusherBlockEntity.INPUT_SLOT,
                        56,
                        35
                )
        );

        // 出力1
        addSlot(
                new Slot(
                        crusher,
                        CrusherBlockEntity.OUTPUT_1_SLOT,
                        116,
                        17
                ) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false;
                    }
                }
        );

        // 出力2
        addSlot(
                new Slot(
                        crusher,
                        CrusherBlockEntity.OUTPUT_2_SLOT,
                        116,
                        35
                ) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false;
                    }
                }
        );

        // 出力3
        addSlot(
                new Slot(
                        crusher,
                        CrusherBlockEntity.OUTPUT_3_SLOT,
                        116,
                        53
                ) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false;
                    }
                }
        );

        // プレイヤーインベントリ
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {

                addSlot(
                        new Slot(
                                inventory,
                                col + row * 9 + 9,
                                8 + col * 18,
                                84 + row * 18
                        )
                );
            }
        }

        // ホットバー
        for (int col = 0; col < 9; col++) {

            addSlot(
                    new Slot(
                            inventory,
                            col,
                            8 + col * 18,
                            142
                    )
            );
        }
    }

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

        if (index < 4) {

            if (!moveItemStackTo(
                    stack,
                    4,
                    slots.size(),
                    true
            )) {
                return ItemStack.EMPTY;
            }

        } else {

            if (!moveItemStackTo(
                    stack,
                    0,
                    1,
                    false
            )) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return copy;
    }

    @Override
    public boolean stillValid(Player player) {
        return crusher.stillValid(player);
    }
}