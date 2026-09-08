package com.himataku.nmo.customblock;

import com.himataku.nmo.NewMineOrder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllFluidItemBlock {

    /*
     * =========================================================
     * Block
     * =========================================================
     */

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(
                    Registries.BLOCK,
                    NewMineOrder.MODID
            );


    /*
     * =========================================================
     * Item
     * =========================================================
     */

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(
                    Registries.ITEM,
                    NewMineOrder.MODID
            );


    /*
     * =========================================================
     * Steam Block
     * =========================================================
     */

    public static final DeferredHolder<Block, LiquidBlock> STEAM_BLOCK =
            BLOCKS.register(
                    "steam",
                    () -> new LiquidBlock(
                            (FlowingFluid) AllFluid.STEAM.value(),
                            BlockBehaviour.Properties.of()
                                    .noCollission()
                                    .strength(100.0F)
                                    .noLootTable()
                                    .replaceable()
                                    .liquid()
                    )
            );


    /*
     * =========================================================
     * Hydrogen Block
     * =========================================================
     */

    public static final DeferredHolder<Block, LiquidBlock> HYDROGEN_BLOCK =
            BLOCKS.register(
                    "hydrogen",
                    () -> new LiquidBlock(
                            (FlowingFluid) AllFluid.HYDROGEN.value(),
                            BlockBehaviour.Properties.of()
                                    .noCollission()
                                    .strength(100.0F)
                                    .noLootTable()
                                    .replaceable()
                                    .liquid()
                    )
            );


    /*
     * =========================================================
     * Oxygen Block
     * =========================================================
     */

    public static final DeferredHolder<Block, LiquidBlock> OXYGEN_BLOCK =
            BLOCKS.register(
                    "oxygen",
                    () -> new LiquidBlock(
                            (FlowingFluid) AllFluid.OXYGEN.value(),
                            BlockBehaviour.Properties.of()
                                    .noCollission()
                                    .strength(100.0F)
                                    .noLootTable()
                                    .replaceable()
                                    .liquid()
                    )
            );


    /*
     * =========================================================
     * Molten Tungsten Block
     * =========================================================
     */

    public static final DeferredHolder<Block, LiquidBlock> MOLTEN_TUNGSTEN_BLOCK =
            BLOCKS.register(
                    "molten_tungsten",
                    () -> new LiquidBlock(
                            (FlowingFluid) AllFluid.MOLTEN_TUNGSTEN.value(),
                            BlockBehaviour.Properties.of()
                                    .noCollission()
                                    .strength(100.0F)
                                    .noLootTable()
                                    .replaceable()
                                    .liquid()
                    )
            );


    /*
     * =========================================================
     * Crude Oil Block
     * =========================================================
     */

    public static final DeferredHolder<Block, LiquidBlock> CRUDE_OIL_BLOCK =
            BLOCKS.register(
                    "crude_oil",
                    () -> new LiquidBlock(
                            (FlowingFluid) AllFluid.CRUDE_OIL.value(),
                            BlockBehaviour.Properties.of()
                                    .noCollission()
                                    .strength(100.0F)
                                    .noLootTable()
                                    .replaceable()
                                    .liquid()
                    )
            );


    /*
     * =========================================================
     * Naphtha Block
     * =========================================================
     */

    public static final DeferredHolder<Block, LiquidBlock> NAPHTHA_BLOCK =
            BLOCKS.register(
                    "naphtha",
                    () -> new LiquidBlock(
                            (FlowingFluid) AllFluid.NAPHTHA.value(),
                            BlockBehaviour.Properties.of()
                                    .noCollission()
                                    .strength(100.0F)
                                    .noLootTable()
                                    .replaceable()
                                    .liquid()
                    )
            );


    /*
     * =========================================================
     * Gasoline Block
     * =========================================================
     */

    public static final DeferredHolder<Block, LiquidBlock> GASOLINE_BLOCK =
            BLOCKS.register(
                    "gasoline",
                    () -> new LiquidBlock(
                            (FlowingFluid) AllFluid.GASOLINE.value(),
                            BlockBehaviour.Properties.of()
                                    .noCollission()
                                    .strength(100.0F)
                                    .noLootTable()
                                    .replaceable()
                                    .liquid()
                    )
            );


    /*
     * =========================================================
     * Steam Bucket
     * =========================================================
     */

    public static final DeferredHolder<Item, Item> STEAM_BUCKET =
            ITEMS.register(
                    "steam_bucket",
                    () -> new BucketItem(
                            AllFluid.STEAM.value(),
                            new Item.Properties()
                                    .craftRemainder(net.minecraft.world.item.Items.BUCKET)
                                    .stacksTo(1)
                    )
            );


    /*
     * =========================================================
     * Hydrogen Bucket
     * =========================================================
     */

    public static final DeferredHolder<Item, Item> HYDROGEN_BUCKET =
            ITEMS.register(
                    "hydrogen_bucket",
                    () -> new BucketItem(
                            AllFluid.HYDROGEN.value(),
                            new Item.Properties()
                                    .craftRemainder(net.minecraft.world.item.Items.BUCKET)
                                    .stacksTo(1)
                    )
            );


    /*
     * =========================================================
     * Oxygen Bucket
     * =========================================================
     */

    public static final DeferredHolder<Item, Item> OXYGEN_BUCKET =
            ITEMS.register(
                    "oxygen_bucket",
                    () -> new BucketItem(
                            AllFluid.OXYGEN.value(),
                            new Item.Properties()
                                    .craftRemainder(net.minecraft.world.item.Items.BUCKET)
                                    .stacksTo(1)
                    )
            );


    /*
     * =========================================================
     * Molten Tungsten Bucket
     * =========================================================
     */

    public static final DeferredHolder<Item, Item> MOLTEN_TUNGSTEN_BUCKET =
            ITEMS.register(
                    "molten_tungsten_bucket",
                    () -> new BucketItem(
                            AllFluid.MOLTEN_TUNGSTEN.value(),
                            new Item.Properties()
                                    .craftRemainder(net.minecraft.world.item.Items.BUCKET)
                                    .stacksTo(1)
                    )
            );


    /*
     * =========================================================
     * Crude Oil Bucket
     * =========================================================
     */

    public static final DeferredHolder<Item, Item> CRUDE_OIL_BUCKET =
            ITEMS.register(
                    "crude_oil_bucket",
                    () -> new BucketItem(
                            AllFluid.CRUDE_OIL.value(),
                            new Item.Properties()
                                    .craftRemainder(net.minecraft.world.item.Items.BUCKET)
                                    .stacksTo(1)
                    )
            );


    /*
     * =========================================================
     * Naphtha Bucket
     * =========================================================
     */

    public static final DeferredHolder<Item, Item> NAPHTHA_BUCKET =
            ITEMS.register(
                    "naphtha_bucket",
                    () -> new BucketItem(
                            AllFluid.NAPHTHA.value(),
                            new Item.Properties()
                                    .craftRemainder(net.minecraft.world.item.Items.BUCKET)
                                    .stacksTo(1)
                    )
            );


    /*
     * =========================================================
     * Gasoline Bucket
     * =========================================================
     */

    public static final DeferredHolder<Item, Item> GASOLINE_BUCKET =
            ITEMS.register(
                    "gasoline_bucket",
                    () -> new BucketItem(
                            AllFluid.GASOLINE.value(),
                            new Item.Properties()
                                    .craftRemainder(net.minecraft.world.item.Items.BUCKET)
                                    .stacksTo(1)
                    )
            );
}