package com.himataku.nmo;

import com.himataku.nmo.CrusherBlock.CrusherMenu;

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
}