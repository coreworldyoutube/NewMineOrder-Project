package com.himataku.nmo.Generator;

import com.himataku.nmo.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class GeneratorBlock extends BaseEntityBlock {

    public static final MapCodec<GeneratorBlock> CODEC =
            simpleCodec(GeneratorBlock::new);

    public GeneratorBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(
            BlockPos pos,
            BlockState state
    ) {
        return new GeneratorBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> type
    ) {
        if (level.isClientSide()) {
            return null;
        }

        return createTickerHelper(
                type,
                ModBlockEntities.GENERATOR.get(),
                GeneratorBlockEntity::tick
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

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.PASS;
        }

        BlockEntity blockEntity =
                level.getBlockEntity(pos);

        if (!(blockEntity instanceof GeneratorBlockEntity generator)) {
            return InteractionResult.PASS;
        }

        serverPlayer.openMenu(
                new MenuProvider() {

                    @Override
                    public Component getDisplayName() {
                        return Component.literal("Generator");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(
                            int id,
                            Inventory inventory,
                            Player player
                    ) {
                        return new GeneratorMenu(
                                id,
                                inventory,
                                generator,
                                generator.getContainerData()
                        );
                    }
                },
                (RegistryFriendlyByteBuf buffer) -> {
                    buffer.writeBlockPos(pos);
                }
        );

        return InteractionResult.CONSUME;
    }

    @Override
    protected void onRemove(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState newState,
            boolean movedByPiston
    ) {
        if (!state.is(newState.getBlock())) {

            BlockEntity blockEntity =
                    level.getBlockEntity(pos);

            if (blockEntity instanceof GeneratorBlockEntity generator) {
                generator.dropContents();
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