package com.himataku.nmo.CrusherBlock;

import com.himataku.nmo.customblock.AllBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.entity.BlockEntityTicker;

public class CrusherBlock extends BaseEntityBlock {

    public static final net.neoforged.neoforge.registries.DeferredBlockEntityType<
            CrusherBlockEntity> CRUSHER_BLOCK_ENTITY =
            null;

    public CrusherBlock(
            BlockBehaviour.Properties properties
    ) {
        super(properties);
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
        return new CrusherBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useItemOn(
            net.minecraft.world.item.ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {
        return InteractionResult.PASS;
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {

        if (!level.isClientSide()) {

            BlockEntity blockEntity =
                    level.getBlockEntity(pos);

            if (blockEntity instanceof CrusherBlockEntity crusher) {

                player.openMenu(
                        new net.minecraft.world.MenuProvider() {

                            @Override
                            public net.minecraft.network.chat.Component getDisplayName() {
                                return net.minecraft.network.chat.Component.literal(
                                        "Crusher"
                                );
                            }

                            @Override
                            public net.minecraft.world.inventory.AbstractContainerMenu createMenu(
                                    int id,
                                    net.minecraft.world.entity.player.Inventory inventory,
                                    Player player
                            ) {
                                return new CrusherMenu(
                                        id,
                                        inventory,
                                        crusher,
                                        crusher.getContainerData()
                                );
                            }
                        }
                );
            }
        }

        return InteractionResult.sidedSuccess(
                level.isClientSide()
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

        return (level1, pos, state1, blockEntity) ->
                CrusherBlockEntity.tick(
                        level1,
                        pos,
                        state1,
                        (CrusherBlockEntity) blockEntity
                );
    }
}