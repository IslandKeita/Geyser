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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DisplayQuaternionConverterTest {
    private static final float EPSILON = 0.001f;

    @Test
    void convertsIdentity() {
        assertVector(Vector3f.ZERO, DisplayQuaternionConverter.javaQuaternionToBedrockEuler(Quaternionf.IDENTITY));
    }

    @Test
    void convertsAxisRotations() {
        assertVector(Vector3f.from(90f, 0f, 0f), convert(Vector3f.UNIT_X));
        assertVector(Vector3f.from(0f, 90f, 0f), convert(Vector3f.UNIT_Y));
        assertVector(Vector3f.from(0f, 0f, 90f), convert(Vector3f.UNIT_Z));
    }

    @Test
    void normalizesQuaternion() {
        Quaternionf quaternion = Quaternionf.fromAngleDegAxis(90f, Vector3f.UNIT_X).mul(5f);
        assertVector(Vector3f.from(90f, 0f, 0f), DisplayQuaternionConverter.javaQuaternionToBedrockEuler(quaternion));
    }

    @Test
    void rejectsInvalidQuaternion() {
        assertVector(Vector3f.ZERO, DisplayQuaternionConverter.javaQuaternionToBedrockEuler(Quaternionf.ZERO));
        assertVector(Vector3f.ZERO, DisplayQuaternionConverter.javaQuaternionToBedrockEuler(
                Quaternionf.from(Float.NaN, 0f, 0f, 1f)));
    }

    private static Vector3f convert(Vector3f axis) {
        return DisplayQuaternionConverter.javaQuaternionToBedrockEuler(Quaternionf.fromAngleDegAxis(90f, axis));
    }

    private static void assertVector(Vector3f expected, Vector3f actual) {
        assertEquals(expected.getX(), actual.getX(), EPSILON);
        assertEquals(expected.getY(), actual.getY(), EPSILON);
        assertEquals(expected.getZ(), actual.getZ(), EPSILON);
    }
}

