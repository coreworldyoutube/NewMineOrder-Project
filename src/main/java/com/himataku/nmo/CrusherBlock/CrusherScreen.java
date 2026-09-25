package com.himataku.nmo.CrusherBlock;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CrusherScreen
        extends AbstractContainerScreen<CrusherMenu> {

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(
                    "nmo",
                    "textures/gui/testgui.png"
            );

    public CrusherScreen(
            CrusherMenu menu,
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
            GuiGraphics graphics,
            float partialTick,
            int mouseX,
            int mouseY
    ) {

        int x = leftPos;
        int y = topPos;

        /*
         * 背景
         */
        graphics.blit(
                BACKGROUND,
                x,
                y,
                0,
                1,
                176,
                166
        );
    }

    @Override
    protected void renderLabels(
            GuiGraphics graphics,
            int mouseX,
            int mouseY
    ) {

        graphics.drawString(
                this.font,
                this.title,
                8,
                6,
                0xFFFFFF,
                false
        );

        graphics.drawString(
                this.font,
                this.playerInventoryTitle,
                8,
                72,
                0xFFFFFF,
                false
        );

        graphics.drawString(
                this.font,
                "FE",
                151,
                91,
                0xFFFFFF,
                false
        );
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {

        renderBackground(
                graphics,
                mouseX,
                mouseY,
                partialTick
        );

        super.render(
                graphics,
                mouseX,
                mouseY,
                partialTick
        );

        renderTooltip(
                graphics,
                mouseX,
                mouseY
        );
    }
}
