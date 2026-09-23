package com.himataku.nmo.Washing;

import com.himataku.nmo.NewMineOrder;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class WashingScreen
        extends AbstractContainerScreen<WashingMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    NewMineOrder.MODID,
                    "textures/gui/washing.png"
            );

    public WashingScreen(
            WashingMenu menu,
            Inventory inventory,
            Component title
    ) {

        super(
                menu,
                inventory,
                title
        );

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(
            GuiGraphics guiGraphics,
            float partialTick,
            int mouseX,
            int mouseY
    ) {

        int x =
                (width - imageWidth) / 2;

        int y =
                (height - imageHeight) / 2;

        guiGraphics.blit(
                TEXTURE,
                x,
                y,
                0,
                0,
                imageWidth,
                imageHeight
        );

        /*
         * ========================================================
         * Progress
         * ========================================================
         */

        int processTime =
                menu.getProcessTime();

        if (processTime > 0) {

            int progress =
                    menu.getProgress();

            int width =
                    progress * 24 / processTime;

            guiGraphics.blit(
                    TEXTURE,
                    x + 79,
                    y + 34,
                    176,
                    0,
                    width,
                    16
            );
        }

        /*
         * ========================================================
         * Energy
         * ========================================================
         */

        int maxEnergy =
                menu.getMaxEnergy();

        if (maxEnergy > 0) {

            int energy =
                    menu.getEnergy();

            int height =
                    energy * 52 / maxEnergy;

            if (height > 0) {

                guiGraphics.blit(
                        TEXTURE,
                        x + 7,
                        y + 17 + (52 - height),
                        176,
                        16 + (52 - height),
                        10,
                        height
                );
            }
        }
    }

    @Override
    public void render(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {

        renderBackground(
                guiGraphics,
                mouseX,
                mouseY,
                partialTick
        );

        super.render(
                guiGraphics,
                mouseX,
                mouseY,
                partialTick
        );

        renderTooltip(
                guiGraphics,
                mouseX,
                mouseY
        );
    }
}