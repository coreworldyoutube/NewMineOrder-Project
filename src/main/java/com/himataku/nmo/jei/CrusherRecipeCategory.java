package com.himataku.nmo.jei;

import com.himataku.nmo.CrusherBlock.CrusherRecipe;
import com.himataku.nmo.NewMineOrder;

import com.himataku.nmo.chemnmo.AllBlock;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CrusherRecipeCategory
        implements IRecipeCategory<CrusherRecipe> {

    public static final RecipeType<CrusherRecipe> RECIPE_TYPE =
            new RecipeType<>(
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "crusher"
                    ),
                    CrusherRecipe.class
            );

    private final IDrawable icon;

    public CrusherRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemStack(
                new ItemStack(
                        AllBlock.CRUSHER_ITEM.get()
                )
        );
    }

    @Override
    public RecipeType<CrusherRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(
                "jei.nmo.category.crusher"
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
            CrusherRecipe recipe,
            IFocusGroup focuses
    ) {
        builder.addSlot(
                mezz.jei.api.recipe.RecipeIngredientRole.INPUT,
                25,
                75
        ).addIngredients(
                recipe.getInput()
        );

        CrusherRecipe.ResultEntry[] results =
                recipe.getResults();

        for (int i = 0; i < results.length && i < 3; i++) {

            CrusherRecipe.ResultEntry result =
                    results[i];

            builder.addSlot(
                    mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT,
                    90 + (i * 25),
                    75
            ).addItemStack(
                    result.stack()
            );
        }
    }

    @Override
    public void draw(
            CrusherRecipe recipe,
            IRecipeSlotsView recipeSlotsView,
            GuiGraphics guiGraphics,
            double mouseX,
            double mouseY
    ) {
        CrusherRecipe.ResultEntry[] results =
                recipe.getResults();

        for (int i = 0; i < results.length && i < 3; i++) {

            CrusherRecipe.ResultEntry result =
                    results[i];

            int x = 90 + (i * 25);
            int y = 95;

            int percentage =
                    Math.round(result.chance() * 100);

            String text =
                    percentage + "%";

            int textWidth =
                    Minecraft.getInstance()
                            .font
                            .width(text);

            guiGraphics.drawString(
                    Minecraft.getInstance().font,
                    text,
                    x + 8 - (textWidth / 2),
                    y,
                    0xFFFFFF,
                    false
            );
        }
    }
}
