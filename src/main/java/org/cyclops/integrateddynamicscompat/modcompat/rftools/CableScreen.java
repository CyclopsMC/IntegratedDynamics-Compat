package org.cyclops.integrateddynamicscompat.modcompat.rftools;

import org.cyclops.cyclopscore.datastructure.EnumFacingMap;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.cyclopscore.persist.nbt.NBTProviderComponent;
import org.cyclops.integrateddynamics.api.block.cable.ICable;
import org.cyclops.integrateddynamics.capability.cable.CableDefault;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import mcjty.rftools.blocks.screens.ScreenHitTileEntity;
import mcjty.rftools.blocks.screens.ScreenTileEntity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@RequiredArgsConstructor
public class CableScreen extends CableDefault implements INBTProvider {
	private final TileEntity tile;
	
	@Delegate
    private INBTProvider nbtProviderComponent = new NBTProviderComponent(this);

	@NBTPersist
	private EnumFacingMap<Boolean> connected = EnumFacingMap.newMap();

	private ScreenTileEntity getScreenTile() {
		if(tile instanceof ScreenTileEntity) return (ScreenTileEntity) tile;
		if(!(tile instanceof ScreenHitTileEntity)) return null;
		ScreenHitTileEntity screenHit = (ScreenHitTileEntity) tile;
		TileEntity screen = tile.getWorld().getTileEntity(tile.getPos().add(screenHit.getDx(), screenHit.getDy(), screenHit.getDz()));
		return screen instanceof ScreenTileEntity ? (ScreenTileEntity)screen : null;
	}

	@Override
	public boolean canConnect(ICable connector, EnumFacing side) {
		if(!super.canConnect(connector, side)) return false;
		ScreenTileEntity screenTile = getScreenTile();
		if(screenTile != null && connector instanceof CableScreen && screenTile == ((CableScreen)connector).getScreenTile())
			return true; // Different blocks of the same multiblock screen are allowed to connect to each other
		// Cables should only be able to connect to screens from the back
		switch(side) {
		case EAST:
			return tile.getBlockMetadata() == EnumFacing.WEST.ordinal();
		case WEST:
			return tile.getBlockMetadata() == EnumFacing.EAST.ordinal();
		case NORTH:
			return tile.getBlockMetadata() == EnumFacing.SOUTH.ordinal();
		default:
			// Screens in RFTools can never actually face up or down, but they can occasionally
			// be placed such that they face south but their metadata says they face up or down.
			return tile.getBlockMetadata() == EnumFacing.NORTH.ordinal();
		}
	}

	@Override
	public void destroy() {
		getWorld().setBlockState(getPos(), Blocks.AIR.getDefaultState(), 3);
	}

	@Override
	protected boolean isForceDisconnectable() {
		return false;
	}

	@Override
	protected EnumFacingMap<Boolean> getForceDisconnected() {
		return null;
	}

	private long lastUpdated = -1L;

	@Override
	protected EnumFacingMap<Boolean> getConnected() {
		if(lastUpdated < getWorld().getTotalWorldTime() && connected.isEmpty()) {
			lastUpdated = getWorld().getTotalWorldTime();
			updateConnections();
		}
		return connected;
	}

	@Override
	protected void markDirty() {
		tile.markDirty();
	}

	@Override
	protected void sendUpdate() {
		BlockHelpers.markForUpdate(tile.getWorld(), tile.getPos());
	}

	@Override
	protected World getWorld() {
		return tile.getWorld();
	}

	@Override
	protected BlockPos getPos() {
		return tile.getPos();
	}
}
