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
            );

    static {

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
    }
}