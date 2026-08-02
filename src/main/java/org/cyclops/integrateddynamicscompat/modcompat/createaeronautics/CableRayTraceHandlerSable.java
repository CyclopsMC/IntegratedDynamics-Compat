package org.cyclops.integrateddynamicscompat.modcompat.createaeronautics;

import dev.ryanhcode.sable.companion.SableCompanion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.function.TriFunction;
import org.cyclops.integrateddynamics.api.block.cable.ICableRayTraceHandler;
import org.cyclops.integrateddynamics.core.block.BlockRayTraceResultComponent;
import org.jetbrains.annotations.Nullable;

/**
 * Handles ray tracing for sub-levels in Create Aeronautics by projecting sub-level coordinates out into the main level.
 * @author rubensworks
 */
public class CableRayTraceHandlerSable implements ICableRayTraceHandler {
    @Override
    public boolean canHandle(BlockPos pos, @Nullable Entity entity) {
        return entity != null && SableCompanion.INSTANCE.isInPlotGrid(entity.level(), pos);
    }

    @Override
    public BlockRayTraceResultComponent rayTrace(BlockPos pos, @Nullable Entity entity, TriFunction<BlockPos, Entity, Vec3, BlockRayTraceResultComponent> parentRayTracer) {
        Vec3 realPos = SableCompanion.INSTANCE.projectOutOfSubLevel(entity.level(), Vec3.atLowerCornerOf(pos));
        return parentRayTracer.apply(pos, entity, realPos);
    }
}
