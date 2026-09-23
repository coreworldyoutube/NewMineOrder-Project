package com.himataku.nmo.Washing;

import com.himataku.nmo.ModBlockEntities;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class WashingBlock extends BaseEntityBlock {

    public static final MapCodec<WashingBlock> CODEC =
            simpleCodec(WashingBlock::new);

    public WashingBlock(
            Properties properties
    ) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(
            BlockPos pos,
            BlockState state
    ) {
        return new WashingBlockEntity(
                pos,
                state
        );
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            net.minecraft.world.level.block.entity.BlockEntityType<T> type
    ) {

        if (level.isClientSide()) {
            return null;
        }

        if (type != ModBlockEntities.WASHING.get()) {
            return null;
        }

        return (level1, pos, state1, blockEntity) ->
                WashingBlockEntity.tick(
                        level1,
                        pos,
                        state1,
                        (WashingBlockEntity) blockEntity
                );
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity =
                level.getBlockEntity(pos);

        if (!(blockEntity instanceof WashingBlockEntity washing)) {
            return InteractionResult.PASS;
        }

        if (player instanceof ServerPlayer serverPlayer) {

            serverPlayer.openMenu(
                    new MenuProvider() {

                        @Override
                        public Component getDisplayName() {
                            return Component.translatable(
                                    "block.nmo.washing"
                            );
                        }

                        @Override
                        public AbstractContainerMenu createMenu(
                                int containerId,
                                Inventory inventory,
                                Player player
                        ) {
                            return new WashingMenu(
                                    containerId,
                                    inventory,
                                    washing
                            );
                        }
                    },
                    buffer -> buffer.writeBlockPos(pos)
            );
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public RenderShape getRenderShape(
            BlockState state
    ) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState newState,
            boolean movedByPiston
    ) {

        if (state.getBlock() != newState.getBlock()) {

            BlockEntity blockEntity =
                    level.getBlockEntity(pos);

            if (blockEntity instanceof WashingBlockEntity washing) {

                Containers.dropContents(
                        level,
                        pos,
                        washing
                );
            }
        }

        super.onRemove(
                state,
                level,
                pos,
                newState,
                movedByPiston
        );
    }
}