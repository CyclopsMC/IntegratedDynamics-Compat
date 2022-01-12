package org.cyclops.integrateddynamicscompat.modcompat.mcmultipart;

import mcmultipart.block.BlockEntityMultipartContainer;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.PartSlot;
import mcmultipart.raytrace.RayTraceUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.core.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.cyclops.integrateddynamics.api.network.INetwork;
import org.cyclops.integrateddynamics.api.part.IPartState;
import org.cyclops.integrateddynamics.api.part.IPartType;
import org.cyclops.integrateddynamics.capability.partcontainer.PartContainerDefault;

import javax.annotation.Nullable;

/**
 * Part container for a {@link PartCable}.
 * @author rubensworks
 */
public class PartContainerPartCable extends PartContainerDefault {

    private final PartCable partCable;

    public PartContainerPartCable(PartCable partCable) {
        this.partCable = partCable;
    }

    protected PartPartType getPartPart(Direction side) {
        IMultipartContainer container = partCable.getContainer();
        if(container != null) {
            IMultipart multipart = container.getPartInSlot(PartSlot.getFaceSlot(side));
            if (multipart instanceof PartPartType) {
                return (PartPartType) multipart;
            }
        }
        return null;
    }

    @Override
    public <P extends IPartType<P, S>, S extends IPartState<P>> boolean canAddPart(Direction side, IPartType<P, S> part) {
        return super.canAddPart(side, part) && MultipartHelper.canAddPart(getWorld(), getPos(), new PartPartType(side, part));
    }

    @Override
    public <P extends IPartType<P, S>, S extends IPartState<P>> void setPart(final Direction side, IPartType<P, S> part, IPartState<P> partState) {
        final PartPartType partPart = new PartPartType(side, part);
        super.setPart(side, part, partState);
        MultipartHelper.addPart(getWorld(), getPos(), partPart);
    }

    @Override
    public IPartType removePart(Direction side, PlayerEntity player, boolean dropMainElement) {
        PartPartType partPartType = getPartPart(side);
        IPartType removed = super.removePart(side, player, dropMainElement);
        if (removed != null && partPartType != null) {
            partCable.getContainer().removePart(partPartType);
        }
        return removed;
    }

    @Override
    protected void markDirty() {
        partCable.markDirty();
    }

    @Override
    protected void sendUpdate() {
        partCable.sendUpdate();
    }

    @Override
    protected World getWorld() {
        return partCable.getWorld();
    }

    @Override
    protected BlockPos getPos() {
        return partCable.getPos();
    }

    @Override
    protected INetwork getNetwork() {
        return partCable.getNetwork();
    }

    @Nullable
    @Override
    public Direction getWatchingSide(World world, BlockPos pos, PlayerEntity player) {
        Vector3d start = RayTraceUtils.getStart(player);
        Vector3d end = RayTraceUtils.getEnd(player);
        RayTraceUtils.AdvancedRayTraceResultPart result = ((BlockEntityMultipartContainer) world.getBlockEntityEntity(pos)).getPartContainer().collisionRayTrace(start, end);
        if(result == null || result.hit == null) return null;
        IMultipart multipart = result.hit.partHit;
        if(!(multipart instanceof PartPartType)) return null;
        PartPartType partPartType = (PartPartType) result.hit.partHit;
        return partPartType != null ? partPartType.getFacing() : null;
    }

}
