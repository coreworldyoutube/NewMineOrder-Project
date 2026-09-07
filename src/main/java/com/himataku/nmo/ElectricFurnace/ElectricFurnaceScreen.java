package com.himataku.nmo.ElectricFurnace;

import com.himataku.nmo.NewMineOrder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ElectricFurnaceScreen
        extends AbstractContainerScreen<ElectricFurnaceMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    NewMineOrder.MODID,
                    "textures/gui/electric_furnace.png"
            );

    public ElectricFurnaceScreen(
            ElectricFurnaceMenu menu,
            Inventory inventory,
            Component title
    ) {

        super(menu, inventory, title);

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

        graphics.blit(
                TEXTURE,
                x,
                y,
                0,
                0,
                this.imageWidth,
                this.imageHeight
        );

        // 電力バー
        int energy =
                menu.getEnergy();

        int maxEnergy =
                menu.getMaxEnergy();

        if (maxEnergy > 0 && energy > 0) {

            int energyHeight =
                    (int) (
                            48.0F
                                    * energy
                                    / maxEnergy
                    );

            graphics.fill(
                    x + 152,
                    y + 17 + (48 - energyHeight),
                    x + 160,
                    y + 65,
                    0xFF00FFFF
            );
        }

        // 進行度
        int progress =
                menu.getProgress();

        int processTime =
                menu.getProcessTime();

        if (processTime > 0 && progress > 0) {

            int progressWidth =
                    (int) (
                            24.0F
                                    * progress
                                    / processTime
                    );

            graphics.fill(
                    x + 79,
                    y + 34,
                    x + 79 + progressWidth,
                    y + 42,
                    0xFFFFAA00
            );
        }
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
                this.titleLabelX,
                this.titleLabelY,
                0x404040,
                false
        );

        graphics.drawString(
                this.font,
                "FE: "
                        + menu.getEnergy()
                        + " / "
                        + menu.getMaxEnergy(),
                8,
                65,
                0x404040,
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

        this.renderBackground(
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

        this.renderTooltip(
                graphics,
                mouseX,
                mouseY
        );
    }
}