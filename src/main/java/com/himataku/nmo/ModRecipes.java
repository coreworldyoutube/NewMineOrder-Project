package com.himataku.nmo.CrusherBlock;

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
            CrusherRecipe.Serializer
            > CRUSHER =
            SERIALIZERS.register(
                    "crusher",
                    () -> CrusherRecipe.Serializer.INSTANCE
            );
}