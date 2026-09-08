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

    public static final DeferredItem<Item> RAW_TITAN =
            ITEMS.register("raw_titan", () ->
                    new Item(
                            new Item.Properties()

                    )
            );
    public static final DeferredItem<Item> BOTTLE_HELIUM_III =
            ITEMS.register("bottle_helium_iii", () ->
                    new Item(
                            new Item.Properties()

                    )
            );
    public static final DeferredItem<Item> BOTTLE_HELIUM =
            ITEMS.register("bottle_helium", () ->
                    new Item(
                            new Item.Properties()

                    )
            );
    public static final DeferredItem<Item> MONAZITE =
            ITEMS.register("monazite", () ->
                    new Item(
                            new Item.Properties()

                    )
            );

    public static final DeferredItem<Item> MOTOR =
            ITEMS.register("motor", () ->
                    new Item(
                            new Item.Properties()

                    )
            );

    public static final DeferredItem<Item> COPPER_WIRE =
            ITEMS.register("copper_wire", () ->
                    new Item(
                            new Item.Properties()

                    )
            );

    public static final DeferredItem<Item> GOLD_WIRE =
            ITEMS.register("gold_wire", () ->
                    new Item(
                            new Item.Properties()

                    )
            );

    public static final DeferredItem<Item> IRON_WIRE =
            ITEMS.register("iron_wire", () ->
                    new Item(
                            new Item.Properties()

                    )
            );

    public static final DeferredItem<Item> RECHARGEABLE_BATTERY =
            ITEMS.register("rechargeable_battery", () ->
                    new Item(
                            new Item.Properties()

                    )
            );

    public static final DeferredItem<Item> DISPOSABLE_BATTERY =
            ITEMS.register("disposable_battery", () ->
                    new Item(
                            new Item.Properties()

                    )
            );

    public static final DeferredItem<Item> TERTIARY_BATTERY =
            ITEMS.register("tertiary_battery", () ->
                    new Item(
                            new Item.Properties()

                    )
            );
    
}
