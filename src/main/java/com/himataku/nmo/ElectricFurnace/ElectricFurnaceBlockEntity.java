package com.himataku.nmo.ElectricFurnace;

import com.himataku.nmo.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ElectricFurnaceBlockEntity extends BlockEntity {

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    public static final int INVENTORY_SIZE = 2;

    public static final int MAX_ENERGY = 100_000;
    public static final int MAX_RECEIVE = 10_000;

    // 1回の精錬に必要な総電力
    public static final int ENERGY_PER_OPERATION = 20_000;

    // 精錬時間
    public static final int PROCESS_TIME = 100;

    // 精錬中に1Tickで消費する電力
    public static final int ENERGY_PER_TICK =
            ENERGY_PER_OPERATION / PROCESS_TIME;

    private int progress = 0;

    // =========================================================
    // アイテムHandler
    //
    // Crusherと同じ方式
    //
    // Slot 0 = Input
    // Slot 1 = Output
    //
    // 外部から
    // insertItem()  → Inputのみ
    // extractItem() → Outputのみ
    // =========================================================

    private final ItemStackHandler itemHandler =
            new ItemStackHandler(INVENTORY_SIZE) {

                @Override
                protected void onContentsChanged(int slot) {
                    setChanged();
                }

                @Override
                public boolean isItemValid(
                        int slot,
                        ItemStack stack
                ) {

                    // Inputにだけアイテムを入れられる
                    return slot == INPUT_SLOT;
                }
            };

    // =========================================================
    // 外部からのアイテム入出力
    //
    // Crusherと同じ考え方
    //
    // Input:
    //   外部から投入可能
    //   外部への搬出不可
    //
    // Output:
    //   外部から投入不可
    //   外部へ搬出可能
    // =========================================================

    private final IItemHandler externalItemHandler =
            new IItemHandler() {

                @Override
                public int getSlots() {
                    return INVENTORY_SIZE;
                }

                @Override
                public ItemStack getStackInSlot(
                        int slot
                ) {

                    if (slot < 0
                            || slot >= INVENTORY_SIZE) {

                        return ItemStack.EMPTY;
                    }

                    return itemHandler.getStackInSlot(slot);
                }

                // =================================================
                // 外部からアイテムを投入
                //
                // Inputだけ許可
                // =================================================

                @Override
                public ItemStack insertItem(
                        int slot,
                        ItemStack stack,
                        boolean simulate
                ) {

                    // Input以外には入れられない
                    if (slot != INPUT_SLOT) {
                        return stack;
                    }

                    if (stack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    ItemStack current =
                            itemHandler.getStackInSlot(
                                    INPUT_SLOT
                            );

                    // すでに別アイテムが入っている
                    if (!current.isEmpty()
                            && !ItemStack.isSameItemSameComponents(
                            current,
                            stack
                    )) {

                        return stack;
                    }

                    int maxStackSize =
                            Math.min(
                                    64,
                                    stack.getMaxStackSize()
                            );

                    int currentCount =
                            current.isEmpty()
                                    ? 0
                                    : current.getCount();

                    int space =
                            maxStackSize - currentCount;

                    if (space <= 0) {
                        return stack;
                    }

                    int insertAmount =
                            Math.min(
                                    stack.getCount(),
                                    space
                            );

                    if (!simulate) {

                        if (current.isEmpty()) {

                            ItemStack inserted =
                                    stack.copy();

                            inserted.setCount(
                                    insertAmount
                            );

                            itemHandler.setStackInSlot(
                                    INPUT_SLOT,
                                    inserted
                            );

                        } else {

                            current.grow(
                                    insertAmount
                            );

                            setChanged();
                        }
                    }

                    ItemStack remainder =
                            stack.copy();

                    remainder.shrink(
                            insertAmount
                    );

                    return remainder;
                }

                // =================================================
                // 外部からアイテムを取り出す
                //
                // Outputだけ許可
                // =================================================

                @Override
                public ItemStack extractItem(
                        int slot,
                        int amount,
                        boolean simulate
                ) {

                    // Output以外からは取り出せない
                    if (slot != OUTPUT_SLOT) {
                        return ItemStack.EMPTY;
                    }

                    if (amount <= 0) {
                        return ItemStack.EMPTY;
                    }

                    ItemStack current =
                            itemHandler.getStackInSlot(
                                    OUTPUT_SLOT
                            );

                    if (current.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    int extractAmount =
                            Math.min(
                                    amount,
                                    current.getCount()
                            );

                    ItemStack result =
                            current.copy();

                    result.setCount(
                            extractAmount
                    );

                    if (!simulate) {

                        current.shrink(
                                extractAmount
                        );

                        setChanged();
                    }

                    return result;
                }

                @Override
                public int getSlotLimit(
                        int slot
                ) {

                    return 64;
                }

                @Override
                public boolean isItemValid(
                        int slot,
                        ItemStack stack
                ) {

                    // 外部から入れられるのはInputだけ
                    return slot == INPUT_SLOT;
                }
            };

    // =========================================================
    // 電力
    // =========================================================

    private final EnergyStorage energyStorage =
            new EnergyStorage(
                    MAX_ENERGY,
                    MAX_RECEIVE,
                    ENERGY_PER_TICK
            );

    // =========================================================
    // コンストラクタ
    // =========================================================

    public ElectricFurnaceBlockEntity(
            BlockPos pos,
            BlockState state
    ) {

        super(
                ModBlockEntities.ELECTRIC_FURNACE.get(),
                pos,
                state
        );
    }

    // =========================================================
    // Tick処理
    // =========================================================

    public static void tick(
            Level level,
            BlockPos pos,
            BlockState state,
            ElectricFurnaceBlockEntity furnace
    ) {

        if (level.isClientSide()) {
            return;
        }

        ItemStack input =
                furnace.itemHandler.getStackInSlot(
                        INPUT_SLOT
                );

        ItemStack output =
                furnace.itemHandler.getStackInSlot(
                        OUTPUT_SLOT
                );

        // =====================================================
        // 入力がない
        // =====================================================

        if (input.isEmpty()) {

            furnace.progress = 0;
            furnace.setChanged();

            return;
        }

        // =====================================================
        // バニラのかまどレシピを検索
        // =====================================================

        SingleRecipeInput recipeInput =
                new SingleRecipeInput(input);

        var recipeOptional =
                level.getRecipeManager().getRecipeFor(
                        RecipeType.SMELTING,
                        recipeInput,
                        level
                );

        // かまどレシピがない
        if (recipeOptional.isEmpty()) {

            furnace.progress = 0;
            furnace.setChanged();

            return;
        }

        var recipe =
                recipeOptional.get().value();

        ItemStack result =
                recipe.assemble(
                        recipeInput,
                        level.registryAccess()
                );

        // =====================================================
        // 精錬結果がない
        // =====================================================

        if (result.isEmpty()) {

            furnace.progress = 0;
            furnace.setChanged();

            return;
        }

        // =====================================================
        // 出力スロットに入らない
        // =====================================================

        if (!canInsertResult(
                output,
                result
        )) {

            furnace.progress = 0;
            furnace.setChanged();

            return;
        }

        // =====================================================
        // 精錬中の電力確認
        // =====================================================

        if (furnace.energyStorage.getEnergyStored()
                < ENERGY_PER_TICK) {

            furnace.setChanged();

            return;
        }

        // =====================================================
        // 精錬中のみ200 FE消費
        // =====================================================

        int extracted =
                furnace.energyStorage.extractEnergy(
                        ENERGY_PER_TICK,
                        false
                );

        // 実際に200 FE消費できた場合のみ進行
        if (extracted < ENERGY_PER_TICK) {

            furnace.setChanged();

            return;
        }

        furnace.progress++;

        // =====================================================
        // 精錬完了
        // =====================================================

        if (furnace.progress >= PROCESS_TIME) {

            input.shrink(1);

            if (output.isEmpty()) {

                furnace.itemHandler.setStackInSlot(
                        OUTPUT_SLOT,
                        result.copy()
                );

            } else {

                output.grow(
                        result.getCount()
                );
            }

            furnace.progress = 0;
        }

        furnace.setChanged();
    }

    // =========================================================
    // 出力可能か
    // =========================================================

    private static boolean canInsertResult(
            ItemStack output,
            ItemStack result
    ) {

        if (output.isEmpty()) {
            return true;
        }

        if (!ItemStack.isSameItemSameComponents(
                output,
                result
        )) {

            return false;
        }

        return output.getCount()
                + result.getCount()
                <= output.getMaxStackSize();
    }

    // =========================================================
    // Getter
    // =========================================================

    public int getProgress() {
        return progress;
    }

    public int getProcessTime() {
        return PROCESS_TIME;
    }

    public int getEnergy() {
        return energyStorage.getEnergyStored();
    }

    public int getMaxEnergy() {
        return MAX_ENERGY;
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    // =========================================================
    // GUI用
    //
    // GUIからはInput / Outputを直接操作する
    // =========================================================

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    // =========================================================
    // 外部機械・ホッパー用
    //
    // insertItem()  → Input
    // extractItem() → Output
    // =========================================================

    public IItemHandler getExternalItemHandler() {
        return externalItemHandler;
    }

    // =========================================================
    // アイテムをドロップ
    // =========================================================

    public void dropContents() {

        if (level == null) {
            return;
        }

        for (
                int i = 0;
                i < itemHandler.getSlots();
                i++
        ) {

            ItemStack stack =
                    itemHandler.getStackInSlot(i);

            if (!stack.isEmpty()) {

                net.minecraft.world.Containers.dropItemStack(
                        level,
                        worldPosition.getX(),
                        worldPosition.getY(),
                        worldPosition.getZ(),
                        stack
                );
            }
        }
    }

    // =========================================================
    // 保存
    // =========================================================

    @Override
    protected void saveAdditional(
            CompoundTag tag,
            HolderLookup.Provider registries
    ) {

        super.saveAdditional(
                tag,
                registries
        );

        tag.put(
                "Inventory",
                itemHandler.serializeNBT(registries)
        );

        tag.putInt(
                "Energy",
                energyStorage.getEnergyStored()
        );

        tag.putInt(
                "Progress",
                progress
        );
    }

    // =========================================================
    // 読み込み
    // =========================================================

    @Override
    protected void loadAdditional(
            CompoundTag tag,
            HolderLookup.Provider registries
    ) {

        super.loadAdditional(
                tag,
                registries
        );

        itemHandler.deserializeNBT(
                registries,
                tag.getCompound("Inventory")
        );

        energyStorage.receiveEnergy(
                tag.getInt("Energy"),
                false
        );

        progress =
                tag.getInt("Progress");
    }
}