package com.himataku.nmo.CrusherBlock;

import com.himataku.nmo.ModMenus;
import net.minecraft.network.RegistryFriendlyByteBuf;
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
        super(ModMenus.CRUSHER.get(), id);

        this.crusher = crusher;
        this.data = data;

        addDataSlots(data);

        addSlot(
                new Slot(
                        crusher,
                        CrusherBlockEntity.INPUT_SLOT,
                        56,
                        35
                )
        );

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
    }

    public CrusherMenu(
            int id,
            Inventory inventory,
            RegistryFriendlyByteBuf buffer
    ) {
        this(
                id,
                inventory,
                getCrusher(
                        inventory,
                        buffer
                )
        );
    }

    private CrusherMenu(
            int id,
            Inventory inventory,
            CrusherBlockEntity crusher
    ) {
        this(
                id,
                inventory,
                crusher,
                crusher.getContainerData()
        );
    }

    private static CrusherBlockEntity getCrusher(
            Inventory inventory,
            RegistryFriendlyByteBuf buffer
    ) {
        var blockPos = buffer.readBlockPos();

        if (!(inventory.player.level().getBlockEntity(blockPos)
                instanceof CrusherBlockEntity crusher)) {
            throw new IllegalStateException(
                    "CrusherBlockEntity not found at " + blockPos
            );
        }

        return crusher;
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
                    CrusherBlockEntity.INPUT_SLOT,
                    CrusherBlockEntity.INPUT_SLOT + 1,
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

    public int getProgress() {
        return data.get(0);
    }

    public int getProcessTime() {
        return data.get(1);
    }

    public int getEnergy() {
        return data.get(2);
    }

    public int getMaxEnergy() {
        return data.get(3);
    }
}