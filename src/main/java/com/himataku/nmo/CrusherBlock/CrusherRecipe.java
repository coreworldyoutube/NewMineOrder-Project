package com.himataku.nmo.CrusherBlock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
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
    private final ResultEntry[] results;

    public CrusherRecipe(
            Ingredient input,
            ResultEntry[] results
    ) {
        this.input = input;
        this.results = results;
    }

    public Ingredient getInput() {
        return input;
    }

    public ResultEntry[] getResults() {
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

        return results[0].stack().copy();
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

        return results[0].stack().copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    /*
     * ========================================
     * Result
     * ========================================
     */

    public record ResultEntry(
            ItemStack stack,
            float chance
    ) {
    }

    /*
     * ========================================
     * Recipe Type
     * ========================================
     */

    public static class Type
            implements RecipeType<CrusherRecipe> {

        public static final Type INSTANCE =
                new Type();
    }

    /*
     * ========================================
     * Serializer
     * ========================================
     */

    public static class Serializer
            implements RecipeSerializer<CrusherRecipe> {

        public static final Serializer INSTANCE =
                new Serializer();

        /*
         * ----------------------------------------
         * Item ID Codec
         * ----------------------------------------
         */

        private static final Codec<Item> ITEM_CODEC =
                BuiltInRegistries.ITEM
                        .byNameCodec();

        /*
         * ----------------------------------------
         * Result Codec
         * ----------------------------------------
         */

        private static final MapCodec<ResultEntry> RESULT_CODEC =
                RecordCodecBuilder.mapCodec(
                        instance -> instance.group(

                                ITEM_CODEC
                                        .fieldOf("id")
                                        .forGetter(
                                                result ->
                                                        result.stack()
                                                                .getItem()
                                        ),

                                Codec.INT
                                        .optionalFieldOf(
                                                "count",
                                                1
                                        )
                                        .forGetter(
                                                result ->
                                                        result.stack()
                                                                .getCount()
                                        ),

                                Codec.FLOAT
                                        .optionalFieldOf(
                                                "chance",
                                                1.0F
                                        )
                                        .forGetter(
                                                ResultEntry::chance
                                        )

                        ).apply(
                                instance,
                                (item, count, chance) ->
                                        new ResultEntry(
                                                new ItemStack(
                                                        item,
                                                        count
                                                ),
                                                chance
                                        )
                        )
                );

        private static final Codec<List<ResultEntry>> RESULTS_CODEC =
                RESULT_CODEC.codec().listOf();

        /*
         * ----------------------------------------
         * Recipe Codec
         * ----------------------------------------
         */

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
                                                        ResultEntry[]::new
                                                )
                                        )
                        )
                );

        /*
         * ----------------------------------------
         * Network Result Stream Codec
         * ----------------------------------------
         */

        public static final StreamCodec<
                RegistryFriendlyByteBuf,
                ResultEntry
                > RESULT_STREAM_CODEC =
                StreamCodec.composite(

                        ItemStack.STREAM_CODEC,
                        ResultEntry::stack,

                        ByteBufCodecs.FLOAT,
                        ResultEntry::chance,

                        ResultEntry::new
                );

        /*
         * ----------------------------------------
         * Network Recipe Stream Codec
         * ----------------------------------------
         */

        public static final StreamCodec<
                RegistryFriendlyByteBuf,
                CrusherRecipe
                > STREAM_CODEC =
                StreamCodec.composite(

                        Ingredient.CONTENTS_STREAM_CODEC,
                        CrusherRecipe::getInput,

                        RESULT_STREAM_CODEC.apply(
                                ByteBufCodecs.list()
                        ),
                        recipe ->
                                List.of(recipe.results),

                        (input, results) ->
                                new CrusherRecipe(
                                        input,
                                        results.toArray(
                                                ResultEntry[]::new
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