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
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/Geyser
 */

package org.geysermc.geyser.entity;

import org.geysermc.geyser.api.util.Identifier;
import org.geysermc.geyser.entity.properties.GeyserEntityProperties;
import org.geysermc.geyser.entity.properties.type.FloatProperty;
import org.geysermc.geyser.entity.properties.type.IntProperty;
import org.geysermc.geyser.registry.Registries;

/**
 * Built-in custom Bedrock entities used as the minimum representation of Java display entities.
 * The matching identifiers, properties, and bone hierarchy are provided by a static resource pack.
 */
public final class DisplayBedrockEntityDefinitions {
    public static final IntProperty INTERPOLATION_DURATION = intProperty("interpolation_duration", 0, 1200, 0);
    public static final IntProperty INTERPOLATION_DELAY = intProperty("interpolation_delay", -1200, 1200, 0);
    public static final IntProperty BLOCK_MODEL = intProperty("block_model", 0, DisplayBlockModelResolver.maxModelId(), 0);
    public static final IntProperty ITEM_MODEL = intProperty("item_model", 0, DisplayItemModel.maxModelId(), 0);
    public static final IntProperty ITEM_CONTEXT = intProperty("item_context", 0, 8, 0);
    public static final IntProperty ITEM_VISIBLE = intProperty("item_visible", 0, 1, 0);
    public static final FloatProperty ENTITY_YAW = rotationProperty("entity_yaw");
    public static final FloatProperty ENTITY_PITCH = rotationProperty("entity_pitch");
    public static final FloatProperty TRANSLATION_X = geometryPositionProperty("translation_x");
    public static final FloatProperty TRANSLATION_Y = geometryPositionProperty("translation_y");
    public static final FloatProperty TRANSLATION_Z = geometryPositionProperty("translation_z");
    public static final FloatProperty SCALE_X = floatProperty("scale_x", -64f, 64f, 1f);
    public static final FloatProperty SCALE_Y = floatProperty("scale_y", -64f, 64f, 1f);
    public static final FloatProperty SCALE_Z = floatProperty("scale_z", -64f, 64f, 1f);
    public static final FloatProperty LEFT_ROTATION_X = rotationProperty("left_rotation_x");
    public static final FloatProperty LEFT_ROTATION_Y = rotationProperty("left_rotation_y");
    public static final FloatProperty LEFT_ROTATION_Z = rotationProperty("left_rotation_z");
    public static final FloatProperty RIGHT_ROTATION_X = rotationProperty("right_rotation_x");
    public static final FloatProperty RIGHT_ROTATION_Y = rotationProperty("right_rotation_y");
    public static final FloatProperty RIGHT_ROTATION_Z = rotationProperty("right_rotation_z");

    public static final CustomBedrockEntityDefinition BLOCK_DISPLAY = register("unified_crossplay:block_display", true);
    public static final CustomBedrockEntityDefinition ITEM_DISPLAY = register("unified_crossplay:item_display", false);

    private DisplayBedrockEntityDefinitions() {
    }

    public static void init() {
        // no-op
    }

    private static FloatProperty floatProperty(String path, float min, float max, float defaultValue) {
        return new FloatProperty(Identifier.of("unified_crossplay", path), max, min, defaultValue);
    }

    private static IntProperty intProperty(String path, int min, int max, int defaultValue) {
        return new IntProperty(Identifier.of("unified_crossplay", path), max, min, defaultValue);
    }

    private static FloatProperty rotationProperty(String path) {
        return floatProperty(path, -360f, 360f, 0f);
    }

    private static FloatProperty geometryPositionProperty(String path) {
        return floatProperty(path, -1024f, 1024f, 0f);
    }

    private static CustomBedrockEntityDefinition register(String identifier, boolean blockDisplay) {
        GeyserEntityProperties.Builder propertiesBuilder = new GeyserEntityProperties.Builder(identifier)
                .add(INTERPOLATION_DURATION).add(INTERPOLATION_DELAY)
                .add(ENTITY_YAW).add(ENTITY_PITCH)
                .add(TRANSLATION_X).add(TRANSLATION_Y).add(TRANSLATION_Z)
                .add(SCALE_X).add(SCALE_Y).add(SCALE_Z)
                .add(LEFT_ROTATION_X).add(LEFT_ROTATION_Y).add(LEFT_ROTATION_Z)
                .add(RIGHT_ROTATION_X).add(RIGHT_ROTATION_Y).add(RIGHT_ROTATION_Z);
        if (blockDisplay) {
            propertiesBuilder.add(BLOCK_MODEL);
        } else {
            propertiesBuilder.add(ITEM_MODEL).add(ITEM_CONTEXT).add(ITEM_VISIBLE);
        }
        GeyserEntityProperties properties = propertiesBuilder.build();
        CustomBedrockEntityDefinition definition = new CustomBedrockEntityDefinition(Identifier.of(identifier), properties);
        Registries.BEDROCK_ENTITY_DEFINITIONS.register(definition.identifier(), definition);
        return definition;
    }
}
