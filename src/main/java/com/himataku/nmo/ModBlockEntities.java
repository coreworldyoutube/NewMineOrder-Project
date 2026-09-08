package com.himataku.nmo;

import com.himataku.nmo.CrusherBlock.CrusherBlockEntity;
import com.himataku.nmo.Distillation.DistillationBlockEntity;
import com.himataku.nmo.ElectricFurnace.ElectricFurnaceBlockEntity;
import com.himataku.nmo.Generator.GeneratorBlockEntity;
import com.himataku.nmo.customblock.AllBlock;

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


    /*
     * =========================================================
     * Crusher
     * =========================================================
     */

    public static final DeferredHolder<
            BlockEntityType<?>,
            BlockEntityType<CrusherBlockEntity>
            > CRUSHER =
            BLOCK_ENTITY_TYPES.register(
                    "crusher",
                    () -> BlockEntityType.Builder.of(
                            CrusherBlockEntity::new,
                            AllBlock.CRUSHER.get()
                    ).build(null)
            );


    /*
     * =========================================================
     * Electric Furnace
     * =========================================================
     */

    public static final DeferredHolder<
            BlockEntityType<?>,
            BlockEntityType<ElectricFurnaceBlockEntity>
            > ELECTRIC_FURNACE =
            BLOCK_ENTITY_TYPES.register(
                    "electric_furnace",
                    () -> BlockEntityType.Builder.of(
                            ElectricFurnaceBlockEntity::new,
                            AllBlock.ELECTRIC_FURNACE.get()
                    ).build(null)
            );


    /*
     * =========================================================
     * Generator
     * =========================================================
     */

    public static final DeferredHolder<
            BlockEntityType<?>,
            BlockEntityType<GeneratorBlockEntity>
            > GENERATOR =
            BLOCK_ENTITY_TYPES.register(
                    "generator",
                    () -> BlockEntityType.Builder.of(
                            GeneratorBlockEntity::new,
                            AllBlock.GENERATOR.get()
                    ).build(null)
            );


    /*
     * =========================================================
     * Distillation
     * =========================================================
     */

    public static final DeferredHolder<
            BlockEntityType<?>,
            BlockEntityType<DistillationBlockEntity>
            > DISTILLATION =
            BLOCK_ENTITY_TYPES.register(
                    "distillation",
                    () -> BlockEntityType.Builder.of(
                            DistillationBlockEntity::new,
                            AllBlock.DISTILLATION.get()
                    ).build(null)
            );
}