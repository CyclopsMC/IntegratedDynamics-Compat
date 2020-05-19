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
import org.cyclops.integrateddynamics.core.helper.L10NValues;
import org.cyclops.integrateddynamics.tileentity.TileProxy;

import java.util.List;

/**
 * Waila data provider for proxies.
 * @author rubensworks
 *
 */
public class ProxyDataProvider implements IWailaDataProvider {

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
            TileProxy tile = TileHelpers.getSafeTile(accessor.getWorld(), accessor.getPosition(), TileProxy.class);
            if (tile != null) {
                currenttip.add(L10NHelpers.localize(L10NValues.GENERAL_ITEM_ID, tile.getProxyId()));
            }
        }
        return currenttip;
    }

    @Override
    public CompoundNBT getNBTData(ServerPlayerEntity player, TileEntity te, CompoundNBT tag, World world, BlockPos pos) {
        return tag;
    }

}
