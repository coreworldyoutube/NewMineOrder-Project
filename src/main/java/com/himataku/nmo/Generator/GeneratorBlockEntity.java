package com.himataku.nmo.Generator;

import com.himataku.nmo.ModBlockEntities;
import com.himataku.nmo.customblock.AllFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class GeneratorBlockEntity extends BlockEntity implements Container {

    public static final int WATER_CAPACITY = 10_000;
    public static final int STEAM_CAPACITY = 10_000;
    public static final int ENERGY_CAPACITY = 100_000;

    public static final int ENERGY_PER_TICK = 100;
    public static final int WATER_PER_TICK = 10;
    public static final int STEAM_PER_TICK = 10;

    private ItemStack fuelStack = ItemStack.EMPTY;

    private int burnTime = 0;
    private int maxBurnTime = 0;

    /*
     * =========================
     * 水タンク
     * =========================
     *
     * 水だけを受け入れる
     */
    private final FluidTank waterTank =
            new FluidTank(
                    WATER_CAPACITY,
                    fluidStack ->
                            fluidStack.getFluid() == net.minecraft.world.level.material.Fluids.WATER
            ) {

                @Override
                protected void onContentsChanged() {
                    setChanged();
                }
            };

    /*
     * =========================
     * Steamタンク
     * =========================
     *
     * Steamだけを受け入れる
     */
    private final FluidTank steamTank =
            new FluidTank(
                    STEAM_CAPACITY,
                    fluidStack ->
                            fluidStack.getFluid() == AllFluid.STEAM.get()
            ) {

                @Override
                protected void onContentsChanged() {
                    setChanged();
                }
            };

    /*
     * =========================
     * 外部Fluid Capability
     * =========================
     *
     * 0 = Water
     *     → INPUT ONLY
     *
     * 1 = Steam
     *     → OUTPUT ONLY
     */
    private final IFluidHandler fluidHandler =
            new IFluidHandler() {

                @Override
                public int getTanks() {
                    return 2;
                }

                @Override
                public FluidStack getFluidInTank(int tank) {
                    return switch (tank) {
                        case 0 -> waterTank.getFluid();
                        case 1 -> steamTank.getFluid();
                        default -> FluidStack.EMPTY;
                    };
                }

                @Override
                public int getTankCapacity(int tank) {
                    return switch (tank) {
                        case 0 -> WATER_CAPACITY;
                        case 1 -> STEAM_CAPACITY;
                        default -> 0;
                    };
                }

                @Override
                public boolean isFluidValid(int tank, FluidStack stack) {
                    return switch (tank) {
                        case 0 ->
                                stack.getFluid()
                                        == net.minecraft.world.level.material.Fluids.WATER;

                        case 1 ->
                                stack.getFluid()
                                        == AllFluid.STEAM.get();

                        default -> false;
                    };
                }

                @Override
                public int fill(
                        FluidStack resource,
                        FluidAction action
                ) {
                    /*
                     * 水 → INPUT
                     */
                    if (resource.getFluid()
                            == net.minecraft.world.level.material.Fluids.WATER) {

                        return waterTank.fill(
                                resource,
                                action
                        );
                    }

                    /*
                     * Steam → INPUT
                     */
                    if (resource.getFluid()
                            == AllFluid.STEAM.get()) {

                        return steamTank.fill(
                                resource,
                                action
                        );
                    }

                    return 0;
                }

                @Override
                public FluidStack drain(
                        FluidStack resource,
                        FluidAction action
                ) {
                    /*
                     * SteamだけOUTPUT可能
                     */
                    if (resource.getFluid()
                            == AllFluid.STEAM.get()) {

                        return steamTank.drain(
                                resource,
                                action
                        );
                    }

                    /*
                     * 水は外部へ出さない
                     */
                    return FluidStack.EMPTY;
                }

                @Override
                public FluidStack drain(
                        int maxDrain,
                        FluidAction action
                ) {
                    /*
                     * SteamだけOUTPUT可能
                     */
                    return steamTank.drain(
                            maxDrain,
                            action
                    );
                }
            };
    /*
     * =========================
     * Energy
     * =========================
     */
    private final EnergyStorage energyStorage =
            new EnergyStorage(
                    ENERGY_CAPACITY,
                    10_000,
                    10_000
            ) {

                @Override
                public int receiveEnergy(
                        int toReceive,
                        boolean simulate
                ) {
                    return super.receiveEnergy(
                            toReceive,
                            simulate
                    );
                }

                @Override
                public int extractEnergy(
                        int toExtract,
                        boolean simulate
                ) {
                    return super.extractEnergy(
                            toExtract,
                            simulate
                    );
                }
            };

    /*
     * =========================
     * ContainerData
     * =========================
     */
    private final ContainerData containerData =
            new ContainerData() {

                @Override
                public int get(int index) {
                    return switch (index) {
                        case 0 -> burnTime;
                        case 1 -> maxBurnTime;
                        case 2 -> waterTank.getFluidAmount();
                        case 3 -> steamTank.getFluidAmount();
                        case 4 -> energyStorage.getEnergyStored();
                        default -> 0;
                    };
                }

                @Override
                public void set(
                        int index,
                        int value
                ) {
                    switch (index) {
                        case 0 -> burnTime = value;
                        case 1 -> maxBurnTime = value;
                        default -> {
                        }
                    }
                }

                @Override
                public int getCount() {
                    return 5;
                }
            };

    public GeneratorBlockEntity(
            BlockPos pos,
            BlockState state
    ) {
        super(
                ModBlockEntities.GENERATOR.get(),
                pos,
                state
        );
    }

    /*
     * =========================
     * Tick
     * =========================
     */
    public static void tick(
            Level level,
            BlockPos pos,
            BlockState state,
            GeneratorBlockEntity generator
    ) {
        if (level.isClientSide()) {
            return;
        }

        generator.serverTick();
    }

    private void serverTick() {

        boolean changed = false;

        /*
         * =========================
         * 燃焼中
         * =========================
         */

        if (burnTime > 0) {

            /*
             * 水があり、
             * Steamタンクに空きがある場合、
             * 水をSteamに変換する。
             */
            if (waterTank.getFluidAmount() >= WATER_PER_TICK
                    && steamTank.getFluidAmount() + STEAM_PER_TICK <= STEAM_CAPACITY) {

                waterTank.drain(
                        WATER_PER_TICK,
                        IFluidHandler.FluidAction.EXECUTE
                );

                steamTank.fill(
                        new FluidStack(
                                AllFluid.STEAM.get(),
                                STEAM_PER_TICK
                        ),
                        IFluidHandler.FluidAction.EXECUTE
                );

                changed = true;
            }

            burnTime--;

            changed = true;
        }

        /*
         * =========================
         * 燃料をセット
         * =========================
         */

        if (burnTime <= 0) {
            burnTime = 0;

            if (!fuelStack.isEmpty()) {

                int fuelTime =
                        getFuelTime(fuelStack);

                if (fuelTime > 0) {

                    maxBurnTime = fuelTime;
                    burnTime = fuelTime;

                    fuelStack.shrink(1);

                    changed = true;
                }
            }
        }

        /*
         * =========================
         * Steam → FE
         * =========================
         */

        if (!level.hasNeighborSignal(worldPosition)
                && steamTank.getFluidAmount() >= STEAM_PER_TICK
                && energyStorage.getEnergyStored() < ENERGY_CAPACITY) {

            FluidStack steam =
                    steamTank.getFluid();

            /*
             * Steam以外では発電しない
             */
            if (steam.getFluid() == AllFluid.STEAM.get()) {

                steamTank.drain(
                        STEAM_PER_TICK,
                        IFluidHandler.FluidAction.EXECUTE
                );

                int generated =
                        energyStorage.receiveEnergy(
                                ENERGY_PER_TICK,
                                false
                        );

                if (generated > 0) {
                    changed = true;
                }
            }
        }

        if (changed) {
            setChanged();
        }
    }

    /*
     * =========================
     * 燃料の燃焼時間
     * =========================
     */

    private int getFuelTime(ItemStack stack) {

        if (stack.isEmpty()) {
            return 0;
        }

        return stack.getBurnTime(null) / 2;
    }

    /*
     * =========================
     * 燃料を入れられるか
     * =========================
     */

    public boolean canInsertFuel(ItemStack stack) {

        if (stack.isEmpty()) {
            return false;
        }

        return getFuelTime(stack) > 0;
    }

    /*
     * =========================
     * Container
     * =========================
     */

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return fuelStack.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {

        if (slot == 0) {
            return fuelStack;
        }

        return ItemStack.EMPTY;
    }

    /*
     * =========================
     * 燃料を取り出せるか
     * =========================
     *
     * 燃焼中は取り出し不可。
     * 燃焼していない場合のみ取り出せる。
     */
    private boolean canRemoveFuel() {
        return burnTime <= 0;
    }

    /*
     * =========================
     * 燃料を取り出す
     * =========================
     */

    @Override
    public ItemStack removeItem(
            int slot,
            int amount
    ) {
        if (slot != 0) {
            return ItemStack.EMPTY;
        }

        if (!canRemoveFuel()) {
            return ItemStack.EMPTY;
        }

        if (fuelStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int removeAmount =
                Math.min(
                        amount,
                        fuelStack.getCount()
                );

        ItemStack result =
                fuelStack.split(removeAmount);

        setChanged();

        return result;
    }

    /*
     * =========================
     * 燃料を直接取り出す
     * =========================
     */

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot != 0) {
            return ItemStack.EMPTY;
        }

        if (!canRemoveFuel()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = fuelStack;

        fuelStack = ItemStack.EMPTY;

        setChanged();

        return result;
    }

    /*
     * =========================
     * アイテムをセット
     * =========================
     */

    @Override
    public void setItem(
            int slot,
            ItemStack stack
    ) {
        if (slot != 0) {
            return;
        }

        /*
         * 燃料ではないアイテムは入れない
         */
        if (!stack.isEmpty()
                && !canInsertFuel(stack)) {
            return;
        }

        fuelStack = stack.copy();

        setChanged();
    }

    /*
     * =========================
     * Container有効判定
     * =========================
     */

    @Override
    public boolean stillValid(Player player) {

        if (level == null) {
            return false;
        }

        if (level.getBlockEntity(worldPosition) != this) {
            return false;
        }

        return player.distanceToSqr(
                worldPosition.getX() + 0.5,
                worldPosition.getY() + 0.5,
                worldPosition.getZ() + 0.5
        ) <= 64.0;
    }

    @Override
    public void clearContent() {
        fuelStack = ItemStack.EMPTY;
        setChanged();
    }

    /*
     * =========================
     * Getter
     * =========================
     */

    public FluidTank getWaterTank() {
        return waterTank;
    }

    public FluidTank getSteamTank() {
        return steamTank;
    }

    public IFluidHandler getFluidHandler() {
        return fluidHandler;
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public ContainerData getContainerData() {
        return containerData;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public int getMaxBurnTime() {
        return maxBurnTime;
    }

    public int getWaterAmount() {
        return waterTank.getFluidAmount();
    }

    public int getSteamAmount() {
        return steamTank.getFluidAmount();
    }

    public int getEnergy() {
        return energyStorage.getEnergyStored();
    }

    /*
     * =========================
     * 保存
     * =========================
     */

    @Override
    protected void saveAdditional(
            CompoundTag tag,
            HolderLookup.Provider registries
    ) {
        super.saveAdditional(
                tag,
                registries
        );

        if (!fuelStack.isEmpty()) {

            tag.put(
                    "Fuel",
                    fuelStack.save(registries)
            );
        }

        tag.putInt(
                "BurnTime",
                burnTime
        );

        tag.putInt(
                "MaxBurnTime",
                maxBurnTime
        );

        tag.put(
                "Water",
                waterTank.writeToNBT(
                        registries,
                        new CompoundTag()
                )
        );

        tag.put(
                "Steam",
                steamTank.writeToNBT(
                        registries,
                        new CompoundTag()
                )
        );

        tag.putInt(
                "Energy",
                energyStorage.getEnergyStored()
        );
    }

    /*
     * =========================
     * 読み込み
     * =========================
     */

    @Override
    protected void loadAdditional(
            CompoundTag tag,
            HolderLookup.Provider registries
    ) {
        super.loadAdditional(
                tag,
                registries
        );

        if (tag.contains("Fuel")) {

            fuelStack =
                    ItemStack.parse(
                            registries,
                            tag.getCompound("Fuel")
                    ).orElse(ItemStack.EMPTY);

        } else {

            fuelStack = ItemStack.EMPTY;
        }

        burnTime =
                tag.getInt("BurnTime");

        maxBurnTime =
                tag.getInt("MaxBurnTime");

        if (tag.contains("Water")) {

            waterTank.readFromNBT(
                    registries,
                    tag.getCompound("Water")
            );
        }

        if (tag.contains("Steam")) {

            steamTank.readFromNBT(
                    registries,
                    tag.getCompound("Steam")
            );
        }

        int energy =
                tag.getInt("Energy");

        energyStorage.receiveEnergy(
                energy,
                false
        );
    }

    /*
     * =========================
     * ブロック破壊時
     * =========================
     */

    public void dropContents() {

        if (level == null) {
            return;
        }

        if (!fuelStack.isEmpty()) {

            Containers.dropItemStack(
                    level,
                    worldPosition.getX(),
                    worldPosition.getY(),
                    worldPosition.getZ(),
                    fuelStack
            );

            fuelStack = ItemStack.EMPTY;
        }
    }
}