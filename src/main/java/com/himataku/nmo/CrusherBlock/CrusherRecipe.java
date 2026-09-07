package com.himataku.nmo.CrusherBlock;

import com.himataku.nmo.CrusherBlock.CrusherRecipeInput;
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

    // =========================================================
    // レシピデータ
    // =========================================================

    private final Ingredient input;

    private final ItemStack[] results;


    // =========================================================
    // コンストラクタ
    // =========================================================

    public CrusherRecipe(
            Ingredient input,
            ItemStack[] results
    ) {

        this.input = input;
        this.results = results;
    }


    // =========================================================
    // 結果取得
    // =========================================================

    public ItemStack[] getResults() {
        return results;
    }


    // =========================================================
    // レシピ判定
    // =========================================================

    @Override
    public boolean matches(
            CrusherRecipeInput input,
            Level level
    ) {

        return this.input.test(
                input.getItem(0)
        );
    }


    // =========================================================
    // 作成
    // =========================================================

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


    // =========================================================
    // サイズ判定
    // =========================================================

    @Override
    public boolean canCraftInDimensions(
            int width,
            int height
    ) {

        return true;
    }


    // =========================================================
    // JEI等で表示する代表結果
    // =========================================================

    @Override
    public ItemStack getResultItem(
            HolderLookup.Provider registries
    ) {

        if (results.length == 0) {
            return ItemStack.EMPTY;
        }

        return results[0].copy();
    }


    // =========================================================
    // Serializer
    // =========================================================

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }


    // =========================================================
    // Type
    // =========================================================

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }


    // =========================================================
    // Recipe Type
    // =========================================================

    public static class Type
            implements RecipeType<CrusherRecipe> {

        public static final Type INSTANCE =
                new Type();

        public static final String ID =
                "crusher";
    }


    // =========================================================
    // Recipe Serializer
    // =========================================================

    public static class Serializer
            implements RecipeSerializer<CrusherRecipe> {

        public static final Serializer INSTANCE =
                new Serializer();


        // =====================================================
        // JSON Codec
        // =====================================================

        private static final Codec<List<ItemStack>> RESULTS_CODEC =
                ItemStack.CODEC.listOf();


        public static final MapCodec<CrusherRecipe> CODEC =
                RecordCodecBuilder.mapCodec(
                        instance -> instance.group(

                                Ingredient.CODEC_NONEMPTY
                                        .fieldOf("ingredient")
                                        .forGetter(
                                                recipe ->
                                                        recipe.input
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


        // =====================================================
        // ネットワーク Codec
        // =====================================================

        public static final StreamCodec<
                RegistryFriendlyByteBuf,
                CrusherRecipe
                > STREAM_CODEC =
                StreamCodec.composite(

                        Ingredient.CONTENTS_STREAM_CODEC,
                        recipe ->
                                recipe.input,

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


        // =====================================================
        // Codec
        // =====================================================

        @Override
        public MapCodec<CrusherRecipe> codec() {
            return CODEC;
        }


        // =====================================================
        // Stream Codec
        // =====================================================

        @Override
        public StreamCodec<
                RegistryFriendlyByteBuf,
                CrusherRecipe
                > streamCodec() {

            return STREAM_CODEC;
        }
    }
}