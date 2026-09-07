package com.himataku.nmo.tab;

import com.himataku.nmo.NewMineOrder;
import com.himataku.nmo.customblock.AllBlock;
import com.himataku.nmo.customblock.AllItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NmoTab {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NewMineOrder.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> NMO_TAB =
            TABS.register("lung", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.nmo.lung"))
                    .icon(() -> Items.DIAMOND.getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(AllBlock.BERYLLIUM_BLOCK);
                        output.accept(AllBlock.BERYLLIUM_ORE);
                        output.accept(AllBlock.DEEPSLATE_BERYLLIUM_ORE);
                        output.accept(AllBlock.TUNGSTEN_BLOCK);
                        output.accept(AllBlock.TUNGSTEN_ORE);
                        output.accept(AllBlock.DEEPSLATE_TUNGSTEN_ORE);


                        output.accept(AllItem.BERYLLIUM_INGOT);
                        output.accept(AllItem.RAW_BERYLLIUM);
                        output.accept(AllItem.TUNGSTEN_INGOT);
                        output.accept(AllItem.RAW_TUNGSTEN);
                    })
                    .withSearchBar()
                    .build());
}