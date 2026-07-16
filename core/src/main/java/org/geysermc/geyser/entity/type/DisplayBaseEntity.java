/*
 * Copyright (c) 2024 GeyserMC. http://geysermc.org
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

package org.geysermc.geyser.entity.type;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.cloudburstmc.math.imaginary.Quaternionf;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.geysermc.geyser.entity.DisplayBedrockEntityDefinitions;
import org.geysermc.geyser.entity.spawn.EntitySpawnContext;
import org.geysermc.geyser.util.DisplayCoordinateConverter;
import org.geysermc.geyser.util.DisplayQuaternionConverter;
import org.geysermc.geyser.util.EntityUtils;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.EntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.ByteEntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.IntEntityMetadata;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DisplayBaseEntity extends Entity {
    private static final Set<String> WARNED_UNSUPPORTED_PROPERTIES = ConcurrentHashMap.newKeySet();

    private @NonNull Vector3f baseTranslation = Vector3f.ZERO;
    private @NonNull Vector3f displayScale = Vector3f.ONE;
    private @NonNull Quaternionf leftRotation = Quaternionf.IDENTITY;
    private @NonNull Quaternionf rightRotation = Quaternionf.IDENTITY;
    private int interpolationDelay;
    private int transformationInterpolationDuration;
    private int positionRotationInterpolationDuration;
    private byte billboardConstraints;
    private int brightnessOverride = -1;
    private float viewRange = 1f;
    private float shadowRadius;
    private float shadowStrength = 1f;
    private float displayWidth;
    private float displayHeight;
    private int glowColorOverride = -1;

    public DisplayBaseEntity(EntitySpawnContext context) {
        super(context);
    }

    @Override
    protected void initializeMetadata() {
        super.initializeMetadata();
        this.metadata.put(EntityDataTypes.HITBOX, NbtMap.EMPTY);
        this.metadata.put(EntityDataTypes.WIDTH, 0f);
        this.metadata.put(EntityDataTypes.HEIGHT, 0f);
        setFlag(EntityFlag.HAS_COLLISION, false);
        setFlag(EntityFlag.HAS_GRAVITY, false);
    }

    public void setInterpolationDelay(IntEntityMetadata entityMetadata) {
        this.interpolationDelay = entityMetadata.getPrimitiveValue();
        warnUnsupported("interpolation delay");
    }

    public void setTransformationInterpolationDuration(IntEntityMetadata entityMetadata) {
        this.transformationInterpolationDuration = entityMetadata.getPrimitiveValue();
        warnUnsupported("transformation interpolation duration");
    }

    public void setPositionRotationInterpolationDuration(IntEntityMetadata entityMetadata) {
        this.positionRotationInterpolationDuration = entityMetadata.getPrimitiveValue();
        warnUnsupported("position/rotation interpolation duration");
    }

    public void setTranslation(EntityMetadata<Vector3f, ?> translationMeta) {
        Vector3f oldTranslation = this.baseTranslation;
        this.baseTranslation = DisplayCoordinateConverter.javaTranslationToBedrock(
                translationMeta.getValue() == null ? Vector3f.ZERO : translationMeta.getValue());

        // If translations are the same, don't update
        if (Objects.equals(oldTranslation, this.baseTranslation)) {
            return;
        }

        if (this.vehicle == null) {
            this.setRiderSeatPosition(this.baseTranslation);
            this.moveAbsoluteRaw(position, yaw, pitch, headYaw, onGround, true);
        } else {
            EntityUtils.updateMountOffset(this, this.vehicle, true, true, 0, 1);
            this.updateBedrockMetadata();
        }
    }

    public Vector3f getTranslation() {
        return baseTranslation;
    }

    public void setDisplayScale(EntityMetadata<Vector3f, ?> scaleMeta) {
        this.displayScale = scaleMeta.getValue() == null ? Vector3f.ONE : scaleMeta.getValue();
        if (propertyManager != null) {
            propertyManager.addProperty(DisplayBedrockEntityDefinitions.SCALE_X, displayScaleComponent(displayScale.getX()));
            propertyManager.addProperty(DisplayBedrockEntityDefinitions.SCALE_Y, displayScaleComponent(displayScale.getY()));
            propertyManager.addProperty(DisplayBedrockEntityDefinitions.SCALE_Z, displayScaleComponent(displayScale.getZ()));
        }
    }

    public void setLeftRotation(EntityMetadata<Quaternionf, ?> rotationMeta) {
        this.leftRotation = rotationMeta.getValue() == null ? Quaternionf.IDENTITY : rotationMeta.getValue();
        applyRotation(leftRotation, true);
    }

    public void setRightRotation(EntityMetadata<Quaternionf, ?> rotationMeta) {
        this.rightRotation = rotationMeta.getValue() == null ? Quaternionf.IDENTITY : rotationMeta.getValue();
        applyRotation(rightRotation, false);
    }

    private void applyRotation(Quaternionf rotation, boolean left) {
        if (propertyManager == null) {
            return;
        }
        Vector3f euler = DisplayQuaternionConverter.javaQuaternionToBedrockEuler(rotation);
        if (left) {
            propertyManager.addProperty(DisplayBedrockEntityDefinitions.LEFT_ROTATION_X, euler.getX());
            propertyManager.addProperty(DisplayBedrockEntityDefinitions.LEFT_ROTATION_Y, euler.getY());
            propertyManager.addProperty(DisplayBedrockEntityDefinitions.LEFT_ROTATION_Z, euler.getZ());
        } else {
            propertyManager.addProperty(DisplayBedrockEntityDefinitions.RIGHT_ROTATION_X, euler.getX());
            propertyManager.addProperty(DisplayBedrockEntityDefinitions.RIGHT_ROTATION_Y, euler.getY());
            propertyManager.addProperty(DisplayBedrockEntityDefinitions.RIGHT_ROTATION_Z, euler.getZ());
        }
    }

    public void setBillboardConstraints(ByteEntityMetadata entityMetadata) {
        this.billboardConstraints = entityMetadata.getPrimitiveValue();
        warnUnsupported("billboard constraints");
    }

    public void setBrightnessOverride(IntEntityMetadata entityMetadata) {
        this.brightnessOverride = entityMetadata.getPrimitiveValue();
        warnUnsupported("brightness override");
    }

    public void setViewRange(EntityMetadata<Float, ?> entityMetadata) {
        this.viewRange = entityMetadata.getValue();
        warnUnsupported("view range");
    }

    public void setShadowRadius(EntityMetadata<Float, ?> entityMetadata) {
        this.shadowRadius = entityMetadata.getValue();
        warnUnsupported("shadow radius");
    }

    public void setShadowStrength(EntityMetadata<Float, ?> entityMetadata) {
        this.shadowStrength = entityMetadata.getValue();
        warnUnsupported("shadow strength");
    }

    public void setDisplayWidth(EntityMetadata<Float, ?> entityMetadata) {
        this.displayWidth = Math.max(0f, finiteOr(entityMetadata.getValue(), 0f));
        this.metadata.put(EntityDataTypes.WIDTH, displayWidth);
    }

    public void setDisplayHeight(EntityMetadata<Float, ?> entityMetadata) {
        this.displayHeight = Math.max(0f, finiteOr(entityMetadata.getValue(), 0f));
        this.metadata.put(EntityDataTypes.HEIGHT, displayHeight);
    }

    public void setGlowColorOverride(IntEntityMetadata entityMetadata) {
        this.glowColorOverride = entityMetadata.getPrimitiveValue();
        warnUnsupported("glow color override");
    }

    protected final void warnUnsupported(String property) {
        if (WARNED_UNSUPPORTED_PROPERTIES.add(property)) {
            session.getGeyser().getLogger().warning("Java Display Entity " + property
                    + " is retained but not rendered by the minimum Bedrock representation");
        }
    }

    private static float finiteOr(float value, float fallback) {
        return Float.isFinite(value) ? value : fallback;
    }

    private static float displayScaleComponent(float value) {
        return Math.clamp(finiteOr(value, 1f), -64f, 64f);
    }

    @Override
    public Vector3f bedrockPosition() {
        return super.bedrockPosition().add(baseTranslation);
    }
}
