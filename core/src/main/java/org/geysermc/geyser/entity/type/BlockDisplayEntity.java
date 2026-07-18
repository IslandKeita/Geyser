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

package org.geysermc.geyser.entity.type;

import lombok.Getter;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.geysermc.geyser.entity.DisplayBedrockEntityDefinitions;
import org.geysermc.geyser.entity.DisplayBlockModelResolver;
import org.geysermc.geyser.entity.spawn.EntitySpawnContext;
import org.geysermc.geyser.level.block.type.BlockState;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.IntEntityMetadata;

@Getter
public class BlockDisplayEntity extends DisplayBaseEntity {
    private BlockDefinition block;

    public BlockDisplayEntity(EntitySpawnContext context) {
        super(context);
    }

    public void setBlock(IntEntityMetadata entityMetadata) {
        int javaBlockState = entityMetadata.getPrimitiveValue();
        this.block = session.getBlockMappings().getBedrockBlock(javaBlockState);
        this.metadata.put(EntityDataTypes.BLOCK, block);

        BlockState state = BlockState.of(javaBlockState);
        Integer model = DisplayBlockModelResolver.resolve(state);
        if (model == null) {
            model = 0;
            warnUnsupported("BlockDisplay model " + state);
        }
        propertyManager.addProperty(DisplayBedrockEntityDefinitions.BLOCK_MODEL, model);
    }
}
