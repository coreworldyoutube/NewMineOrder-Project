package com.himataku.nmo.chemnmo;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.himataku.nmo.NewMineOrder;
import com.smashingmods.chemlib.api.MatterState;
import com.smashingmods.chemlib.common.items.CompoundItem;
import com.smashingmods.chemlib.registry.ChemicalRegistry;
import com.smashingmods.chemlib.registry.ItemRegistry;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Objects;

public class ChemNmoRegistry {

    private static final JsonObject COMPOUNDS_JSON = loadJson();

    private static JsonObject loadJson() {
        InputStream stream = ChemNmoRegistry.class.getResourceAsStream(
                "/data/nmo/compounds.json"
        );

        Objects.requireNonNull(stream, "NMO compounds.json could not be found.");

        return JsonParser.parseReader(
                new BufferedReader(new InputStreamReader(stream))
        ).getAsJsonObject();
    }

    public static void register() {
        JsonArray compounds = COMPOUNDS_JSON.getAsJsonArray("compounds");

        for (JsonElement jsonElement : compounds) {
            JsonObject object = jsonElement.getAsJsonObject();

            String compoundName = object.get("name").getAsString();

            MatterState matterState = MatterState.valueOf(
                    object.get("matter_state")
                            .getAsString()
                            .toUpperCase(Locale.ROOT)
            );

            String description = object.has("description")
                    ? object.get("description").getAsString()
                    : "";

            String color = object.get("color").getAsString();

            JsonArray components = object.getAsJsonArray("components");

            HashMap<String, Integer> componentMap = new LinkedHashMap<>();

            for (JsonElement component : components) {
                JsonObject componentObject = component.getAsJsonObject();

                String componentName =
                        componentObject.get("name").getAsString();

                int count = componentObject.has("count")
                        ? componentObject.get("count").getAsInt()
                        : 1;

                componentMap.put(componentName, count);
            }

            ItemRegistry.REGISTRY_COMPOUNDS.register(
                    compoundName,
                    () -> new CompoundItem(
                            compoundName,
                            matterState,
                            componentMap,
                            description,
                            color,
                            ChemicalRegistry.mobEffectsFactory(object)
                    )
            );
        }
    }
}