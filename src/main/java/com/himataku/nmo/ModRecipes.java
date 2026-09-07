package com.himataku.nmo.recipe;

import com.himataku.nmo.CrusherBlock.CrusherRecipe;
import com.himataku.nmo.NewMineOrder;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(
                    Registries.RECIPE_SERIALIZER,
                    NewMineOrder.MODID
            );

    public static final DeferredHolder<
            RecipeSerializer<?>,
            RecipeSerializer<CrusherRecipe>
            > CRUSHER =
            SERIALIZERS.register(
                    "crusher",
                    () -> CrusherRecipe.Serializer.INSTANCE
            );
}