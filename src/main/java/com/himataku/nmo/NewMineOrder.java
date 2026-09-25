package com.himataku.nmo;

import com.himataku.nmo.CrusherBlock.ModRecipes;
import com.himataku.nmo.chemnmo.AllFluid;
import com.himataku.nmo.chemnmo.AllBlock;
import com.himataku.nmo.chemnmo.AllItem;
import com.himataku.nmo.tab.NmoTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

//import static com.smashingmods.chemlib.registry.ItemRegistry.REGISTRY_COMPOUNDS;
//import static com.smashingmods.chemlib.registry.ItemRegistry.REGISTRY_INGOTS;


@Mod(NewMineOrder.MODID)
public class NewMineOrder {

    public static final String MODID = "nmo";
    public static final String MODNAME = "newmineorder";

    public NewMineOrder(IEventBus bus) {

        AllBlock.BLOCKS.register(bus);
        AllBlock.ITEMS.register(bus);
        AllItem.ITEMS.register(bus);

        AllFluid.FLUID_TYPES.register(bus);
        AllFluid.FLUIDS.register(bus);

        AllFluid.BLOCKS.register(bus);
        AllFluid.ITEMS.register(bus);

        NmoTab.TABS.register(bus);

        ModBlockEntities.BLOCK_ENTITY_TYPES.register(bus);
        ModMenus.MENUS.register(bus);
        ModRecipes.SERIALIZERS.register(bus);
        ModRecipes.RECIPE_TYPES.register(bus);

//        REGISTRY_COMPOUNDS.register(bus);
//        REGISTRY_INGOTS.register(bus);

        bus.addListener(
                NewMineOrder::registerCapabilities
        );
    }

    private static void registerCapabilities(
            RegisterCapabilitiesEvent event
    ) {

        // =========================
        // Crusher
        // =========================

        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.CRUSHER.get(),
                (blockEntity, side) ->
                        blockEntity.getEnergyStorage()
        );

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.CRUSHER.get(),
                (blockEntity, side) ->
                        blockEntity.getItemHandler()
        );

        // =========================
        // Electric Furnace
        // =========================

        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.ELECTRIC_FURNACE.get(),
                (blockEntity, side) ->
                        blockEntity.getEnergyStorage()
        );

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.ELECTRIC_FURNACE.get(),
                (blockEntity, side) ->
                        blockEntity.getExternalItemHandler()
        );

        // =========================
        // Generator
        // =========================

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.GENERATOR.get(),
                (blockEntity, side) ->
                        blockEntity.getFluidHandler()
        );

        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.GENERATOR.get(),
                (blockEntity, side) ->
                        blockEntity.getEnergyStorage()
        );
        // =========================
        // Distillation
        // =========================

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.DISTILLATION.get(),
                (blockEntity, side) ->
                        blockEntity.getFluidHandler()
        );

        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.DISTILLATION.get(),
                (blockEntity, side) ->
                        blockEntity.getEnergyStorage()
        );
        // =========================
        // Washing
        // =========================

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.WASHING.get(),
                (blockEntity, side) ->
                        blockEntity.getItemHandler()
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.WASHING.get(),
                (blockEntity, side) ->
                        blockEntity.getFluidHandler()
        );

        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.WASHING.get(),
                (blockEntity, side) ->
                        blockEntity.getEnergyStorage()
        );
    }
}