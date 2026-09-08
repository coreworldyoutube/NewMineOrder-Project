package com.himataku.nmo;

import com.himataku.nmo.CrusherBlock.CrusherMenu;
import com.himataku.nmo.ElectricFurnace.ElectricFurnaceMenu;
import com.himataku.nmo.Generator.GeneratorMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(
                    Registries.MENU,
                    NewMineOrder.MODID
            );

    public static final DeferredHolder<
            MenuType<?>,
            MenuType<CrusherMenu>
            > CRUSHER =
            MENUS.register(
                    "crusher",
                    () -> IMenuTypeExtension.create(
                            CrusherMenu::new
                    )
            );

    public static final DeferredHolder<
            MenuType<?>,
            MenuType<ElectricFurnaceMenu>
            > ELECTRIC_FURNACE =
            MENUS.register(
                    "electric_furnace",
                    () -> IMenuTypeExtension.create(
                            ElectricFurnaceMenu::new
                    )
            );

    public static final DeferredHolder<
            MenuType<?>,
            MenuType<GeneratorMenu>
            > GENERATOR =
            MENUS.register(
                    "generator",
                    () -> IMenuTypeExtension.create(
                            GeneratorMenu::new
                    )
            );
}