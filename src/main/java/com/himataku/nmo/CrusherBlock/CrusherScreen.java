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
                    "textures/gui/crusher.png"
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
         * 仮背景
         *
         * 後で
         *
         * assets/nmo/textures/gui/crusher.png
         *
         * を用意すれば、この部分を
         * blit()に変更できる。
         */

        graphics.fill(
                x,
                y,
                x + imageWidth,
                y + imageHeight,
                0xFF201020
        );

        /*
         * ピンクの外枠
         */
        graphics.fill(
                x,
                y,
                x + imageWidth,
                y + 2,
                0xFFFF00FF
        );

        graphics.fill(
                x,
                y + imageHeight - 2,
                x + imageWidth,
                y + imageHeight,
                0xFFFF00FF
        );

        graphics.fill(
                x,
                y,
                x + 2,
                y + imageHeight,
                0xFFFF00FF
        );

        graphics.fill(
                x + imageWidth - 2,
                y,
                x + imageWidth,
                y + imageHeight,
                0xFFFF00FF
        );

        /*
         * 入力スロット周辺
         */
        graphics.fill(
                x + 52,
                y + 31,
                x + 71,
                y + 50,
                0xFF302030
        );

        /*
         * 出力スロット周辺
         */
        graphics.fill(
                x + 112,
                y + 13,
                x + 131,
                y + 32,
                0xFF302030
        );

        graphics.fill(
                x + 112,
                y + 31,
                x + 131,
                y + 50,
                0xFF302030
        );

        graphics.fill(
                x + 112,
                y + 49,
                x + 131,
                y + 68,
                0xFF302030
        );

        /*
         * 進捗バー背景
         */
        graphics.fill(
                x + 76,
                y + 34,
                x + 106,
                y + 42,
                0xFF301530
        );

        /*
         * 進捗バー
         */
        int progress =
                menu.getProgress();

        int processTime =
                menu.getProcessTime();

        if (
                processTime > 0
                        && progress > 0
        ) {

            int width =
                    progress * 30 / processTime;

            graphics.fill(
                    x + 76,
                    y + 34,
                    x + 76 + width,
                    y + 42,
                    0xFFFF00FF
            );
        }

        /*
         * FEバー背景
         */
        graphics.fill(
                x + 154,
                y + 30,
                x + 162,
                y + 84,
                0xFF301530
        );

        /*
         * FEバー
         */
        int energy =
                menu.getEnergy();

        int maxEnergy =
                menu.getMaxEnergy();

        if (maxEnergy > 0) {

            int height =
                    energy * 50 / maxEnergy;

            graphics.fill(
                    x + 154,
                    y + 84 - height,
                    x + 162,
                    y + 84,
                    0xFFFF00FF
            );
        }

        /*
         * スロット枠
         */
        drawSlot(
                graphics,
                x + 53,
                y + 32
        );

        drawSlot(
                graphics,
                x + 113,
                y + 14
        );

        drawSlot(
                graphics,
                x + 113,
                y + 32
        );

        drawSlot(
                graphics,
                x + 113,
                y + 50
        );
    }

    private void drawSlot(
            GuiGraphics graphics,
            int x,
            int y
    ) {

        graphics.fill(
                x,
                y,
                x + 18,
                y + 18,
                0xFFFF00FF
        );

        graphics.fill(
                x + 1,
                y + 1,
                x + 17,
                y + 17,
                0xFF201020
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