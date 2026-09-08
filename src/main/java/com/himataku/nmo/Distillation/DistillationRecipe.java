package com.himataku.nmo.Distillation;

import com.himataku.nmo.CrusherBlock.ModRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

public class DistillationRecipe
        implements Recipe<
        DistillationRecipe.DistillationRecipeInput
        > {

    /*
     * ==================================================
     * Recipe Codec
     * ==================================================
     */

    public static final MapCodec<DistillationRecipe> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(

                            /*
                             * Input Fluid
                             */

                            SizedFluidIngredient.FLAT_CODEC
                                    .fieldOf("input")
                                    .forGetter(
                                            DistillationRecipe::getInput
                                    ),

                            /*
                             * Fluid Outputs
                             *
                             * 最大9個
                             */

                            FluidStack.CODEC
                                    .listOf()
                                    .validate(outputs -> {

                                        if (outputs.size() > 9) {
                                            return DataResult.error(
                                                    () ->
                                                            "Distillation recipe cannot have more than 9 fluid outputs"
                                            );
                                        }

                                        return DataResult.success(
                                                outputs
                                        );
                                    })
                                    .fieldOf("fluid_outputs")
                                    .forGetter(
                                            DistillationRecipe::getFluidOutputs
                                    ),

                            /*
                             * Item Output
                             */

                            ItemStack.CODEC
                                    .optionalFieldOf(
                                            "item_output",
                                            ItemStack.EMPTY
                                    )
                                    .forGetter(
                                            DistillationRecipe::getItemOutput
                                    ),

                            /*
                             * Processing Time
                             */

                            Codec.INT
                                    .fieldOf("processing_time")
                                    .forGetter(
                                            DistillationRecipe::getProcessingTime
                                    ),

                            /*
                             * Energy
                             */

                            Codec.INT
                                    .fieldOf("energy")
                                    .forGetter(
                                            DistillationRecipe::getEnergy
                                    )

                    ).apply(
                            instance,
                            DistillationRecipe::new
                    )
            );

    /*
     * ==================================================
     * Network Codec
     * ==================================================
     */

    public static final StreamCodec<
            RegistryFriendlyByteBuf,
            DistillationRecipe
            > STREAM_CODEC =
            StreamCodec.composite(

                    SizedFluidIngredient.STREAM_CODEC,
                    DistillationRecipe::getInput,

                    FluidStack.STREAM_CODEC.apply(
                            ByteBufCodecs.list()
                    ),
                    DistillationRecipe::getFluidOutputs,

                    ItemStack.OPTIONAL_STREAM_CODEC,
                    DistillationRecipe::getItemOutput,

                    ByteBufCodecs.VAR_INT,
                    DistillationRecipe::getProcessingTime,

                    ByteBufCodecs.VAR_INT,
                    DistillationRecipe::getEnergy,

                    DistillationRecipe::new
            );

    /*
     * ==================================================
     * Recipe Serializer
     * ==================================================
     */

    public static final RecipeSerializer<DistillationRecipe> SERIALIZER =
            new RecipeSerializer<DistillationRecipe>() {

                @Override
                public MapCodec<DistillationRecipe> codec() {
                    return CODEC;
                }

                @Override
                public StreamCodec<
                        RegistryFriendlyByteBuf,
                        DistillationRecipe
                        > streamCodec() {

                    return STREAM_CODEC;
                }
            };

    /*
     * ==================================================
     * Fields
     * ==================================================
     */

    private final SizedFluidIngredient input;

    private final List<FluidStack> fluidOutputs;

    private final ItemStack itemOutput;

    private final int processingTime;

    private final int energy;

    /*
     * ==================================================
     * Constructor
     * ==================================================
     */

    public DistillationRecipe(
            SizedFluidIngredient input,
            List<FluidStack> fluidOutputs,
            ItemStack itemOutput,
            int processingTime,
            int energy
    ) {

        this.input = input;

        this.fluidOutputs =
                fluidOutputs
                        .stream()
                        .map(FluidStack::copy)
                        .toList();

        this.itemOutput =
                itemOutput.copy();

        this.processingTime =
                processingTime;

        this.energy =
                energy;
    }

    /*
     * ==================================================
     * Getters
     * ==================================================
     */

    public SizedFluidIngredient getInput() {
        return input;
    }

    public List<FluidStack> getFluidOutputs() {
        return fluidOutputs
                .stream()
                .map(FluidStack::copy)
                .toList();
    }

    public ItemStack getItemOutput() {
        return itemOutput.copy();
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public int getEnergy() {
        return energy;
    }

    /*
     * ==================================================
     * Recipe
     * ==================================================
     */

    @Override
    public boolean matches(
            DistillationRecipeInput input,
            Level level
    ) {

        return this.input.test(
                input.fluid()
        );
    }

    @Override
    public ItemStack assemble(
            DistillationRecipeInput input,
            HolderLookup.Provider registries
    ) {

        return itemOutput.copy();
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

        return itemOutput.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {

        /*
         * static初期化時には呼ばない。
         * 実際にRecipeTypeが必要になった時点で取得する。
         */

        return ModRecipes.DISTILLATION_TYPE.get();
    }

    /*
     * ==================================================
     * Recipe Input
     * ==================================================
     */

    public record DistillationRecipeInput(
            FluidStack fluid
    ) implements RecipeInput {

        @Override
        public ItemStack getItem(
                int index
        ) {

            return ItemStack.EMPTY;
        }

        @Override
        public int size() {
            return 0;
        }
    }
}