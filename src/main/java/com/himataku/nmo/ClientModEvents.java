package com.himataku.nmo;

import com.himataku.nmo.CrusherBlock.CrusherScreen;
import com.himataku.nmo.ElectricFurnace.ElectricFurnaceScreen;
import com.himataku.nmo.Generator.GeneratorScreen;
import com.himataku.nmo.customblock.AllFluid;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(
        modid = NewMineOrder.MODID,
        value = Dist.CLIENT
)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerScreens(
            RegisterMenuScreensEvent event
    ) {

        event.register(
                ModMenus.CRUSHER.get(),
                CrusherScreen::new
        );

        event.register(
                ModMenus.ELECTRIC_FURNACE.get(),
                ElectricFurnaceScreen::new
        );

        event.register(
                ModMenus.GENERATOR.get(),
                GeneratorScreen::new
        );
    }

    @SubscribeEvent
    public static void registerFluidClientExtensions(
            RegisterClientExtensionsEvent event
    ) {

        event.registerFluidType(
                new IClientFluidTypeExtensions() {

                    private static final ResourceLocation STILL =
                            ResourceLocation.fromNamespaceAndPath(
                                    NewMineOrder.MODID,
                                    "block/steam"
                            );

                    private static final ResourceLocation FLOWING =
                            ResourceLocation.fromNamespaceAndPath(
                                    NewMineOrder.MODID,
                                    "block/steam_flow"
                            );

                    @Override
                    public ResourceLocation getStillTexture() {
                        return STILL;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return FLOWING;
                    }

                    @Override
                    public int getTintColor() {
                        return 0xFFFFFFFF;
                    }
                },
                AllFluid.STEAM_TYPE.value()
        );
    }
}