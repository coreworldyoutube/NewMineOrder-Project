package com.himataku.nmo.Generator;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GeneratorScreen
        extends AbstractContainerScreen<GeneratorMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    "nmo",
                    "textures/gui/generator.png"
            );

    public GeneratorScreen(
            GeneratorMenu menu,
            Inventory inventory,
            Component title
    ) {
        super(
                menu,
                inventory,
                title
        );

        imageWidth = 176;
        imageHeight = 166;
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

        int burnTime =
                menu.getData().get(0);

        int maxBurnTime =
                menu.getData().get(1);

        if (maxBurnTime > 0 && burnTime > 0) {

            int height =
                    burnTime * 14 / maxBurnTime;

            guiGraphics.blit(
                    TEXTURE,
                    x + 80,
                    y + 52 - height,
                    176,
                    0,
                    14,
                    height
            );
        }

        int energy =
                menu.getData().get(4);

        int energyHeight =
                energy * 50 / GeneratorBlockEntity.ENERGY_CAPACITY;

        guiGraphics.blit(
                TEXTURE,
                x + 152,
                y + 68 - energyHeight,
                190,
                0,
                14,
                energyHeight
        );
    }

    @Override
    protected void renderLabels(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY
    ) {
        guiGraphics.drawString(
                font,
                title,
                8,
                6,
                0x404040,
                false
        );

        guiGraphics.drawString(
                font,
                Component.literal(
                        "Water: " +
                                menu.getData().get(2) +
                                " mB"
                ),
                8,
                58,
                0x404040,
                false
        );

        guiGraphics.drawString(
                font,
                Component.literal(
                        "Steam: " +
                                menu.getData().get(3) +
                                " mB"
                ),
                8,
                68,
                0x404040,
                false
        );

        guiGraphics.drawString(
                font,
                Component.literal(
                        "Energy: " +
                                menu.getData().get(4) +
                                " FE"
                ),
                8,
                78,
                0x404040,
                false
        );
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