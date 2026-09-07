package com.himataku.nmo.CrusherBlock;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class CrusherRecipeInput implements RecipeInput {

    private final ItemStack input;

    public CrusherRecipeInput(ItemStack input) {
        this.input = input;
    }

    @Override
    public ItemStack getItem(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException(
                    "CrusherRecipeInput only has one slot"
            );
        }

        return input;
    }

    @Override
    public int size() {
        return 1;
    }
}