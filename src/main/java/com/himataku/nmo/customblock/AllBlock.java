package com.himataku.nmo.customblock;

import com.himataku.nmo.CrusherBlock.CrusherBlock;
import com.himataku.nmo.Distillation.DistillationBlock;
import com.himataku.nmo.ElectricFurnace.ElectricFurnace;
import com.himataku.nmo.Generator.GeneratorBlock;
import com.himataku.nmo.NewMineOrder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllBlock {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(NewMineOrder.MODID);

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(NewMineOrder.MODID);

    public static final DeferredBlock<Block> BERYLLIUM_BLOCK =
            BLOCKS.register("beryllium_block", () ->
                    new Block(
                            BlockBehaviour.Properties.of()
                                    .destroyTime(2.0f)
                                    .explosionResistance(10.0f)
                                    .sound(SoundType.STONE)
                                    .lightLevel(state -> 7)
                                    .requiresCorrectToolForDrops()
                    )
            );

    public static final DeferredBlock<Block> BERYLLIUM_ORE =
            BLOCKS.register("beryllium_ore", () ->
                    new Block(
                            BlockBehaviour.Properties.of()
                                    .destroyTime(2.0f)
                                    .explosionResistance(10.0f)
                                    .sound(SoundType.STONE)
                                    .lightLevel(state -> 7)
                                    .requiresCorrectToolForDrops()
                    )
            );

    public static final DeferredBlock<Block> DEEPSLATE_BERYLLIUM_ORE =
            BLOCKS.register("deepslate_beryllium_ore", () ->
                    new Block(
                            BlockBehaviour.Properties.of()
                                    .destroyTime(2.0f)
                                    .explosionResistance(10.0f)
                                    .sound(SoundType.STONE)
                                    .lightLevel(state -> 7)
                                    .requiresCorrectToolForDrops()
                    )
            );

    public static final DeferredBlock<Block> TUNGSTEN_BLOCK =
            BLOCKS.register("tungsten_block", () ->
                    new Block(
                            BlockBehaviour.Properties.of()
                                    .destroyTime(2.0f)
                                    .explosionResistance(10.0f)
                                    .sound(SoundType.STONE)
                                    .lightLevel(state -> 7)
                                    .requiresCorrectToolForDrops()
                    )
            );

    public static final DeferredBlock<Block> TUNGSTEN_ORE =
            BLOCKS.register("tungsten_ore", () ->
                    new Block(
                            BlockBehaviour.Properties.of()
                                    .destroyTime(2.0f)
                                    .explosionResistance(10.0f)
                                    .sound(SoundType.STONE)
                                    .lightLevel(state -> 7)
                                    .requiresCorrectToolForDrops()
                    )
            );

    public static final DeferredBlock<Block> DEEPSLATE_TUNGSTEN_ORE =
            BLOCKS.register("deepslate_tungsten_ore", () ->
                    new Block(
                            BlockBehaviour.Properties.of()
                                    .destroyTime(2.0f)
                                    .explosionResistance(10.0f)
                                    .sound(SoundType.STONE)
                                    .lightLevel(state -> 7)
                                    .requiresCorrectToolForDrops()
                    )
            );

    public static final DeferredBlock<Block> CRUSHER =
            BLOCKS.register("crusher", () ->
                    new CrusherBlock(
                            BlockBehaviour.Properties.of()
                                    .destroyTime(3.0f)
                                    .explosionResistance(10.0f)
                                    .sound(SoundType.METAL)
                                    .requiresCorrectToolForDrops()
                    )
            );

    public static final DeferredBlock<Block> TITAN_BLOCK =
            BLOCKS.register("titan_block", () ->
                    new Block(
                            BlockBehaviour.Properties.of()
                                    .destroyTime(3.0f)
                                    .explosionResistance(10.0f)
                                    .sound(SoundType.STONE)
                                    .lightLevel(state -> 7)
                                    .requiresCorrectToolForDrops()
                    )
            );

    public static final DeferredBlock<Block> TITAN_ORE =
            BLOCKS.register("titan_ore", () ->
                    new Block(
                            BlockBehaviour.Properties.of()
                                    .destroyTime(3.0f)
                                    .explosionResistance(10.0f)
                                    .sound(SoundType.STONE)
                                    .lightLevel(state -> 7)
                                    .requiresCorrectToolForDrops()
                    )
            );

    public static final DeferredBlock<Block> MONAZITE_SAND =
            BLOCKS.register("monazite_sand", () ->
                    new Block(
                            BlockBehaviour.Properties.of()
                                    .explosionResistance(10.0f)
                                    .sound(SoundType.SAND)
                                    .speedFactor(0.75f)
                    )
            );

    public static final DeferredBlock<Block> ELECTRIC_FURNACE =
            BLOCKS.register("electric_furnace", () ->
                    new ElectricFurnace(
                            BlockBehaviour.Properties.of()
                                    .destroyTime(3.0f)
                                    .explosionResistance(10.0f)
                                    .sound(SoundType.METAL)
                                    .requiresCorrectToolForDrops()
                    )
            );

    public static final DeferredBlock<Block> GENERATOR =
            BLOCKS.register("generator", () ->
                    new GeneratorBlock(
                            BlockBehaviour.Properties.of()
                                    .destroyTime(3.0f)
                                    .explosionResistance(10.0f)
                                    .sound(SoundType.METAL)
                                    .requiresCorrectToolForDrops()
                    )
            );

    public static final DeferredBlock<Block> DISTILLATION =
            BLOCKS.register("distillation", () ->
                    new DistillationBlock(
                            BlockBehaviour.Properties.of()
                                    .destroyTime(3.0f)
                                    .explosionResistance(10.0f)
                                    .sound(SoundType.METAL)
                                    .requiresCorrectToolForDrops()
                    )
            );

    public static final DeferredBlock<Block> DUST =
            BLOCKS.register("dust", () ->
                    new Block(
                            BlockBehaviour.Properties.of()
                                    .explosionResistance(10.0f)
                                    .sound(SoundType.SAND)
                                    .speedFactor(0.75f)
                    )
            );



    public static final DeferredBlock<Block> CRUSHER_ITEM =
            registerBlockItem("crusher", CRUSHER);

    public static final DeferredBlock<Block> DISTILLATION_ITEM =
            registerBlockItem("distillation", DISTILLATION);

    public static final DeferredBlock<Block> ELECTRIC_FURNACE_ITEM =
            registerBlockItem("electric_furnace", ELECTRIC_FURNACE);

    public static final DeferredBlock<Block> GENERATOR_ITEM =
            registerBlockItem("generator", GENERATOR);

    public static final DeferredBlock<Block> BERYLLIUM_BLOCK_ITEM =
            registerBlockItem("beryllium_block", BERYLLIUM_BLOCK);

    public static final DeferredBlock<Block> BERYLLIUM_ORE_ITEM =
            registerBlockItem("beryllium_ore", BERYLLIUM_ORE);

    public static final DeferredBlock<Block> DEEPSLATE_BERYLLIUM_ORE_ITEM =
            registerBlockItem("deepslate_beryllium_ore", DEEPSLATE_BERYLLIUM_ORE);

    public static final DeferredBlock<Block> TUNGSTEN_BLOCK_ITEM =
            registerBlockItem("tungsten_block", TUNGSTEN_BLOCK);

    public static final DeferredBlock<Block> TUNGSTEN_ORE_ITEM =
            registerBlockItem("tungsten_ore", TUNGSTEN_ORE);

    public static final DeferredBlock<Block> DEEPSLATE_TUNGSTEN_ORE_ITEM =
            registerBlockItem("deepslate_tungsten_ore", DEEPSLATE_TUNGSTEN_ORE);

    public static final DeferredBlock<Block> TITAN_BLOCK_ITEM =
            registerBlockItem("titan_block", TITAN_BLOCK);

    public static final DeferredBlock<Block> TITAN_ORE_ITEM =
            registerBlockItem("titan_ore", TITAN_ORE);

    public static final DeferredBlock<Block> MONAZITE_SAND_ITEM =
            registerBlockItem("monazite_sand", MONAZITE_SAND);

    public static final DeferredBlock<Block> DUST_ITEM =
            registerBlockItem("dust", DUST);


    private static DeferredBlock<Block> registerBlockItem(
            String name,
            DeferredBlock<Block> block
    ) {

        ITEMS.register(name, () ->
                new BlockItem(
                        block.get(),
                        new Item.Properties()
                )
        );

        return block;
    }
}