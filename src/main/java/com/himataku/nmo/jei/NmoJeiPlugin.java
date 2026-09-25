package com.himataku.nmo.jei;

import com.himataku.nmo.CrusherBlock.CrusherRecipe;
import com.himataku.nmo.CrusherBlock.ModRecipes;
import com.himataku.nmo.NewMineOrder;
import com.himataku.nmo.Washing.WashingRecipe;
import com.himataku.nmo.chemnmo.AllBlock;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class NmoJeiPlugin implements IModPlugin {

    private static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(
                    NewMineOrder.MODID,
                    "jei_plugin"
            );

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    // =========================
    // JEI Recipe Categories
    // =========================

    @Override
    public void registerCategories(
            IRecipeCategoryRegistration registration
    ) {
        registration.addRecipeCategories(
                new CrusherRecipeCategory(
                        registration.getJeiHelpers().getGuiHelper()
                ),
                new WashingRecipeCategory(
                        registration.getJeiHelpers().getGuiHelper()
                ),
                new DistillationRecipeCategory(
                        registration.getJeiHelpers().getGuiHelper()
                )
        );
    }

    // =========================
    // JEI Recipes
    // =========================

    @Override
    public void registerRecipes(
            IRecipeRegistration registration
    ) {
        var recipeManager =
                Minecraft.getInstance()
                        .level
                        .getRecipeManager();

        // =========================
        // Crusher
        // =========================

        var crusherRecipes =
                recipeManager
                        .getAllRecipesFor(
                                CrusherRecipe.Type.INSTANCE
                        )
                        .stream()
                        .map(holder -> holder.value())
                        .toList();

        registration.addRecipes(
                CrusherRecipeCategory.RECIPE_TYPE,
                crusherRecipes
        );

        // =========================
        // Washing
        // =========================

        var washingRecipes =
                recipeManager
                        .getAllRecipesFor(
                                ModRecipes.WASHING_TYPE.get()
                        )
                        .stream()
                        .map(holder -> holder.value())
                        .toList();

        registration.addRecipes(
                WashingRecipeCategory.RECIPE_TYPE,
                washingRecipes
        );

        // =========================
        // Distillation
        // =========================

        var distillationRecipes =
                recipeManager
                        .getAllRecipesFor(
                                ModRecipes.DISTILLATION_TYPE.get()
                        )
                        .stream()
                        .map(holder -> holder.value())
                        .toList();

        registration.addRecipes(
                DistillationRecipeCategory.RECIPE_TYPE,
                distillationRecipes
        );
    }

    // =========================
    // JEI Recipe Catalysts
    // =========================

    @Override
    public void registerRecipeCatalysts(
            IRecipeCatalystRegistration registration
    ) {
        // Crusher
        registration.addRecipeCatalyst(
                AllBlock.CRUSHER_ITEM.get(),
                CrusherRecipeCategory.RECIPE_TYPE
        );

        // Washing
        registration.addRecipeCatalyst(
                AllBlock.WASHINGBLOCK_ITEM.get(),
                WashingRecipeCategory.RECIPE_TYPE
        );

        // Distillation
        registration.addRecipeCatalyst(
                AllBlock.DISTILLATION_ITEM.get(),
                DistillationRecipeCategory.RECIPE_TYPE
        );
    }
}
