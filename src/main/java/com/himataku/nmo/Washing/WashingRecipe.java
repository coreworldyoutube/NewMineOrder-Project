package com.himataku.nmo.Washing;

import com.himataku.nmo.CrusherBlock.ModRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import net.neoforged.neoforge.fluids.FluidStack;

public class WashingRecipe
        implements Recipe<WashingRecipe.WashingRecipeInput> {

    private final Item inputItem;
    private final FluidStack inputFluid;

    private final ItemStack outputItem;
    private final FluidStack dirtyFluid;

    private final int energy;
    private final int processTime;

    public WashingRecipe(
            Item inputItem,
            FluidStack inputFluid,
            ItemStack outputItem,
            FluidStack dirtyFluid,
            int energy,
            int processTime
    ) {
        this.inputItem = inputItem;
        this.inputFluid = inputFluid;
        this.outputItem = outputItem;
        this.dirtyFluid = dirtyFluid;
        this.energy = energy;
        this.processTime = processTime;
    }

    public Item getInputItem() {
        return inputItem;
    }

    public FluidStack getInputFluid() {
        return inputFluid;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public FluidStack getDirtyFluid() {
        return dirtyFluid;
    }

    public int getEnergy() {
        return energy;
    }

    public int getProcessTime() {
        return processTime;
    }

    @Override
    public boolean matches(
            WashingRecipeInput input,
            Level level
    ) {
        if (input.item().isEmpty()) {
            return false;
        }

        if (input.item().getItem() != inputItem) {
            return false;
        }

        FluidStack fluid = input.fluid();

        if (fluid.isEmpty()) {
            return false;
        }

        return FluidStack.isSameFluidSameComponents(
                fluid,
                inputFluid
        );
    }

    @Override
    public ItemStack assemble(
            WashingRecipeInput input,
            HolderLookup.Provider registries
    ) {
        return outputItem.copy();
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
        return outputItem.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.WASHING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.WASHING_TYPE.get();
    }

    /*
     * ============================================================
     * Recipe Input
     * ============================================================
     */

    public record WashingRecipeInput(
            ItemStack item,
            FluidStack fluid
    ) implements RecipeInput {

        @Override
        public ItemStack getItem(int index) {

            if (index != 0) {
                return ItemStack.EMPTY;
            }

            return item;
        }

        @Override
        public int size() {
            return 1;
        }
    }

    /*
     * ============================================================
     * Serializer
     * ============================================================
     */

    public static class Serializer
            implements RecipeSerializer<WashingRecipe> {

        public static final Serializer INSTANCE =
                new Serializer();

        /*
         * ========================================================
         * Fluid JSON Data
         *
         * JSON:
         *
         * "fluid": {
         *     "fluid": "minecraft:water",
         *     "amount": 1
         * }
         *
         * ========================================================
         */

        private record FluidData(
                ResourceLocation fluid,
                int amount
        ) {

            private static final MapCodec<FluidData> CODEC =
                    RecordCodecBuilder.mapCodec(instance ->
                            instance.group(

                                    ResourceLocation.CODEC
                                            .fieldOf("fluid")
                                            .forGetter(
                                                    FluidData::fluid
                                            ),

                                    Codec.INT
                                            .fieldOf("amount")
                                            .forGetter(
                                                    FluidData::amount
                                            )

                            ).apply(
                                    instance,
                                    FluidData::new
                            )
                    );
        }

        /*
         * ========================================================
         * Input Codec
         * ========================================================
         */

        private static final MapCodec<InputData> INPUT_CODEC =
                RecordCodecBuilder.mapCodec(instance ->
                        instance.group(

                                ResourceLocation.CODEC
                                        .fieldOf("item")
                                        .forGetter(
                                                InputData::item
                                        ),

                                FluidData.CODEC
                                        .fieldOf("fluid")
                                        .forGetter(
                                                InputData::fluid
                                        )

                        ).apply(
                                instance,
                                InputData::new
                        )
                );

        /*
         * ========================================================
         * Output Codec
         * ========================================================
         */

        private static final MapCodec<OutputData> OUTPUT_CODEC =
                RecordCodecBuilder.mapCodec(instance ->
                        instance.group(

                                ResourceLocation.CODEC
                                        .fieldOf("item")
                                        .forGetter(
                                                OutputData::item
                                        ),

                                FluidData.CODEC
                                        .fieldOf("dirty_fluid")
                                        .forGetter(
                                                OutputData::dirtyFluid
                                        )

                        ).apply(
                                instance,
                                OutputData::new
                        )
                );

        /*
         * ========================================================
         * Recipe Codec
         * ========================================================
         */

        public static final MapCodec<WashingRecipe> CODEC =
                RecordCodecBuilder.mapCodec(instance ->
                        instance.group(

                                INPUT_CODEC
                                        .fieldOf("input")
                                        .forGetter(
                                                recipe ->
                                                        new InputData(
                                                                BuiltInRegistries.ITEM.getKey(
                                                                        recipe.inputItem
                                                                ),
                                                                new FluidData(
                                                                        BuiltInRegistries.FLUID.getKey(
                                                                                recipe.inputFluid.getFluid()
                                                                        ),
                                                                        recipe.inputFluid.getAmount()
                                                                )
                                                        )
                                        ),

                                OUTPUT_CODEC
                                        .fieldOf("output")
                                        .forGetter(
                                                recipe ->
                                                        new OutputData(
                                                                BuiltInRegistries.ITEM.getKey(
                                                                        recipe.outputItem.getItem()
                                                                ),
                                                                new FluidData(
                                                                        BuiltInRegistries.FLUID.getKey(
                                                                                recipe.dirtyFluid.getFluid()
                                                                        ),
                                                                        recipe.dirtyFluid.getAmount()
                                                                )
                                                        )
                                        ),

                                Codec.INT
                                        .fieldOf("energy")
                                        .forGetter(
                                                WashingRecipe::getEnergy
                                        ),

                                Codec.INT
                                        .fieldOf("process_time")
                                        .forGetter(
                                                WashingRecipe::getProcessTime
                                        )

                        ).apply(
                                instance,
                                (input, output, energy, processTime) -> {

                                    Item inputItem =
                                            BuiltInRegistries.ITEM.get(
                                                    input.item()
                                            );

                                    Item outputItem =
                                            BuiltInRegistries.ITEM.get(
                                                    output.item()
                                            );

                                    FluidStack inputFluid =
                                            new FluidStack(
                                                    BuiltInRegistries.FLUID.get(
                                                            input.fluid().fluid()
                                                    ),
                                                    input.fluid().amount()
                                            );

                                    FluidStack dirtyFluid =
                                            new FluidStack(
                                                    BuiltInRegistries.FLUID.get(
                                                            output.dirtyFluid().fluid()
                                                    ),
                                                    output.dirtyFluid().amount()
                                            );

                                    return new WashingRecipe(
                                            inputItem,
                                            inputFluid,
                                            new ItemStack(outputItem),
                                            dirtyFluid,
                                            energy,
                                            processTime
                                    );
                                }
                        )
                );

        /*
         * ========================================================
         * Network Codec
         * ========================================================
         */

        public static final StreamCodec<
                RegistryFriendlyByteBuf,
                WashingRecipe
                > STREAM_CODEC =
                StreamCodec.of(
                        Serializer::toNetwork,
                        Serializer::fromNetwork
                );

        @Override
        public MapCodec<WashingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<
                RegistryFriendlyByteBuf,
                WashingRecipe
                > streamCodec() {
            return STREAM_CODEC;
        }

        /*
         * ========================================================
         * Write Network
         * ========================================================
         */

        private static void toNetwork(
                RegistryFriendlyByteBuf buffer,
                WashingRecipe recipe
        ) {

            buffer.writeResourceLocation(
                    BuiltInRegistries.ITEM.getKey(
                            recipe.inputItem
                    )
            );

            FluidStack.STREAM_CODEC.encode(
                    buffer,
                    recipe.inputFluid
            );

            buffer.writeResourceLocation(
                    BuiltInRegistries.ITEM.getKey(
                            recipe.outputItem.getItem()
                    )
            );

            FluidStack.STREAM_CODEC.encode(
                    buffer,
                    recipe.dirtyFluid
            );

            buffer.writeInt(
                    recipe.energy
            );

            buffer.writeInt(
                    recipe.processTime
            );
        }

        /*
         * ========================================================
         * Read Network
         * ========================================================
         */

        private static WashingRecipe fromNetwork(
                RegistryFriendlyByteBuf buffer
        ) {

            ResourceLocation inputItemId =
                    buffer.readResourceLocation();

            Item inputItem =
                    BuiltInRegistries.ITEM.get(
                            inputItemId
                    );

            FluidStack inputFluid =
                    FluidStack.STREAM_CODEC.decode(
                            buffer
                    );

            ResourceLocation outputItemId =
                    buffer.readResourceLocation();

            Item outputItem =
                    BuiltInRegistries.ITEM.get(
                            outputItemId
                    );

            FluidStack dirtyFluid =
                    FluidStack.STREAM_CODEC.decode(
                            buffer
                    );

            int energy =
                    buffer.readInt();

            int processTime =
                    buffer.readInt();

            return new WashingRecipe(
                    inputItem,
                    inputFluid,
                    new ItemStack(outputItem),
                    dirtyFluid,
                    energy,
                    processTime
            );
        }

        /*
         * ========================================================
         * Codec Data
         * ========================================================
         */

        private record InputData(
                ResourceLocation item,
                FluidData fluid
        ) {
        }

        private record OutputData(
                ResourceLocation item,
                FluidData dirtyFluid
        ) {
        }
    }
}