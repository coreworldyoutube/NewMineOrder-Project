package com.himataku.nmo.jei;

import com.himataku.nmo.Distillation.DistillationRecipe;
import com.himataku.nmo.NewMineOrder;

import com.himataku.nmo.chemnmo.AllBlock;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class DistillationRecipeCategory
        implements IRecipeCategory<DistillationRecipe> {

    public static final RecipeType<DistillationRecipe> RECIPE_TYPE =
            new RecipeType<>(
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "distillation"
                    ),
                    DistillationRecipe.class
            );

    private final IDrawable icon;

    public DistillationRecipeCategory(
            IGuiHelper guiHelper
    ) {
        this.icon = guiHelper.createDrawableItemStack(
                new ItemStack(
                        AllBlock.DISTILLATION_ITEM.get()
                )
        );
    }

    @Override
    public RecipeType<DistillationRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(
                "jei.nmo.category.distillation"
        );
    }

    @Override
    public int getWidth() {
        return 176;
    }

    @Override
    public int getHeight() {
        return 166;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(
            IRecipeLayoutBuilder builder,
            DistillationRecipe recipe,
            IFocusGroup focuses
    ) {

        // =========================
        // Input Fluid
        // =========================

        builder.addSlot(
                RecipeIngredientRole.INPUT,
                25,
                75
        ).addIngredients(
                NeoForgeTypes.FLUID_STACK,
                java.util.List.of(
                        recipe.getInput().getFluids()
                )
        );

        // =========================
        // Fluid Outputs
        // =========================

        var fluidOutputs =
                recipe.getFluidOutputs();

        for (
                int i = 0;
                i < fluidOutputs.size() && i < 9;
                i++
        ) {

            builder.addSlot(
                    RecipeIngredientRole.OUTPUT,
                    75 + ((i % 3) * 25),
                    50 + ((i / 3) * 25)
            ).addIngredients(
                    NeoForgeTypes.FLUID_STACK,
                    java.util.List.of(
                            fluidOutputs.get(i)
                    )
            );
        }

        // =========================
        // Item Output
        // =========================

        ItemStack itemOutput =
                recipe.getItemOutput();

        if (!itemOutput.isEmpty()) {

            builder.addSlot(
                    RecipeIngredientRole.OUTPUT,
                    125,
                    125
            ).addItemStack(
                    itemOutput
            );
        }
    }

    @Override
    public void draw(
            DistillationRecipe recipe,
            IRecipeSlotsView recipeSlotsView,
            GuiGraphics guiGraphics,
            double mouseX,
            double mouseY
    ) {

        Minecraft minecraft =
                Minecraft.getInstance();

        // =========================
        // Energy
        // =========================

        String energyText =
                recipe.getEnergy() + " FE/t";

        guiGraphics.drawString(
                minecraft.font,
                energyText,
                65,
                20,
                0xFFFFFF,
                false
        );

        // =========================
        // Processing Time
        // =========================

        String timeText =
                recipe.getProcessingTime() + " ticks";

        guiGraphics.drawString(
                minecraft.font,
                timeText,
                65,
                32,
                0xFFFFFF,
                false
        );
    }
}

