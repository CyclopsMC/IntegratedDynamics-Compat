package org.cyclops.integrateddynamicscompat.modcompat.waila;

import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.BlockEntityEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.cyclops.integrateddynamics.core.helper.L10NValues;
import org.cyclops.integrateddynamics.tileentity.BlockEntityProxy;

import java.util.List;

/**
 * Waila data provider for proxies.
 * @author rubensworks
 *
 */
public class ProxyDataProvider implements IComponentProvider, IServerDataProvider<BlockEntityEntity> {

    public static final ResourceLocation ID = ResourceLocation.parse(org.cyclops.integrateddynamicscompat.Reference.MOD_ID, "proxy");

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if(config.get(ProxyDataProvider.ID)) {
            tooltip.add(new TranslationTextComponent(L10NValues.GENERAL_ITEM_ID, accessor.getServerData().getInt("id")));
        }
    }

    @Override
    public void appendServerData(CompoundNBT tag, ServerPlayerEntity player, World world, BlockEntityEntity tileEntity) {
        BlockEntityProxy tile = (BlockEntityProxy) tileEntity;
        tag.putInt("id", tile.getProxyId());
    }

}
