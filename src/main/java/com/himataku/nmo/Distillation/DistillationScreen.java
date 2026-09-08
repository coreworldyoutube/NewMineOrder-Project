package com.himataku.nmo.Distillation;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DistillationScreen
        extends AbstractContainerScreen<DistillationMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    "nmo",
                    "textures/gui/distillation.png"
            );

    public DistillationScreen(
            DistillationMenu menu,
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

        int x =
                (this.width - this.imageWidth) / 2;

        int y =
                (this.height - this.imageHeight) / 2;

        /*
         * ==================================================
         * GUI背景
         * ==================================================
         */

        graphics.blit(
                TEXTURE,
                x,
                y,
                0,
                0,
                this.imageWidth,
                this.imageHeight
        );

        /*
         * ==================================================
         * Progress
         * ==================================================
         */

        int progress =
                menu.getProgress();

        int maxProgress =
                menu.getMaxProgress();

        if (maxProgress > 0
                && progress > 0) {

            int progressWidth =
                    (int) (
                            24.0F
                                    * progress
                                    / maxProgress
                    );

            if (progressWidth > 0) {

                graphics.blit(
                        TEXTURE,
                        x + 76,
                        y + 34,
                        176,
                        0,
                        progressWidth,
                        16
                );
            }
        }

        /*
         * ==================================================
         * Energy
         * ==================================================
         */

        int energy =
                menu.getEnergy();

        int maxEnergy =
                menu.getMaxEnergy();

        if (maxEnergy > 0
                && energy > 0) {

            int energyHeight =
                    (int) (
                            52.0F
                                    * energy
                                    / maxEnergy
                    );

            if (energyHeight > 0) {

                graphics.blit(
                        TEXTURE,
                        x + 8,
                        y + 17
                                + (52 - energyHeight),
                        200,
                        0,
                        16,
                        energyHeight
                );
            }
        }

        /*
         * ==================================================
         * Fluid Tanks
         *
         * Tank 0
         *      Input
         *
         * Tank 1～9
         *      Output
         *
         * 液体はGUIテクスチャから描画しない。
         * fill()によって完全に不透明な矩形として描画する。
         * ==================================================
         */

        for (
                int tank = 0;
                tank < DistillationBlockEntity.TANK_COUNT;
                tank++
        ) {

            int amount =
                    menu.getFluidAmount(tank);

            int capacity =
                    menu.getFluidCapacity(tank);

            if (capacity <= 0
                    || amount <= 0) {

                continue;
            }

            int fluidHeight =
                    (int) (
                            52.0F
                                    * amount
                                    / capacity
                    );

            if (fluidHeight <= 0) {
                continue;
            }

            /*
             * タンク位置
             */
            int fluidX =
                    x + 26 + tank * 14;

            int fluidY =
                    y + 17
                            + (52 - fluidHeight);

            /*
             * ==================================================
             * 液体本体
             *
             * ここはARGBで完全不透明。
             *
             * 0xFF = Alpha 255
             * ==================================================
             */

            int fluidColor;

            if (tank == DistillationBlockEntity.INPUT_TANK) {

                // 入力液体
                fluidColor =
                        0xFF4A3322;

            } else {

                // 出力液体
                fluidColor =
                        0xFF4A90E2;
            }

            graphics.fill(
                    fluidX,
                    fluidY,
                    fluidX + 12,
                    fluidY + fluidHeight,
                    fluidColor
            );
        }
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