package com.himataku.nmo.chemnmo;

import com.himataku.nmo.NewMineOrder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import static mekanism.common.ChemicalConstants.HYDROGEN;

public class AllFluid {

    /*
     * =========================================================
     * Registers
     * =========================================================
     */

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(
                    NeoForgeRegistries.Keys.FLUID_TYPES,
                    NewMineOrder.MODID
            );

    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(
                    Registries.FLUID,
                    NewMineOrder.MODID
            );

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(
                    Registries.BLOCK,
                    NewMineOrder.MODID
            );

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(
                    Registries.ITEM,
                    NewMineOrder.MODID
            );


    /*
     * =========================================================
     * Fluid Definitions
     * =========================================================
     *
     * ここにFluidを追加していく。
     *
     * 1つのFluid = 1つのFluidDefinition
     *
     * FluidType
     * Source Fluid
     * Flowing Fluid
     * Block
     * Bucket
     *
     * を1つのオブジェクトとして管理する。
     */


    public static final FluidDefinition STEAM =
            new FluidDefinition(
                    "steam",
                    FluidType.Properties.create()
                            .density(-100)
                            .temperature(373)
                            .viscosity(100)
                            .canPushEntity(false)
                            .canSwim(false)
                            .canDrown(false)
                            .canExtinguish(false)
                            .canConvertToSource(false)
            );

    public static final FluidDefinition CRUDE_OIL =
            new FluidDefinition(
                    "crude_oil",
                    FluidType.Properties.create()
                            .density(850)
                            .temperature(300)
                            .viscosity(500)
                            .canPushEntity(false)
                            .canSwim(false)
                            .canDrown(false)
                            .canExtinguish(false)
                            .canConvertToSource(false)
            );

    public static final FluidDefinition NAPHTHA =
            new FluidDefinition(
                    "naphtha",
                    FluidType.Properties.create()
                            .density(700)
                            .temperature(300)
                            .viscosity(200)
                            .canPushEntity(false)
                            .canSwim(false)
                            .canDrown(false)
                            .canExtinguish(false)
                            .canConvertToSource(false)
            );

    public static final FluidDefinition GASOLINE =
            new FluidDefinition(
                    "gasoline",
                    FluidType.Properties.create()
                            .density(740)
                            .temperature(300)
                            .viscosity(100)
                            .canPushEntity(false)
                            .canSwim(false)
                            .canDrown(false)
                            .canExtinguish(false)
                            .canConvertToSource(false)
            );


    public static final FluidDefinition DIRTY_WATER =
            new FluidDefinition(
                    "dirty_water",
                    FluidType.Properties.create()
                            .density(1000)
                            .temperature(300)
                            .viscosity(1200)
                            .canPushEntity(false)
                            .canSwim(false)
                            .canDrown(false)
                            .canExtinguish(false)
                            .canConvertToSource(false)
            );


    /*
     * =========================================================
     * Custom Example
     * =========================================================
     *
     * FluidDefinitionは標準設定を持つ。
     *
     * 必要なら以下のように個別カスタムできる。
     *
     * 例:
     *
     * public static final FluidDefinition EXAMPLE =
     *         new FluidDefinition(
     *                 "example",
     *                 FluidType.Properties.create()
     *                         .density(500)
     *                         .temperature(500)
     *                         .viscosity(100)
     *         )
     *         .block(properties ->
     *                 properties.strength(50.0F)
     *         )
     *         .bucket(properties ->
     *                 properties.stacksTo(1)
     *         );
     *
     *
     * =========================================================
     */


    /*
     * =========================================================
     * Definition List
     * =========================================================
     */

    private static final List<FluidDefinition> DEFINITIONS =
            new ArrayList<>();


    /*
     * =========================================================
     * Registration
     * =========================================================
     */

    static {

        register(STEAM);
        register(CRUDE_OIL);
        register(NAPHTHA);
        register(GASOLINE);
        register(DIRTY_WATER);

        for (FluidDefinition definition : DEFINITIONS) {
            definition.register();
        }
    }


    /*
     * =========================================================
     * Register Definition
     * =========================================================
     */

    private static void register(FluidDefinition definition) {
        DEFINITIONS.add(definition);
    }


    /*
     * =========================================================
     * FluidDefinition
     * =========================================================
     *
     * 「1つのFluid」を表すオブジェクト。
     *
     * ID
     * FluidType
     * Source
     * Flowing
     * Block
     * Bucket
     *
     * をまとめて管理する。
     */

