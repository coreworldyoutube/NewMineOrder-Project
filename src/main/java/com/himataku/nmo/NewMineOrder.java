package com.himataku.nmo;

import com.himataku.nmo.customblock.AllBlock;
import com.himataku.nmo.customblock.AllItem;
import com.himataku.nmo.tab.NmoTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(NewMineOrder.MODID)
public class NewMineOrder {

    public static final String MODID = "nmo";
    public static final String MODNAME = "newmineorder";

    public NewMineOrder(IEventBus bus) {
        AllBlock.BLOCKS.register(bus);
        AllBlock.ITEMS.register(bus);
        AllItem.ITEMS.register(bus);
        NmoTab.TABS.register(bus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(bus);
    }
}