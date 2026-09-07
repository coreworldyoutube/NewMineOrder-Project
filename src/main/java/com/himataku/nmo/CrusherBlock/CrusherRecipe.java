package com.himataku.nmo.CrusherBlock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public class CrusherRecipe
        implements Recipe<CrusherRecipeInput> {

    private final Ingredient input;
    private final ItemStack[] results;

    public CrusherRecipe(
            Ingredient input,
            ItemStack[] results
    ) {
        this.input = input;
        this.results = results;
    }

    public Ingredient getInput() {
        return input;
    }

    public ItemStack[] getResults() {
        return results;
    }

    @Override
    public boolean matches(
            CrusherRecipeInput input,
            Level level
    ) {
        return this.input.test(
                input.getItem(0)
        );
    }

    @Override
    public ItemStack assemble(
            CrusherRecipeInput input,
            HolderLookup.Provider registries
    ) {
        if (results.length == 0) {
            return ItemStack.EMPTY;
        }

        return results[0].copy();
    }

    @Override
    public boolean canCraftInDimensions(
            int width,
            int height
    ) {
        return true;
    }

    @Override
    public ItemStack getResultItem(
            HolderLookup.Provider registries
    ) {
        if (results.length == 0) {
            return ItemStack.EMPTY;
        }

        return results[0].copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type
            implements RecipeType<CrusherRecipe> {

        public static final Type INSTANCE =
                new Type();
    }

    public static class Serializer
            implements RecipeSerializer<CrusherRecipe> {

        public static final Serializer INSTANCE =
                new Serializer();

        private static final Codec<List<ItemStack>> RESULTS_CODEC =
                ItemStack.CODEC.listOf();

        public static final MapCodec<CrusherRecipe> CODEC =
                RecordCodecBuilder.mapCodec(
                        instance -> instance.group(
                                Ingredient.CODEC_NONEMPTY
                                        .fieldOf("ingredient")
                                        .forGetter(
                                                CrusherRecipe::getInput
                                        ),

                                RESULTS_CODEC
                                        .fieldOf("results")
                                        .forGetter(
                                                recipe ->
                                                        List.of(
                                                                recipe.results
                                                        )
                                        )
                        ).apply(
                                instance,
                                (input, results) ->
                                        new CrusherRecipe(
                                                input,
                                                results.toArray(
                                                        ItemStack[]::new
                                                )
                                        )
                        )
                );

        public static final StreamCodec<
                RegistryFriendlyByteBuf,
                CrusherRecipe
                > STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC,
                        CrusherRecipe::getInput,

                        ItemStack.STREAM_CODEC.apply(
                                ByteBufCodecs.list()
                        ),
                        recipe ->
                                List.of(recipe.results),

                        (input, results) ->
                                new CrusherRecipe(
                                        input,
                                        results.toArray(
                                                ItemStack[]::new
                                        )
                                )
                );

        @Override
        public MapCodec<CrusherRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<
                RegistryFriendlyByteBuf,
                CrusherRecipe
                > streamCodec() {
            return STREAM_CODEC;
        }
    }
}