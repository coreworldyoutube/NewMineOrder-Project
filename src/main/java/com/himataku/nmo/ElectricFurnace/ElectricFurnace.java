package com.himataku.nmo.ElectricFurnace;

import com.himataku.nmo.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ElectricFurnace extends BaseEntityBlock {

    public ElectricFurnace(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(ElectricFurnace::new);
    }

    @Override
    public BlockEntity newBlockEntity(
            BlockPos pos,
            BlockState state
    ) {
        return new ElectricFurnaceBlockEntity(pos, state);
    }

    // =========================================================
    // Tick処理
    // =========================================================

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> type
    ) {
        return createTickerHelper(
                type,
                ModBlockEntities.ELECTRIC_FURNACE.get(),
                ElectricFurnaceBlockEntity::tick
        );
    }

    // =========================================================
    // 描画
    // =========================================================

    @Override
    protected RenderShape getRenderShape(
            BlockState state
    ) {
        return RenderShape.MODEL;
    }

    // =========================================================
    // 右クリック
    // =========================================================

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {
        if (!level.isClientSide) {

            BlockEntity blockEntity =
                    level.getBlockEntity(pos);

            if (blockEntity instanceof ElectricFurnaceBlockEntity furnace) {

                player.openMenu(
                        new SimpleMenuProvider(
                                (containerId, inventory, p) ->
                                        new ElectricFurnaceMenu(
                                                containerId,
                                                inventory,
                                                furnace
                                        ),
                                Component.literal("Electric Furnace")
                        ),
                        buf -> buf.writeBlockPos(pos)
                );
            }
        }

        return InteractionResult.sidedSuccess(
                level.isClientSide
        );
    }

    // =========================================================
    // ブロック破壊時
    // =========================================================

    @Override
    protected void onRemove(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState newState,
            boolean isMoving
    ) {
        if (!state.is(newState.getBlock())) {

            BlockEntity blockEntity =
                    level.getBlockEntity(pos);

            if (blockEntity instanceof ElectricFurnaceBlockEntity furnace) {
                furnace.dropContents();
            }

            super.onRemove(
                    state,
                    level,
                    pos,
                    newState,
                    isMoving
            );
        }
    }
}