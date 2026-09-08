package com.himataku.nmo;

import com.himataku.nmo.CrusherBlock.ModRecipes;
import com.himataku.nmo.customblock.AllFluid;
import com.himataku.nmo.customblock.AllBlock;
import com.himataku.nmo.customblock.AllFluidItemBlock;
import com.himataku.nmo.customblock.AllItem;
import com.himataku.nmo.tab.NmoTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;



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

        AllFluidItemBlock.BLOCKS.register(bus);
        AllFluidItemBlock.ITEMS.register(bus);

        NmoTab.TABS.register(bus);

        ModBlockEntities.BLOCK_ENTITY_TYPES.register(bus);
        ModMenus.MENUS.register(bus);
        ModRecipes.SERIALIZERS.register(bus);

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
    }
}