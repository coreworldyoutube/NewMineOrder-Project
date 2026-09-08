package com.himataku.nmo.Distillation;

import com.himataku.nmo.ModBlockEntities;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
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

import org.jetbrains.annotations.Nullable;

public class DistillationBlock extends BaseEntityBlock {

    public static final MapCodec<DistillationBlock> CODEC =
            simpleCodec(DistillationBlock::new);

    public DistillationBlock(
            BlockBehaviour.Properties properties
    ) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(
            BlockState state
    ) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(
            BlockPos pos,
            BlockState state
    ) {
        return new DistillationBlockEntity(
                pos,
                state
        );
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
                ModBlockEntities.DISTILLATION.get(),
                DistillationBlockEntity::tick
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

        if (!(blockEntity
                instanceof DistillationBlockEntity distillation)) {

            return InteractionResult.PASS;
        }

        player.openMenu(
                new MenuProvider() {

                    @Override
                    public Component getDisplayName() {
                        return Component.literal(
                                "Distillation"
                        );
                    }

                    @Override
                    public AbstractContainerMenu createMenu(
                            int containerId,
                            Inventory inventory,
                            Player player
                    ) {
                        return new DistillationMenu(
                                containerId,
                                inventory,
                                distillation
                        );
                    }
                },

                (RegistryFriendlyByteBuf buffer) -> {
                    buffer.writeBlockPos(pos);
                }
        );

        return InteractionResult.CONSUME;
    }
}