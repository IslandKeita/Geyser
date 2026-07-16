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

import org.geysermc.mcprotocollib.protocol.data.game.entity.type.EntityType;
import org.junit.jupiter.api.Test;

import static org.geysermc.geyser.scoreboard.network.util.GeyserMockContext.mockContext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DisplayEntityDefinitionsTest {
    @Test
    void registersBlockDisplay() {
        mockContext(() -> {
            assertTrue(VanillaEntities.BLOCK_DISPLAY.is(EntityType.BLOCK_DISPLAY));
            assertSame(DisplayBedrockEntityDefinitions.BLOCK_DISPLAY,
                    VanillaEntities.BLOCK_DISPLAY.defaultBedrockDefinition());
            assertFalse(VanillaEntities.BLOCK_DISPLAY.defaultBedrockDefinition().vanilla());
            assertEquals(24, VanillaEntities.BLOCK_DISPLAY.translators().size());
        });
    }

    @Test
    void registersItemDisplay() {
        mockContext(() -> {
            assertTrue(VanillaEntities.ITEM_DISPLAY.is(EntityType.ITEM_DISPLAY));
            assertSame(DisplayBedrockEntityDefinitions.ITEM_DISPLAY,
                    VanillaEntities.ITEM_DISPLAY.defaultBedrockDefinition());
            assertFalse(VanillaEntities.ITEM_DISPLAY.defaultBedrockDefinition().vanilla());
            assertEquals(25, VanillaEntities.ITEM_DISPLAY.translators().size());
        });
    }

    @Test
    void definesNineTransformPropertiesPerEntity() {
        mockContext(() -> {
            assertEquals(9, DisplayBedrockEntityDefinitions.BLOCK_DISPLAY.properties().size());
            assertEquals(9, DisplayBedrockEntityDefinitions.ITEM_DISPLAY.properties().size());
        });
    }
}
