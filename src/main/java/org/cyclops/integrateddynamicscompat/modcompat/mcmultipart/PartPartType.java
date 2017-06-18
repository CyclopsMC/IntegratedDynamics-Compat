package org.cyclops.integrateddynamicscompat.modcompat.mcmultipart;

import com.google.common.collect.Lists;
import mcmultipart.client.multipart.AdvancedParticleManager;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.PartSlot;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.integrateddynamics.api.network.INetwork;
import org.cyclops.integrateddynamics.api.part.IPartContainer;
import org.cyclops.integrateddynamics.api.part.IPartState;
import org.cyclops.integrateddynamics.api.part.IPartType;
import org.cyclops.integrateddynamics.api.part.PartTarget;
import org.cyclops.integrateddynamics.block.BlockCable;
import org.cyclops.integrateddynamics.core.helper.PartHelpers;
import org.cyclops.integrateddynamics.core.helper.WrenchHelpers;
import org.cyclops.integrateddynamics.item.ItemBlockCable;

import java.util.EnumSet;
import java.util.List;

/**
 * An McMultiPart part for a {@link IPartType}.
 * @author rubensworks
 */
public class PartPartType extends MultipartBase {

    private EnumFacing facing;
    private IPartType partType;

    public PartPartType() {

    }

    public PartPartType(EnumFacing facing, IPartType partType) {
        this();
        init(facing, partType);
    }

    public void init(EnumFacing facing, IPartType partType) {
        this.facing = facing;
        this.partType = partType;
    }

    public static ResourceLocation getType(IPartType partType) {
        return partType.getBlockModelPath();
    }

    @Override
    public ResourceLocation getType() {
        return getType(getPartType());
    }

    @Override
    protected ItemStack getItemStack() {
        return new ItemStack(getPartType().getItem());
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> drops = Lists.newLinkedList();
        IPartState partState = getDelegatedPartState();
        // partstate can be null if there is not cable in this block
        if(partState != null) {
            getPartType().addDrops(getPartTarget(), partState, drops, true);
        } else {
            drops.add(getItemStack());
        }
        return drops;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if(!tag.getKeySet().isEmpty()) {
            Pair<EnumFacing, IPartType> part = PartHelpers.readPartTypeFromNBT(getNetwork(), getPos(), tag.getCompoundTag("part"));
            if (part != null) {
                this.facing = part.getKey();
                this.partType = part.getValue();
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);
        NBTTagCompound partTag = new NBTTagCompound();
        PartHelpers.writePartTypeToNBT(partTag, getFacing(), getPartType());
        tag.setTag("part", partTag);
        return tag;
    }

    @Override
    public IBlockState getActualState(IBlockState state) {
        return getPartType().getBlockState(getPartContainer(), getFacing());
    }

    @Override
    public BlockStateContainer createBlockState() {
        return getPartType().getBaseBlockState();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return getPartType().getPartRenderPosition().getBoundingBox(getFacing());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(AdvancedParticleManager advancedParticleManager) {
        advancedParticleManager.addBlockDestroyEffects(getPos(), BlockCable.getInstance().texture);
        return true;
    }

    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> list) {
        list.add(getRenderBoundingBox());
    }

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> list) {
        list.add(getRenderBoundingBox());
    }

    @Override
    public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        AxisAlignedBB boundingBox = getRenderBoundingBox();
        if(mask.intersectsWith(boundingBox)) {
            list.add(boundingBox);
        }
    }

    @Override
    public boolean canRenderInLayer(BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT;
    }

    @Override
    public ResourceLocation getModelPath() {
        return getPartType().getBlockModelPath();
    }

    @Override
    public EnumSet<PartSlot> getSlotMask() {
        return EnumSet.of(PartSlot.getFaceSlot(getFacing()));
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        if (getPartCable() != null && getPartCable().hasPart(getFacing())) { // Can be false when breaking with hand.
            PartHelpers.removePart(getWorld(), getPos(), getFacing(), null, false, false);
        }
    }

    @Override
    public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack heldItem, PartMOP hit) {
        World world = player.worldObj;
        BlockPos pos = hit.getBlockPos();
        if(!world.isRemote && WrenchHelpers.isWrench(player, heldItem, world, pos, hit.sideHit)) {
            // Remove part from cable
            if(player.isSneaking()) {
                PartCable cable = getPartCable();
                if(cable == null) {
                    // In this case, the part won't be associated with a network at all,
                    // so it definitely won't have any additional drop, so just drop the main element.
                    if (!player.capabilities.isCreativeMode) {
                        ItemStackHelpers.spawnItemStackToPlayer(world, pos, getItemStack(), player);
                    }
                    getContainer().removePart(this);
                } else {
                    // In this case, this part is placed on a cable, so remove it like normal.
                    PartHelpers.removePart(world, pos, getFacing(), player, false, true);
                }
                ItemBlockCable.playBreakSound(world, pos, BlockCable.getInstance().getDefaultState());
            }
            return true;
        } else {
            IPartState partState = getDelegatedPartState();
            if(partState != null) {
                return getPartType().onPartActivated(getWorld(), getPos(), partState,
                        player, hand, heldItem, getFacing(), (float) hit.hitVec.xCoord, (float) hit.hitVec.yCoord, (float) hit.hitVec.zCoord)
                        || super.onActivated(player, hand, heldItem, hit);
            }
        }
        return false;
    }

    public EnumFacing getFacing() {
        return this.facing;
    }

    protected PartTarget getPartTarget() {
        return PartTarget.fromCenter(DimPos.of(getWorld(), getPos()), getFacing());
    }

    protected PartCable getPartCable() {
        IMultipartContainer multipartContainer = getContainer();
        if(multipartContainer != null) {
            IMultipart centerPart = multipartContainer.getPartInSlot(PartSlot.CENTER);
            if (centerPart instanceof PartCable) {
                return (PartCable) centerPart;
            }
        }
        return null;
    }

    public IPartContainer getPartContainer() {
        return PartHelpers.getPartContainer(getWorld(), getPos());
    }

    public IPartType getPartType() {
        return this.partType;
    }

    public IPartState getDelegatedPartState() {
        IPartContainer partContainer = getPartContainer();
        if(partContainer != null) {
            return partContainer.getPartState(getFacing());
        }
        return null;
    }

    public INetwork getNetwork() {
        PartCable partCable = getPartCable();
        if(partCable != null) {
            return partCable.getNetwork();
        }
        return null;
    }
}
