package com.himataku.nmo.ElectricFurnace;

import com.himataku.nmo.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ElectricFurnaceMenu extends AbstractContainerMenu {

    private final ElectricFurnaceBlockEntity blockEntity;

    public ElectricFurnaceMenu(
            int containerId,
            Inventory inventory,
            FriendlyByteBuf buffer
    ) {

        this(
                containerId,
                inventory,
                getBlockEntity(inventory, buffer)
        );
    }

    public ElectricFurnaceMenu(
            int containerId,
            Inventory inventory,
            ElectricFurnaceBlockEntity blockEntity
    ) {

        super(
                ModMenus.ELECTRIC_FURNACE.get(),
                containerId
        );

        this.blockEntity = blockEntity;

        ItemStackHandler handler =
                blockEntity.getItemHandler();

        // =========================
        // 電気かまど 入力
        // =========================

        addSlot(
                new FurnaceSlot(
                        handler,
                        ElectricFurnaceBlockEntity.INPUT_SLOT,
                        56,
                        35
                )
        );

        // =========================
        // 電気かまど 出力
        // =========================

        addSlot(
                new FurnaceOutputSlot(
                        handler,
                        ElectricFurnaceBlockEntity.OUTPUT_SLOT,
                        116,
                        35
                )
        );

        // =========================
        // プレイヤーインベントリ
        // =========================

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

        // =========================
        // ホットバー
        // =========================

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

    private static ElectricFurnaceBlockEntity getBlockEntity(
            Inventory inventory,
            FriendlyByteBuf buffer
    ) {

        var player =
                inventory.player;

        var level =
                player.level();

        var pos =
                buffer.readBlockPos();

        var blockEntity =
                level.getBlockEntity(pos);

        if (!(blockEntity instanceof ElectricFurnaceBlockEntity furnace)) {

            throw new IllegalStateException(
                    "Electric Furnace BlockEntity not found at " + pos
            );
        }

        return furnace;
    }

    @Override
    public ItemStack quickMoveStack(
            Player player,
            int index
    ) {

        Slot slot =
                slots.get(index);

        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack =
                slot.getItem();

        ItemStack original =
                stack.copy();

        // 電気かまどのスロット
        if (index < 2) {

            if (!moveItemStackTo(
                    stack,
                    2,
                    slots.size(),
                    true
            )) {

                return ItemStack.EMPTY;
            }

            // プレイヤーインベントリ
        } else {

            if (!moveItemStackTo(
                    stack,
                    ElectricFurnaceBlockEntity.INPUT_SLOT,
                    ElectricFurnaceBlockEntity.INPUT_SLOT + 1,
                    false
            )) {

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

        return original;
    }

    @Override
    public boolean stillValid(Player player) {

        return blockEntity.getBlockPos()
                .distSqr(player.blockPosition())
                <= 64.0D;
    }

    public ElectricFurnaceBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public int getEnergy() {
        return blockEntity.getEnergy();
    }

    public int getMaxEnergy() {
        return blockEntity.getMaxEnergy();
    }

    public int getProgress() {
        return blockEntity.getProgress();
    }

    public int getProcessTime() {
        return blockEntity.getProcessTime();
    }

    // =========================================================
    // 入力スロット
    // =========================================================

    private static class FurnaceSlot extends Slot {

        private final ItemStackHandler handler;
        private final int handlerSlot;

        public FurnaceSlot(
                ItemStackHandler handler,
                int slot,
                int x,
                int y
        ) {

            super(
                    new FurnaceContainer(handler, slot),
                    0,
                    x,
                    y
            );

            this.handler = handler;
            this.handlerSlot = slot;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {

            return handler.isItemValid(
                    handlerSlot,
                    stack
            );
        }

        @Override
        public ItemStack getItem() {

            return handler.getStackInSlot(
                    handlerSlot
            );
        }

        @Override
        public void set(ItemStack stack) {

            handler.setStackInSlot(
                    handlerSlot,
                    stack
            );

            setChanged();
        }

        @Override
        public ItemStack remove(int amount) {

            ItemStack result =
                    handler.extractItem(
                            handlerSlot,
                            amount,
                            false
                    );

            setChanged();

            return result;
        }
    }

    // =========================================================
    // 出力スロット
    // =========================================================

    private static class FurnaceOutputSlot extends Slot {

        private final ItemStackHandler handler;
        private final int handlerSlot;

        public FurnaceOutputSlot(
                ItemStackHandler handler,
                int slot,
                int x,
                int y
        ) {

            super(
                    new FurnaceContainer(handler, slot),
                    0,
                    x,
                    y
            );

            this.handler = handler;
            this.handlerSlot = slot;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean mayPickup(
                Player player
        ) {
            return true;
        }

        @Override
        public ItemStack getItem() {

            return handler.getStackInSlot(
                    handlerSlot
            );
        }

        @Override
        public void set(ItemStack stack) {

            handler.setStackInSlot(
                    handlerSlot,
                    stack
            );

            setChanged();
        }

        @Override
        public ItemStack remove(int amount) {

            ItemStack result =
                    handler.extractItem(
                            handlerSlot,
                            amount,
                            false
                    );

            setChanged();

            return result;
        }
    }

    // =========================================================
    // ItemStackHandlerをContainerとして扱うためのクラス
    // =========================================================

    private static class FurnaceContainer
            implements net.minecraft.world.Container {

        private final ItemStackHandler handler;
        private final int slot;

        public FurnaceContainer(
                ItemStackHandler handler,
                int slot
        ) {

            this.handler = handler;
            this.slot = slot;
        }

        @Override
        public int getContainerSize() {
            return 1;
        }

        @Override
        public boolean isEmpty() {

            return handler
                    .getStackInSlot(slot)
                    .isEmpty();
        }

        @Override
        public ItemStack getItem(int index) {

            if (index != 0) {
                return ItemStack.EMPTY;
            }

            return handler.getStackInSlot(slot);
        }

        @Override
        public ItemStack removeItem(
                int index,
                int count
        ) {

            if (index != 0) {
                return ItemStack.EMPTY;
            }

            return handler.extractItem(
                    slot,
                    count,
                    false
            );
        }

        @Override
        public ItemStack removeItemNoUpdate(
                int index
        ) {

            if (index != 0) {
                return ItemStack.EMPTY;
            }

            ItemStack result =
                    handler.getStackInSlot(slot).copy();

            handler.setStackInSlot(
                    slot,
                    ItemStack.EMPTY
            );

            return result;
        }

        @Override
        public void setItem(
                int index,
                ItemStack stack
        ) {

            if (index == 0) {

                handler.setStackInSlot(
                        slot,
                        stack
                );
            }
        }

        @Override
        public void setChanged() {
        }

        @Override
        public boolean stillValid(
                Player player
        ) {
            return true;
        }

        @Override
        public void clearContent() {

            handler.setStackInSlot(
                    slot,
                    ItemStack.EMPTY
            );
        }
    }
}