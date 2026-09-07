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
         * GUI本体
         *
         * テクスチャがまだ存在しなくても
         * GUIそのものは表示できるようにする。
         */
        graphics.fill(
                x,
                y,
                x + imageWidth,
                y + imageHeight,
                0xFF202020
        );

        /*
         * 入力・出力エリア
         */
        graphics.fill(
                x + 45,
                y + 24,
                x + 82,
                y + 61,
                0xFF303030
        );

        graphics.fill(
                x + 106,
                y + 7,
                x + 143,
                y + 68,
                0xFF303030
        );

        /*
         * 進捗バー
         */
        int progress =
                menu.getProgress();

        int processTime =
                menu.getProcessTime();

        if (processTime > 0 && progress > 0) {

            int width =
                    progress * 24 / processTime;

            graphics.fill(
                    x + 82,
                    y + 32,
                    x + 82 + width,
                    y + 40,
                    0xFFAAAAAA
            );
        }

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
                    x + 155,
                    y + 84 - height,
                    x + 163,
                    y + 84,
                    0xFFFFAA00
            );
        }

        /*
         * スロットの枠
         */
        drawSlot(graphics, x + 53, y + 32);
        drawSlot(graphics, x + 113, y + 14);
        drawSlot(graphics, x + 113, y + 32);
        drawSlot(graphics, x + 113, y + 50);
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
                0xFF8A8A8A
        );

        graphics.fill(
                x + 1,
                y + 1,
                x + 17,
                y + 17,
                0xFF202020
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