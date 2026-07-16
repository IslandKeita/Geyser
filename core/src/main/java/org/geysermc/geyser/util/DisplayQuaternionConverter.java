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

package org.geysermc.geyser.util;

import org.cloudburstmc.math.imaginary.Quaternionf;
import org.cloudburstmc.math.vector.Vector3f;

public final class DisplayQuaternionConverter {
    private static final float MINIMUM_LENGTH_SQUARED = 1.0e-12f;

    private DisplayQuaternionConverter() {
    }

    /**
     * Converts a Java display quaternion into the X/Y/Z degree channels consumed by Bedrock bone animations.
     * A malformed quaternion falls back to identity rather than sending NaN entity properties.
     */
    public static Vector3f javaQuaternionToBedrockEuler(Quaternionf quaternion) {
        if (quaternion == null || !Float.isFinite(quaternion.getX()) || !Float.isFinite(quaternion.getY())
                || !Float.isFinite(quaternion.getZ()) || !Float.isFinite(quaternion.getW())
                || quaternion.lengthSquared() < MINIMUM_LENGTH_SQUARED) {
            return Vector3f.ZERO;
        }

        Vector3f angles = quaternion.normalize().getAxesAnglesDeg();
        if (!Float.isFinite(angles.getX()) || !Float.isFinite(angles.getY()) || !Float.isFinite(angles.getZ())) {
            return Vector3f.ZERO;
        }
        return angles;
    }
}
