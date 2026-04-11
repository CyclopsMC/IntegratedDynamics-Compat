package org.cyclops.integrateddynamicscompat.modcompat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import org.cyclops.integrateddynamics.blockentity.BlockEntityProxy;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

/**
 * Waila data provider for proxies.
 * @author rubensworks
 *
 */
public class ProxyDataProviderServer implements IServerDataProvider<BlockAccessor> {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(org.cyclops.integrateddynamicscompat.Reference.MOD_ID, "proxy");

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        BlockEntityProxy tile = (BlockEntityProxy) accessor.getBlockEntity();
        tag.putInt("id", tile.getProxyId());
    }

    @Override
    public Identifier getUid() {
        return ID;
    }

}
