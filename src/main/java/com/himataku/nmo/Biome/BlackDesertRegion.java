package com.himataku.nmo.Biome;

import com.mojang.datafixers.util.Pair;

import java.util.function.Consumer;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;

import terrablender.api.Region;
import terrablender.api.RegionType;

public class BlackDesertRegion extends Region {

    private static final ResourceKey<Biome> BLACK_DESERT =
            ResourceKey.create(
                    Registries.BIOME,
                    ResourceLocation.fromNamespaceAndPath(
                            "nmo",
                            "black_desert"
                    )
            );

    public BlackDesertRegion() {
        super(
                ResourceLocation.fromNamespaceAndPath(
                        "nmo",
                        "black_desert_region"
                ),
                RegionType.OVERWORLD,
                450
        );
    }

    @Override
    public void addBiomes(
            Registry<Biome> registry,
            Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper
    ) {
        addModifiedVanillaOverworldBiomes(
                mapper,
                builder -> builder.replaceBiome(
                        Biomes.DESERT,
                        BLACK_DESERT
                )
        );
    }
}