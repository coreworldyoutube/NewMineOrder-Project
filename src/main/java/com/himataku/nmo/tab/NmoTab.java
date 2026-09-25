package com.himataku.nmo.tab;

import com.himataku.nmo.NewMineOrder;
import com.himataku.nmo.chemnmo.AllBlock;
import com.himataku.nmo.chemnmo.AllItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NmoTab {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(
                    Registries.CREATIVE_MODE_TAB,
                    NewMineOrder.MODID
            );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> NMO_TAB =
            TABS.register("lang", () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.nmo.lung"))
                            .icon(() -> Items.DIAMOND.getDefaultInstance())
                            .displayItems((parameters, output) -> {

                                // Materials
                                output.accept(AllItem.OBSIDIAN_DUST);
                                output.accept(AllItem.CRUSHED_ENDSTONE);
                                output.accept(AllItem.CRUSHED_RAW_TUNGSTEN);
                                output.accept(AllItem.CRUSHED_RAW_BERYLLIUM);
                                output.accept(AllItem.CRUSHED_RAW_GOLD);
                                output.accept(AllItem.CRUSHED_RAW_COPPER);
                                output.accept(AllItem.CRUSHED_RAW_IRON);

                                // Batteries
                                output.accept(AllItem.TERTIARY_BATTERY);
                                output.accept(AllItem.DISPOSABLE_BATTERY);
                                output.accept(AllItem.RECHARGEABLE_BATTERY);

                                // Wires
                                output.accept(AllItem.IRON_WIRE);
                                output.accept(AllItem.GOLD_WIRE);
                                output.accept(AllItem.COPPER_WIRE);

                                // Components
                                output.accept(AllItem.MINI_PISTON);
                                output.accept(AllItem.MOTOR);
                                output.accept(AllItem.HANDCRAFT_IC);

                                // Raw Materials
                                output.accept(AllItem.RAW_TITAN);
                                output.accept(AllItem.RAW_TUNGSTEN);
                                output.accept(AllItem.RAW_BERYLLIUM);

                                // Machines
                                output.accept(AllBlock.CRUSHER_ITEM);
                                output.accept(AllBlock.GENERATOR_ITEM);
                                output.accept(AllBlock.DISTILLATION_ITEM);
                                output.accept(AllBlock.ELECTRIC_FURNACE_ITEM);
                                output.accept(AllBlock.WASHINGBLOCK_ITEM);

                                // Ores
                                output.accept(AllBlock.BERYLLIUM_ORE_ITEM);
                                output.accept(AllBlock.DEEPSLATE_BERYLLIUM_ORE_ITEM);
                                output.accept(AllBlock.TUNGSTEN_ORE_ITEM);
                                output.accept(AllBlock.TITAN_ORE_ITEM);

                                // Materials / Blocks
                                output.accept(AllBlock.DUST_ITEM);
                                output.accept(AllBlock.MONAZITE_SAND_ITEM);
                                output.accept(AllBlock.BLACK_SAND_ITEM);

                            })
                            .withSearchBar()
                            .build());
}