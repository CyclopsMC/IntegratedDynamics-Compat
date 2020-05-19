package org.cyclops.integrateddynamicscompat.modcompat.waila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.tileentity.TileSqueezer;

import java.util.List;

/**
 * Waila data provider for the squeezer.
 * @author rubensworks
 *
 */
public class SqueezerDataProvider implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if(config.getConfig(org.cyclops.integrateddynamicscompat.modcompat.waila.Waila.getProxyConfigId())) {
            TileSqueezer tile = TileHelpers.getSafeTile(accessor.getWorld(), accessor.getPosition(), TileSqueezer.class);
            if (tile != null) {
                if(tile.getStackInSlot(0) != null) {
                    currenttip.add(L10NHelpers.localize("gui." + Reference.MOD_ID + ".waila.item",
                            tile.getStackInSlot(0).getDisplayName()));
                }
                if(!tile.getTank().isEmpty()) {
                    currenttip.add(L10NHelpers.localize("gui." + Reference.MOD_ID + ".waila.fluid",
                            tile.getTank().getFluid().getLocalizedName(), tile.getTank().getFluidAmount()));
                }
            }
        }
        return currenttip;
    }

    @Override
    public CompoundNBT getNBTData(ServerPlayerEntity player, TileEntity te, CompoundNBT tag, World world, BlockPos pos) {
        return tag;
    }

}
