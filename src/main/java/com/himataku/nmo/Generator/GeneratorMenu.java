package com.himataku.nmo.Generator;

import com.himataku.nmo.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class GeneratorMenu extends AbstractContainerMenu {

    private final GeneratorBlockEntity generator;
    private final ContainerData data;

    public GeneratorMenu(
            int containerId,
            Inventory inventory,
            GeneratorBlockEntity generator,
            ContainerData data
    ) {
        super(ModMenus.GENERATOR.get(), containerId);

        this.generator = generator;
        this.data = data;

        addDataSlots(data);

        addSlot(
                new Slot(
                        generator,
                        0,
                        80,
                        35
                ) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return generator.canInsertFuel(stack);
                    }

                    @Override
                    public boolean mayPickup(Player player) {
                        return false;
                    }
                }
        );

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

    public GeneratorMenu(
            int containerId,
            Inventory inventory,
            FriendlyByteBuf buffer
    ) {
        this(
                containerId,
                inventory,
                (GeneratorBlockEntity)
                        inventory.player.level().getBlockEntity(
                                buffer.readBlockPos()
                        ),
                new ContainerData() {

                    @Override
                    public int get(int index) {
                        return 0;
                    }

                    @Override
                    public void set(int index, int value) {
                    }

                    @Override
                    public int getCount() {
                        return 0;
                    }
                }
        );
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

        if (index == 0) {

            if (!moveItemStackTo(
                    stack,
                    1,
                    slots.size(),
                    true
            )) {
                return ItemStack.EMPTY;
            }

        } else {

            if (generator.canInsertFuel(stack)) {

                if (!moveItemStackTo(
                        stack,
                        0,
                        1,
                        false
                )) {
                    return ItemStack.EMPTY;
                }

            } else {

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
        return generator.stillValid(player);
    }

    public GeneratorBlockEntity getGenerator() {
        return generator;
    }

    public ContainerData getData() {
        return data;
    }
}