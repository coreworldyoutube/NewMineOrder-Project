package com.himataku.nmo.jei;

import com.himataku.nmo.NewMineOrder;
import com.himataku.nmo.Washing.WashingRecipe;

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

public class WashingRecipeCategory
        implements IRecipeCategory<WashingRecipe> {

    public static final RecipeType<WashingRecipe> RECIPE_TYPE =
            new RecipeType<>(
                    ResourceLocation.fromNamespaceAndPath(
                            NewMineOrder.MODID,
                            "washing"
                    ),
                    WashingRecipe.class
            );

    private final IDrawable icon;

    public WashingRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemStack(
                new ItemStack(
                        AllBlock.WASHINGBLOCK_ITEM.get()
                )
        );
    }

    @Override
    public RecipeType<WashingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(
                "jei.nmo.category.washing"
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
            WashingRecipe recipe,
            IFocusGroup focuses
    ) {

        // =========================
        // Input Item
        // =========================

        builder.addSlot(
                RecipeIngredientRole.INPUT,
                25,
                55
        ).addItemStack(
                new ItemStack(
                        recipe.getInputItem()
                )
        );

        // =========================
        // Input Fluid
        // =========================

        builder.addSlot(
                RecipeIngredientRole.INPUT,
                25,
                90
        ).addIngredients(
                NeoForgeTypes.FLUID_STACK,
                java.util.List.of(
                        recipe.getInputFluid()
                )
        );

        // =========================
        // Output Items
        // =========================

        int outputIndex = 0;

        for (
                WashingRecipe.Output output
                : recipe.getOutputs()
        ) {

            if (outputIndex >= 3) {
                break;
            }

            int x =
                    100
                            + outputIndex * 25;

            builder.addSlot(
                    RecipeIngredientRole.OUTPUT,
                    x,
                    55
            ).addItemStack(
                    output.item()
            );

            outputIndex++;
        }

        // =========================
        // Dirty Fluid
        // =========================

        builder.addSlot(
                RecipeIngredientRole.OUTPUT,
                125,
                90
        ).addIngredients(
                NeoForgeTypes.FLUID_STACK,
                java.util.List.of(
                        recipe.getDirtyFluid()
                )
        );
    }

    @Override
    public void draw(
            WashingRecipe recipe,
            IRecipeSlotsView recipeSlotsView,
            GuiGraphics guiGraphics,
            double mouseX,
            double mouseY
    ) {

        Minecraft minecraft =
                Minecraft.getInstance();

        // =========================
        // Liquid Relation
        // =========================

        String arrow =
                "→";

        guiGraphics.drawString(
                minecraft.font,
                arrow,
                78,
                91,
                0xFFFFFF,
                false
        );

        // =========================
        // Input Fluid Amount
        // =========================

        String inputAmount =
                recipe.getInputFluid().getAmount()
                        + " mB";

        guiGraphics.drawString(
                minecraft.font,
                inputAmount,
                25,
                108,
                0xFFFFFF,
                false
        );

        // =========================
        // Dirty Fluid Amount
        // =========================

        String dirtyAmount =
                recipe.getDirtyFluid().getAmount()
                        + " mB";

        guiGraphics.drawString(
                minecraft.font,
                dirtyAmount,
                125,
                108,
                0xFFFFFF,
                false
        );

        // =========================
        // Output Chance
        // =========================

        int outputIndex = 0;

        for (
                WashingRecipe.Output output
                : recipe.getOutputs()
        ) {

            if (outputIndex >= 3) {
                break;
            }

            int x =
                    100
                            + outputIndex * 25;

            String chanceText =
                    formatChance(
                            output.chance()
                    );

            guiGraphics.drawString(
                    minecraft.font,
                    chanceText,
                    x,
                    75,
                    0xFFFFFF,
                    false
            );

            outputIndex++;
        }

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
        // Process Time
        // =========================

        String timeText =
                recipe.getProcessTime() + " ticks";

        guiGraphics.drawString(
                minecraft.font,
                timeText,
                65,
                32,
                0xFFFFFF,
                false
        );
    }

    /*
     * ============================================================
     * Chance Display
     * ============================================================
     */

    private String formatChance(
            float chance
    ) {

        float percent =
                chance * 100.0F;

        if (percent == (int) percent) {

            return (int) percent + "%";
        }

        return String.format(
                "%.1f%%",
                percent
        );
    }
}