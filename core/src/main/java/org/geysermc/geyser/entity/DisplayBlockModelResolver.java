/*
 * Copyright (c) 2026 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 */

package org.geysermc.geyser.entity;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.geyser.level.block.type.BlockState;

/** Startup-frozen exact Java block-state to Bedrock BlockDisplay model resolver. */
public final class DisplayBlockModelResolver {
    private static final int MAX_MODEL_ID = 4095;
    private static final Map<String, Integer> MODELS = new LinkedHashMap<>();
    private static boolean frozen;

    private DisplayBlockModelResolver() {
    }

    public static void register(String javaBlockState, int modelId) {
        if (frozen) {
            throw new IllegalStateException("BlockDisplay model registry is already frozen.");
        }
        if (javaBlockState == null || javaBlockState.isBlank() || !javaBlockState.contains(":")) {
            throw new IllegalArgumentException("BlockDisplay Java block-state selector is invalid: " + javaBlockState);
        }
        if (modelId < 0 || modelId > MAX_MODEL_ID) {
            throw new IllegalArgumentException("BlockDisplay model ID must be in range 0.." + MAX_MODEL_ID + ": " + modelId);
        }
        Integer previous = MODELS.putIfAbsent(javaBlockState, modelId);
        if (previous != null) {
            throw new IllegalStateException("Duplicate BlockDisplay Java block-state selector: " + javaBlockState);
        }
    }

    public static @Nullable Integer resolve(BlockState blockState) {
        Objects.requireNonNull(blockState, "blockState");
        Integer exact = MODELS.get(blockState.toString());
        if (exact != null) {
            return exact;
        }
        DisplayBlockModel legacy = DisplayBlockModel.fromJavaIdentifier(blockState.block().javaIdentifier().toString());
        return legacy == null ? null : legacy.modelId();
    }

    public static Map<String, Integer> models() {
        return Collections.unmodifiableMap(MODELS);
    }

    public static void freeze() {
        frozen = true;
    }

    public static int maxModelId() {
        return MAX_MODEL_ID;
    }
}
