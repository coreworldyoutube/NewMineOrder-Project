package com.himataku.nmo.customblock;

import com.himataku.nmo.NewMineOrder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllItem {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(NewMineOrder.MODID);

    public static final DeferredItem<Item> BERYLLIUM_INGOT =
            ITEMS.register("beryllium_ingot", () ->
                    new Item(
                            new Item.Properties()

                    )
            );
    public static final DeferredItem<Item> TUNGSTEN_INGOT =
            ITEMS.register("tungsten_ingot", () ->
                    new Item(
                            new Item.Properties()

                    )
            );
    public static final DeferredItem<Item> RAW_BERYLLIUM =
            ITEMS.register("raw_beryllium", () ->
                    new Item(
                            new Item.Properties()

                    )
            );
    public static final DeferredItem<Item> RAW_TUNGSTEN =
            ITEMS.register("raw_tungsten", () ->
                    new Item(
                            new Item.Properties()

                    )
            );
    
}
