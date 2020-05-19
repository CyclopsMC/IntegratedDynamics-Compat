package org.cyclops.integrateddynamicscompat.modcompat.mcmultipart;

import com.google.common.base.Optional;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.integrateddynamics.api.part.PartRenderPosition;
import org.cyclops.integrateddynamics.block.BlockCable;
import org.cyclops.integrateddynamics.client.model.CableModelBase;
import org.cyclops.integrateddynamics.client.model.IRenderState;

/**
 * A dynamic model for cables.
 * @author rubensworks
 */
public class PartCableModel extends CableModelBase {

    public PartCableModel(BlockState blockState, Direction facing, long rand) {
        super(blockState, facing, rand);
    }

    public PartCableModel(ItemStack itemStack, World world, LivingEntity entity) {
        super(itemStack, world, entity);
    }

    public PartCableModel() {
        super();
    }

    @Override
    protected boolean isRealCable() {
        return true;
    }

    @Override
    protected Optional<BlockState> getFacade() {
        return Optional.absent();
    }

    @Override
    protected boolean isConnected(Direction side) {
        return BlockHelpers.getSafeBlockStateProperty(getState(), BlockCable.CONNECTED[side.ordinal()], false);
    }

    @Override
    protected boolean hasPart(Direction side) {
        return getPartRenderPosition(side) != PartRenderPosition.NONE;
    }

    @Override
    protected PartRenderPosition getPartRenderPosition(Direction side) {
        return BlockHelpers.getSafeBlockStateProperty(getState(), BlockCable.PART_RENDERPOSITIONS[side.ordinal()], PartRenderPosition.NONE);
    }

    @Override
    protected boolean shouldRenderParts() {
        return false;
    }

    @Override
    protected IBakedModel getPartModel(Direction side) {
        return null;
    }

    @Override
    protected IRenderState getRenderState() {
        return BlockHelpers.getSafeBlockStateProperty(getState(), BlockCable.RENDERSTATE, null);
    }

    @Override
    public IBakedModel handleBlockState(BlockState state, Direction side, long rand) {
        return new PartCableModel(state, side, rand);
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack, World world, LivingEntity entity) {
        return new PartCableModel(stack, world, entity);
    }
}
