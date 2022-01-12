package org.cyclops.integrateddynamicscompat.modcompat.mcmultipart;

import com.google.common.base.Predicate;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.OcclusionHelper;
import mcmultipart.multipart.PartSlot;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.level.Level;
import org.cyclops.integrateddynamics.api.block.cable.ICable;
import org.cyclops.integrateddynamics.block.BlockCable;
import org.cyclops.integrateddynamics.capability.cable.CableDefault;
import org.cyclops.integrateddynamics.modcompat.mcmultipart.MultipartBase;

import javax.annotation.Nullable;

/**
 * Implementation of {@link ICable} for a {@link MultipartBase}.
 * @author rubensworks
 */
public abstract class CablePart<T extends MultipartBase> extends CableDefault {

    protected final T part;

    public CablePart(T part) {
        this.part = part;
    }

    @Override
    protected void markDirty() {
        part.markDirty();
    }

    @Override
    protected void sendUpdate() {
        part.sendUpdate();
    }

    @Override
    protected Level getWorld() {
        return part.getWorld();
    }

    @Override
    protected BlockPos getPos() {
        return part.getPos();
    }

    @Override
    public boolean canConnect(ICable connector, Direction side) {
        return super.canConnect(connector, side) && part.getContainer().getPartInSlot(PartSlot.getFaceSlot(side)) == null
                && OcclusionHelper.occlusionTest(OcclusionHelper.boxes(BlockCable.getInstance().getCableBoundingBox(side)), new Predicate<IMultipart>() {
            @Override
            public boolean apply(@Nullable IMultipart input) {
                return input == part;
            }
        }, part.getContainer().getParts());
    }
}
