package com.himataku.nmo;

import com.himataku.nmo.CrusherBlock.CrusherBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(
                    Registries.BLOCK_ENTITY_TYPE,
                    NewMineOrder.MODID
            );

    public static final DeferredHolder<
            BlockEntityType<?>,
            BlockEntityType<CrusherBlockEntity>
            > CRUSHER =
            BLOCK_ENTITY_TYPES.register(
                    "crusher",
                    () -> BlockEntityType.Builder.of(
                            CrusherBlockEntity::new,
                            com.himataku.nmo.customblock.AllBlock.CRUSHER.get()
                    ).build(null)
            );
}