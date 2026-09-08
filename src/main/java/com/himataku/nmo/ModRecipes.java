package com.himataku.nmo.CrusherBlock;

import com.himataku.nmo.Distillation.DistillationRecipe;
import com.himataku.nmo.NewMineOrder;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {

    // =========================
    // Recipe Type
    // =========================

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(
                    Registries.RECIPE_TYPE,
                    NewMineOrder.MODID
            );

    public static final DeferredHolder<
            RecipeType<?>,
            RecipeType<DistillationRecipe>
            > DISTILLATION_TYPE =
            RECIPE_TYPES.register(
                    "distillation",
                    () -> RecipeType.simple(
                            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(
                                    NewMineOrder.MODID,
                                    "distillation"
                            )
                    )
            );

    // =========================
    // Recipe Serializer
    // =========================

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

    public static final DeferredHolder<
            RecipeSerializer<?>,
            RecipeSerializer<DistillationRecipe>
            > DISTILLATION =
            SERIALIZERS.register(
                    "distillation",
                    () -> DistillationRecipe.SERIALIZER
            );
}