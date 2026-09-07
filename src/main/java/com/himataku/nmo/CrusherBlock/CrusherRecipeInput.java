package com.himataku.nmo.CrusherBlock;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record CrusherRecipeInput(
        ItemStack input
) implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {

        if (index != 0) {
            return ItemStack.EMPTY;
        }

        return input;
    }

    @Override
    public int size() {
        return 1;
    }
}