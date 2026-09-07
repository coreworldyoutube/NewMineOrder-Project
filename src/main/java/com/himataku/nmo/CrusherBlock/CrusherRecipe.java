package com.himataku.nmo.recipe;

import com.himataku.nmo.customblock.AllBlock;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

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

        public static final String ID =
                "crusher";
    }

    public static class Serializer
            implements RecipeSerializer<CrusherRecipe> {

        public static final Serializer INSTANCE =
                new Serializer();

        public static final StreamCodec<
                RegistryFriendlyByteBuf,
                CrusherRecipe
                > STREAM_CODEC =
                new StreamCodec<>() {

                    @Override
                    public CrusherRecipe decode(
                            RegistryFriendlyByteBuf buffer
                    ) {

                        Ingredient input =
                                Ingredient.CONTENTS_STREAM_CODEC
                                        .decode(buffer);

                        int count =
                                buffer.readVarInt();

                        ItemStack[] results =
                                new ItemStack[count];

                        for (int i = 0; i < count; i++) {

                            results[i] =
                                    ItemStack.STREAM_CODEC
                                            .decode(buffer);
                        }

                        return new CrusherRecipe(
                                input,
                                results
                        );
                    }

                    @Override
                    public void encode(
                            RegistryFriendlyByteBuf buffer,
                            CrusherRecipe recipe
                    ) {

                        Ingredient.CONTENTS_STREAM_CODEC
                                .encode(
                                        buffer,
                                        recipe.input
                                );

                        buffer.writeVarInt(
                                recipe.results.length
                        );

                        for (ItemStack result :
                                recipe.results) {

                            ItemStack.STREAM_CODEC
                                    .encode(
                                            buffer,
                                            result
                                    );
                        }
                    }
                };

        @Override
        public com.mojang.serialization.MapCodec<CrusherRecipe> codec() {

            return com.mojang.serialization.MapCodec
                    .of(
                            new com.mojang.serialization.MapEncoder<>() {

                                @Override
                                public <T> com.mojang.serialization.RecordBuilder<T> encode(
                                        CrusherRecipe input,
                                        com.mojang.serialization.DynamicOps<T> ops,
                                        com.mojang.serialization.RecordBuilder<T> prefix
                                ) {
                                    return prefix;
                                }
                            },
                            new com.mojang.serialization.MapDecoder<>() {

                                @Override
                                public <T> com.mojang.serialization.DataResult<CrusherRecipe> decode(
                                        com.mojang.serialization.DynamicOps<T> ops,
                                        com.mojang.serialization.MapLike<T> input
                                ) {
                                    return com.mojang.serialization.DataResult.error(
                                            () -> "Use JSON codec"
                                    );
                                }
                            }
                    );
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