package com.himataku.nmo.customblock;

import com.himataku.nmo.NewMineOrder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class AllFluid {

    /*
     * =========================================================
     * FluidType
     * =========================================================
     */

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(
                    NeoForgeRegistries.Keys.FLUID_TYPES,
                    NewMineOrder.MODID
            );


    /*
     * =========================================================
     * Fluid
     * =========================================================
     */

    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(
                    Registries.FLUID,
                    NewMineOrder.MODID
            );


    /*
     * =========================================================
     * Steam
     * =========================================================
     */

    public static final DeferredHolder<FluidType, FluidType> STEAM_TYPE =
            DeferredHolder.create(
                    NeoForgeRegistries.Keys.FLUID_TYPES,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "steam"
                    )
            );

    public static final DeferredHolder<Fluid, Fluid> STEAM =
            DeferredHolder.create(
                    Registries.FLUID,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "steam"
                    )
            );

    public static final DeferredHolder<Fluid, Fluid> FLOWING_STEAM =
            DeferredHolder.create(
                    Registries.FLUID,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "flowing_steam"
                    )
            );

    private static final BaseFlowingFluid.Properties STEAM_PROPERTIES =
            new BaseFlowingFluid.Properties(
                    STEAM_TYPE,
                    STEAM::value,
                    FLOWING_STEAM::value
            )
                    .block(() -> AllFluidItemBlock.STEAM_BLOCK.value())
                    .bucket(() -> AllFluidItemBlock.STEAM_BUCKET.value());


    /*
     * =========================================================
     * Hydrogen
     * =========================================================
     */

    public static final DeferredHolder<FluidType, FluidType> HYDROGEN_TYPE =
            DeferredHolder.create(
                    NeoForgeRegistries.Keys.FLUID_TYPES,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "hydrogen"
                    )
            );

    public static final DeferredHolder<Fluid, Fluid> HYDROGEN =
            DeferredHolder.create(
                    Registries.FLUID,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "hydrogen"
                    )
            );

    public static final DeferredHolder<Fluid, Fluid> FLOWING_HYDROGEN =
            DeferredHolder.create(
                    Registries.FLUID,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "flowing_hydrogen"
                    )
            );

    private static final BaseFlowingFluid.Properties HYDROGEN_PROPERTIES =
            new BaseFlowingFluid.Properties(
                    HYDROGEN_TYPE,
                    HYDROGEN::value,
                    FLOWING_HYDROGEN::value
            )
                    .block(() -> AllFluidItemBlock.HYDROGEN_BLOCK.value())
                    .bucket(() -> AllFluidItemBlock.HYDROGEN_BUCKET.value());


    /*
     * =========================================================
     * Oxygen
     * =========================================================
     */

    public static final DeferredHolder<FluidType, FluidType> OXYGEN_TYPE =
            DeferredHolder.create(
                    NeoForgeRegistries.Keys.FLUID_TYPES,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "oxygen"
                    )
            );

    public static final DeferredHolder<Fluid, Fluid> OXYGEN =
            DeferredHolder.create(
                    Registries.FLUID,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "oxygen"
                    )
            );

    public static final DeferredHolder<Fluid, Fluid> FLOWING_OXYGEN =
            DeferredHolder.create(
                    Registries.FLUID,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "flowing_oxygen"
                    )
            );

    private static final BaseFlowingFluid.Properties OXYGEN_PROPERTIES =
            new BaseFlowingFluid.Properties(
                    OXYGEN_TYPE,
                    OXYGEN::value,
                    FLOWING_OXYGEN::value
            )
                    .block(() -> AllFluidItemBlock.OXYGEN_BLOCK.value())
                    .bucket(() -> AllFluidItemBlock.OXYGEN_BUCKET.value());


    /*
     * =========================================================
     * Molten Tungsten
     * =========================================================
     */

    public static final DeferredHolder<FluidType, FluidType> MOLTEN_TUNGSTEN_TYPE =
            DeferredHolder.create(
                    NeoForgeRegistries.Keys.FLUID_TYPES,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "molten_tungsten"
                    )
            );

    public static final DeferredHolder<Fluid, Fluid> MOLTEN_TUNGSTEN =
            DeferredHolder.create(
                    Registries.FLUID,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "molten_tungsten"
                    )
            );

    public static final DeferredHolder<Fluid, Fluid> FLOWING_MOLTEN_TUNGSTEN =
            DeferredHolder.create(
                    Registries.FLUID,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "flowing_molten_tungsten"
                    )
            );

    private static final BaseFlowingFluid.Properties MOLTEN_TUNGSTEN_PROPERTIES =
            new BaseFlowingFluid.Properties(
                    MOLTEN_TUNGSTEN_TYPE,
                    MOLTEN_TUNGSTEN::value,
                    FLOWING_MOLTEN_TUNGSTEN::value
            )
                    .block(() -> AllFluidItemBlock.MOLTEN_TUNGSTEN_BLOCK.value())
                    .bucket(() -> AllFluidItemBlock.MOLTEN_TUNGSTEN_BUCKET.value());

    /*
     * =========================================================
     * Crude Oil
     * =========================================================
     */

    public static final DeferredHolder<FluidType, FluidType> CRUDE_OIL_TYPE =
            DeferredHolder.create(
                    NeoForgeRegistries.Keys.FLUID_TYPES,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "crude_oil"
                    )
            );

    public static final DeferredHolder<Fluid, Fluid> CRUDE_OIL =
            DeferredHolder.create(
                    Registries.FLUID,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "crude_oil"
                    )
            );

    public static final DeferredHolder<Fluid, Fluid> FLOWING_CRUDE_OIL =
            DeferredHolder.create(
                    Registries.FLUID,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "flowing_crude_oil"
                    )
            );

    private static final BaseFlowingFluid.Properties CRUDE_OIL_PROPERTIES =
            new BaseFlowingFluid.Properties(
                    CRUDE_OIL_TYPE,
                    CRUDE_OIL::value,
                    FLOWING_CRUDE_OIL::value
            )
                    .block(() -> AllFluidItemBlock.CRUDE_OIL_BLOCK.value())
                    .bucket(() -> AllFluidItemBlock.CRUDE_OIL_BUCKET.value());


    /*
     * =========================================================
     * Naphtha
     * =========================================================
     */

    public static final DeferredHolder<FluidType, FluidType> NAPHTHA_TYPE =
            DeferredHolder.create(
                    NeoForgeRegistries.Keys.FLUID_TYPES,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "naphtha"
                    )
            );

    public static final DeferredHolder<Fluid, Fluid> NAPHTHA =
            DeferredHolder.create(
                    Registries.FLUID,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "naphtha"
                    )
            );

    public static final DeferredHolder<Fluid, Fluid> FLOWING_NAPHTHA =
            DeferredHolder.create(
                    Registries.FLUID,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "flowing_naphtha"
                    )
            );

    private static final BaseFlowingFluid.Properties NAPHTHA_PROPERTIES =
            new BaseFlowingFluid.Properties(
                    NAPHTHA_TYPE,
                    NAPHTHA::value,
                    FLOWING_NAPHTHA::value
            )
                    .block(() -> AllFluidItemBlock.NAPHTHA_BLOCK.value())
                    .bucket(() -> AllFluidItemBlock.NAPHTHA_BUCKET.value());


    /*
     * =========================================================
     * Gasoline
     * =========================================================
     */

    public static final DeferredHolder<FluidType, FluidType> GASOLINE_TYPE =
            DeferredHolder.create(
                    NeoForgeRegistries.Keys.FLUID_TYPES,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "gasoline"
                    )
            );

    public static final DeferredHolder<Fluid, Fluid> GASOLINE =
            DeferredHolder.create(
                    Registries.FLUID,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "gasoline"
                    )
            );

    public static final DeferredHolder<Fluid, Fluid> FLOWING_GASOLINE =
            DeferredHolder.create(
                    Registries.FLUID,
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "flowing_gasoline"
                    )
            );

    private static final BaseFlowingFluid.Properties GASOLINE_PROPERTIES =
            new BaseFlowingFluid.Properties(
                    GASOLINE_TYPE,
                    GASOLINE::value,
                    FLOWING_GASOLINE::value
            )
                    .block(() -> AllFluidItemBlock.GASOLINE_BLOCK.value())
                    .bucket(() -> AllFluidItemBlock.GASOLINE_BUCKET.value());


    /*
     * =========================================================
     * Registration
     * =========================================================
     */

    static {

        /*
         * -----------------------------------------------------
         * Steam
         * -----------------------------------------------------
         */

        FLUID_TYPES.register(
                "steam",
                () -> new FluidType(
                        FluidType.Properties.create()
                                .density(-100)
                                .temperature(373)
                                .viscosity(100)
                                .canPushEntity(false)
                                .canSwim(false)
                                .canDrown(false)
                                .canExtinguish(false)
                                .canConvertToSource(false)
                )
        );

        FLUIDS.register(
                "steam",
                () -> new BaseFlowingFluid.Source(
                        STEAM_PROPERTIES
                )
        );

        FLUIDS.register(
                "flowing_steam",
                () -> new BaseFlowingFluid.Flowing(
                        STEAM_PROPERTIES
                )
        );


        /*
         * -----------------------------------------------------
         * Hydrogen
         * -----------------------------------------------------
         */

        FLUID_TYPES.register(
                "hydrogen",
                () -> new FluidType(
                        FluidType.Properties.create()
                                .density(-80)
                                .temperature(20)
                                .viscosity(20)
                                .canPushEntity(false)
                                .canSwim(false)
                                .canDrown(false)
                                .canExtinguish(false)
                                .canConvertToSource(false)
                )
        );

        FLUIDS.register(
                "hydrogen",
                () -> new BaseFlowingFluid.Source(
                        HYDROGEN_PROPERTIES
                )
        );

        FLUIDS.register(
                "flowing_hydrogen",
                () -> new BaseFlowingFluid.Flowing(
                        HYDROGEN_PROPERTIES
                )
        );


        /*
         * -----------------------------------------------------
         * Oxygen
         * -----------------------------------------------------
         */

        FLUID_TYPES.register(
                "oxygen",
                () -> new FluidType(
                        FluidType.Properties.create()
                                .density(-60)
                                .temperature(90)
                                .viscosity(25)
                                .canPushEntity(false)
                                .canSwim(false)
                                .canDrown(false)
                                .canExtinguish(false)
                                .canConvertToSource(false)
                )
        );

        FLUIDS.register(
                "oxygen",
                () -> new BaseFlowingFluid.Source(
                        OXYGEN_PROPERTIES
                )
        );

        FLUIDS.register(
                "flowing_oxygen",
                () -> new BaseFlowingFluid.Flowing(
                        OXYGEN_PROPERTIES
                )
        );


        /*
         * -----------------------------------------------------
         * Molten Tungsten
         * -----------------------------------------------------
         */

        FLUID_TYPES.register(
                "molten_tungsten",
                () -> new FluidType(
                        FluidType.Properties.create()
                                .density(19300)
                                .temperature(3695)
                                .viscosity(10000)
                                .canPushEntity(false)
                                .canSwim(false)
                                .canDrown(false)
                                .canExtinguish(false)
                                .canConvertToSource(false)
                )
        );

        FLUIDS.register(
                "molten_tungsten",
                () -> new BaseFlowingFluid.Source(
                        MOLTEN_TUNGSTEN_PROPERTIES
                )
        );

        FLUIDS.register(
                "flowing_molten_tungsten",
                () -> new BaseFlowingFluid.Flowing(
                        MOLTEN_TUNGSTEN_PROPERTIES
                )
        );

        /*
         * -----------------------------------------------------
         * Crude Oil
         * -----------------------------------------------------
         */

        FLUID_TYPES.register(
                "crude_oil",
                () -> new FluidType(
                        FluidType.Properties.create()
                                .density(850)
                                .temperature(300)
                                .viscosity(500)
                                .canPushEntity(false)
                                .canSwim(false)
                                .canDrown(false)
                                .canExtinguish(false)
                                .canConvertToSource(false)
                )
        );

        FLUIDS.register(
                "crude_oil",
                () -> new BaseFlowingFluid.Source(
                        CRUDE_OIL_PROPERTIES
                )
        );

        FLUIDS.register(
                "flowing_crude_oil",
                () -> new BaseFlowingFluid.Flowing(
                        CRUDE_OIL_PROPERTIES
                )
        );


        /*
         * -----------------------------------------------------
         * Naphtha
         * -----------------------------------------------------
         */

        FLUID_TYPES.register(
                "naphtha",
                () -> new FluidType(
                        FluidType.Properties.create()
                                .density(700)
                                .temperature(300)
                                .viscosity(200)
                                .canPushEntity(false)
                                .canSwim(false)
                                .canDrown(false)
                                .canExtinguish(false)
                                .canConvertToSource(false)
                )
        );

        FLUIDS.register(
                "naphtha",
                () -> new BaseFlowingFluid.Source(
                        NAPHTHA_PROPERTIES
                )
        );

        FLUIDS.register(
                "flowing_naphtha",
                () -> new BaseFlowingFluid.Flowing(
                        NAPHTHA_PROPERTIES
                )
        );


        /*
         * -----------------------------------------------------
         * Gasoline
         * -----------------------------------------------------
         */

        FLUID_TYPES.register(
                "gasoline",
                () -> new FluidType(
                        FluidType.Properties.create()
                                .density(740)
                                .temperature(300)
                                .viscosity(100)
                                .canPushEntity(false)
                                .canSwim(false)
                                .canDrown(false)
                                .canExtinguish(false)
                                .canConvertToSource(false)
                )
        );

        FLUIDS.register(
                "gasoline",
                () -> new BaseFlowingFluid.Source(
                        GASOLINE_PROPERTIES
                )
        );

        FLUIDS.register(
                "flowing_gasoline",
                () -> new BaseFlowingFluid.Flowing(
                        GASOLINE_PROPERTIES
                )
        );
    }
}