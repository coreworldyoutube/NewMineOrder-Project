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

import java.util.List;

public class WashingRecipe
        implements Recipe<WashingRecipe.WashingRecipeInput> {

    /*
     * ============================================================
     * Input
     * ============================================================
     */

    private final Item inputItem;
    private final FluidStack inputFluid;

    /*
     * ============================================================
     * Outputs
     * ============================================================
     */

    private final List<Output> outputs;

    /*
     * ============================================================
     * Dirty Fluid
     * ============================================================
     */

    private final FluidStack dirtyFluid;

    /*
     * ============================================================
     * Machine Settings
     * ============================================================
     */

    private final int energy;
    private final int processTime;

    /*
     * ============================================================
     * Constructor
     * ============================================================
     */

    public WashingRecipe(
            Item inputItem,
            FluidStack inputFluid,
            List<Output> outputs,
            FluidStack dirtyFluid,
            int energy,
            int processTime
    ) {

        this.inputItem = inputItem;
        this.inputFluid = inputFluid;
        this.outputs = outputs;
        this.dirtyFluid = dirtyFluid;
        this.energy = energy;
        this.processTime = processTime;
    }

    /*
     * ============================================================
     * Getters
     * ============================================================
     */

    public Item getInputItem() {
        return inputItem;
    }

    public FluidStack getInputFluid() {
        return inputFluid;
    }

    public List<Output> getOutputs() {
        return outputs;
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

    /*
     * ============================================================
     * Recipe Matching
     * ============================================================
     */

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

        FluidStack fluid =
                input.fluid();

        if (fluid.isEmpty()) {
            return false;
        }

        return FluidStack.isSameFluidSameComponents(
                fluid,
                inputFluid
        );
    }

    /*
     * ============================================================
     * Assemble
     * ============================================================
     *
     * 複数出力なので、ここでは最初の出力を
     * Recipeの代表結果として返す。
     *
     * 実際のWashing処理は
     * WashingBlockEntity側でoutputsを処理する。
     *
     * ============================================================
     */

    @Override
    public ItemStack assemble(
            WashingRecipeInput input,
            HolderLookup.Provider registries
    ) {

        if (outputs.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return outputs.get(0).item().copy();
    }

    /*
     * ============================================================
     * Craft Dimensions
     * ============================================================
     */

    @Override
    public boolean canCraftInDimensions(
            int width,
            int height
    ) {
        return true;
    }

    /*
     * ============================================================
     * Result Item
     * ============================================================
     */

    @Override
    public ItemStack getResultItem(
            HolderLookup.Provider registries
    ) {

        if (outputs.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return outputs.get(0).item().copy();
    }

    /*
     * ============================================================
     * Recipe Serializer
     * ============================================================
     */

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.WASHING.get();
    }

    /*
     * ============================================================
     * Recipe Type
     * ============================================================
     */

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
        public ItemStack getItem(
                int index
        ) {

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
     * Output
     * ============================================================
     *
     * JSON:
     *
     * {
     *     "item": "minecraft:redstone",
     *     "amount": 1,
     *     "chance": 1.0
     * }
     *
     * amount:
     * 生成個数
     *
     * chance:
     * 生成確率
     *
     * 1.0 = 100%
     * 0.5 = 50%
     * 0.25 = 25%
     *
     * ============================================================
     */

    public record Output(
            ItemStack item,
            float chance
    ) {
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
         * Fluid Data
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
         * Input Data
         * ========================================================
         */

        private static InputData getInputData(
                WashingRecipe recipe
        ) {

            return new InputData(
                    BuiltInRegistries.ITEM.getKey(
                            recipe.inputItem
                    ),
                    new FluidData(
                            BuiltInRegistries.FLUID.getKey(
                                    recipe.inputFluid.getFluid()
                            ),
                            recipe.inputFluid.getAmount()
                    )
            );
        }

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
         * Output Data
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

                                Codec.INT
                                        .fieldOf("amount")
                                        .forGetter(
                                                OutputData::amount
                                        ),

                                Codec.FLOAT
                                        .fieldOf("chance")
                                        .forGetter(
                                                OutputData::chance
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

                                /*
                                 * Input
                                 */

                                INPUT_CODEC
                                        .fieldOf("input")
                                        .forGetter(
                                                Serializer::getInputData
                                        ),

                                /*
                                 * Outputs
                                 */

                                OUTPUT_CODEC.codec()
                                        .listOf()
                                        .fieldOf("outputs")
                                        .forGetter(
                                                recipe ->
                                                        recipe.outputs
                                                                .stream()
                                                                .map(
                                                                        output ->
                                                                                new OutputData(
                                                                                        BuiltInRegistries.ITEM.getKey(
                                                                                                output.item().getItem()
                                                                                        ),
                                                                                        output.item().getCount(),
                                                                                        output.chance()
                                                                                )
                                                                )
                                                                .toList()
                                        ),

                                /*
                                 * Dirty Fluid
                                 */

                                FluidData.CODEC
                                        .fieldOf("dirty_fluid")
                                        .forGetter(
                                                recipe ->
                                                        new FluidData(
                                                                BuiltInRegistries.FLUID.getKey(
                                                                        recipe.dirtyFluid.getFluid()
                                                                ),
                                                                recipe.dirtyFluid.getAmount()
                                                        )
                                        ),

                                /*
                                 * Energy
                                 */

                                Codec.INT
                                        .fieldOf("energy")
                                        .forGetter(
                                                WashingRecipe::getEnergy
                                        ),

                                /*
                                 * Process Time
                                 */

                                Codec.INT
                                        .fieldOf("process_time")
                                        .forGetter(
                                                WashingRecipe::getProcessTime
                                        )

                        ).apply(
                                instance,
                                (
                                        input,
                                        outputs,
                                        dirtyFluid,
                                        energy,
                                        processTime
                                ) -> {

                                    /*
                                     * Input Item
                                     */

                                    Item inputItem =
                                            BuiltInRegistries.ITEM.get(
                                                    input.item()
                                            );

                                    /*
                                     * Input Fluid
                                     */

                                    if (input.fluid().amount() <= 0) {
                                        throw new IllegalArgumentException(
                                                "Washing recipe input fluid amount must be greater than 0: "
                                                        + input.fluid().fluid()
                                                        + " amount="
                                                        + input.fluid().amount()
                                        );
                                    }

                                    var inputFluidHolder =
                                            BuiltInRegistries.FLUID
                                                    .getHolder(
                                                            input.fluid().fluid()
                                                    )
                                                    .orElseThrow(() ->
                                                            new IllegalArgumentException(
                                                                    "Unknown washing recipe input fluid: "
                                                                            + input.fluid().fluid()
                                                            )
                                                    );

                                    FluidStack inputFluid =
                                            new FluidStack(
                                                    inputFluidHolder.value(),
                                                    input.fluid().amount()
                                            );

                                    /*
                                     * Outputs
                                     */

                                    List<Output> recipeOutputs =
                                            outputs.stream()
                                                    .map(
                                                            output -> {

                                                                Item item =
                                                                        BuiltInRegistries.ITEM.get(
                                                                                output.item()
                                                                        );

                                                                ItemStack stack =
                                                                        new ItemStack(
                                                                                item,
                                                                                output.amount()
                                                                        );

                                                                return new Output(
                                                                        stack,
                                                                        output.chance()
                                                                );
                                                            }
                                                    )
                                                    .toList();

                                    /*
                                     * Dirty Fluid
                                     */

                                    if (dirtyFluid.amount() <= 0) {
                                        throw new IllegalArgumentException(
                                                "Washing recipe dirty fluid amount must be greater than 0: "
                                                        + dirtyFluid.fluid()
                                                        + " amount="
                                                        + dirtyFluid.amount()
                                        );
                                    }

                                    var dirtyFluidHolder =
                                            BuiltInRegistries.FLUID
                                                    .getHolder(
                                                            dirtyFluid.fluid()
                                                    )
                                                    .orElseThrow(() ->
                                                            new IllegalArgumentException(
                                                                    "Unknown washing recipe dirty fluid: "
                                                                            + dirtyFluid.fluid()
                                                            )
                                                    );

                                    FluidStack dirtyFluidStack =
                                            new FluidStack(
                                                    dirtyFluidHolder.value(),
                                                    dirtyFluid.amount()
                                            );

                                    return new WashingRecipe(
                                            inputItem,
                                            inputFluid,
                                            recipeOutputs,
                                            dirtyFluidStack,
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

            /*
             * Input Item
             */

            buffer.writeResourceLocation(
                    BuiltInRegistries.ITEM.getKey(
                            recipe.inputItem
                    )
            );

            /*
             * Input Fluid
             */

            FluidStack.STREAM_CODEC.encode(
                    buffer,
                    recipe.inputFluid
            );

            /*
             * Outputs
             */

            buffer.writeInt(
                    recipe.outputs.size()
            );

            for (
                    Output output
                    : recipe.outputs
            ) {

                buffer.writeResourceLocation(
                        BuiltInRegistries.ITEM.getKey(
                                output.item().getItem()
                        )
                );

                buffer.writeInt(
                        output.item().getCount()
                );

                buffer.writeFloat(
                        output.chance()
                );
            }

            /*
             * Dirty Fluid
             */

            FluidStack.STREAM_CODEC.encode(
                    buffer,
                    recipe.dirtyFluid
            );

            /*
             * Machine Settings
             */

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

            /*
             * Input Item
             */

            ResourceLocation inputItemId =
                    buffer.readResourceLocation();

            Item inputItem =
                    BuiltInRegistries.ITEM.get(
                            inputItemId
                    );

            /*
             * Input Fluid
             */

            FluidStack inputFluid =
                    FluidStack.STREAM_CODEC.decode(
                            buffer
                    );

            /*
             * Outputs
             */

            int outputCount =
                    buffer.readInt();

            List<Output> outputs =
                    new java.util.ArrayList<>();

            for (
                    int i = 0;
                    i < outputCount;
                    i++
            ) {

                ResourceLocation outputItemId =
                        buffer.readResourceLocation();

                Item outputItem =
                        BuiltInRegistries.ITEM.get(
                                outputItemId
                        );

                int amount =
                        buffer.readInt();

                float chance =
                        buffer.readFloat();

                outputs.add(
                        new Output(
                                new ItemStack(
                                        outputItem,
                                        amount
                                ),
                                chance
                        )
                );
            }

            /*
             * Dirty Fluid
             */

            FluidStack dirtyFluid =
                    FluidStack.STREAM_CODEC.decode(
                            buffer
                    );

            /*
             * Machine Settings
             */

            int energy =
                    buffer.readInt();

            int processTime =
                    buffer.readInt();

            return new WashingRecipe(
                    inputItem,
                    inputFluid,
                    outputs,
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
                int amount,
                float chance
        ) {
        }
    }
}