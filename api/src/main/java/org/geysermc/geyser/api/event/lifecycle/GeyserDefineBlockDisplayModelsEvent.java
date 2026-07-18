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
 */

package org.geysermc.geyser.api.event.lifecycle;

import java.util.Map;

import org.geysermc.event.Event;
import org.jetbrains.annotations.ApiStatus;

/**
 * Startup-only extension point for Java BlockDisplay state selectors.
 *
 * <p>Registrations select a model supplied by a prebuilt Bedrock resource pack.
 * They do not generate assets or allocate IDs at runtime.</p>
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface GeyserDefineBlockDisplayModelsEvent extends Event {
    /**
     * Registers an exact Java block-state selector, for example
     * {@code minecraft:note_block[instrument=harp,note=0,powered=false]}.
     *
     * @throws IllegalStateException if the selector has already been registered
     * @throws IllegalArgumentException if the input is invalid
     */
    void register(String javaBlockState, int modelId);

    /** Immutable selector to model-ID mappings currently registered. */
    Map<String, Integer> models();
}