    public static class FluidDefinition {

        private final String id;

        private final FluidType.Properties fluidTypeProperties;

        private UnaryOperator<BlockBehaviour.Properties> blockCustomizer =
                properties -> properties;

        private UnaryOperator<Item.Properties> bucketCustomizer =
                properties -> properties;


        /*
         * -----------------------------------------------------
         * Registered Objects
         * -----------------------------------------------------
         */

        private DeferredHolder<FluidType, FluidType> type;

        private DeferredHolder<Fluid, BaseFlowingFluid.Source> source;

        private DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowing;

        private DeferredHolder<Block, LiquidBlock> block;

        private DeferredHolder<Item, Item> bucket;


        /*
         * -----------------------------------------------------
         * Constructor
         * -----------------------------------------------------
         */

        public FluidDefinition(
                String id,
                FluidType.Properties fluidTypeProperties
        ) {
            this.id = id;
            this.fluidTypeProperties = fluidTypeProperties;
        }


        /*
         * =====================================================
         * Customization
         * =====================================================
         */

        public FluidDefinition block(
                UnaryOperator<BlockBehaviour.Properties> customizer
        ) {
            this.blockCustomizer = customizer;
            return this;
        }


        public FluidDefinition bucket(
                UnaryOperator<Item.Properties> customizer
        ) {
            this.bucketCustomizer = customizer;
            return this;
        }


        /*
         * =====================================================
         * Registration
         * =====================================================
         */

        private void register() {

            /*
             * -------------------------------------------------
             * FluidType
             * -------------------------------------------------
             */

            type =
                    FLUID_TYPES.register(
                            id,
                            () -> new FluidType(
                                    fluidTypeProperties
                            )
                    );


            /*
             * -------------------------------------------------
             * Fluid Properties
             * -------------------------------------------------
             */

            BaseFlowingFluid.Properties fluidProperties =
                    new BaseFlowingFluid.Properties(
                            type,
                            () -> source.value(),
                            () -> flowing.value()
                    )
                            .block(() -> block.value())
                            .bucket(() -> bucket.value());


            /*
             * -------------------------------------------------
             * Source Fluid
             * -------------------------------------------------
             */

            source =
                    FLUIDS.register(
                            id,
                            () -> new BaseFlowingFluid.Source(
                                    fluidProperties
                            )
                    );


            /*
             * -------------------------------------------------
             * Flowing Fluid
             * -------------------------------------------------
             */

            flowing =
                    FLUIDS.register(
                            "flowing_" + id,
                            () -> new BaseFlowingFluid.Flowing(
                                    fluidProperties
                            )
                    );


            /*
             * -------------------------------------------------
             * Block
             * -------------------------------------------------
             */

            block =
                    BLOCKS.register(
                            id,
                            () -> new LiquidBlock(
                                    (FlowingFluid) source.value(),
                                    blockCustomizer.apply(
                                            BlockBehaviour.Properties.of()
                                                    .noCollission()
                                                    .strength(100.0F)
                                                    .noLootTable()
                                                    .replaceable()
                                                    .liquid()
                                    )
                            )
                    );


            /*
             * -------------------------------------------------
             * Bucket
             * -------------------------------------------------
             */

            bucket =
                    ITEMS.register(
                            id + "_bucket",
                            () -> new BucketItem(
                                    source.value(),
                                    bucketCustomizer.apply(
                                            new Item.Properties()
                                                    .craftRemainder(
                                                            net.minecraft.world.item.Items.BUCKET
                                                    )
                                                    .stacksTo(1)
                                    )
                            )
                    );
        }


        /*
         * =====================================================
         * Getters
         * =====================================================
         */

        public DeferredHolder<FluidType, FluidType> type() {
            return type;
        }


        public DeferredHolder<Fluid, BaseFlowingFluid.Source> source() {
            return source;
        }


        public DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowing() {
            return flowing;
        }


        /*
         * 実際のSource Fluidを取得する。
         *
         * 例:
         * AllFluid.STEAM.get()
         */
        public Fluid get() {
            return source.value();
        }


        public DeferredHolder<Block, LiquidBlock> block() {
            return block;
        }


        public DeferredHolder<Item, Item> bucket() {
            return bucket;
        }


        public String id() {
            return id;
        }
    }
}
