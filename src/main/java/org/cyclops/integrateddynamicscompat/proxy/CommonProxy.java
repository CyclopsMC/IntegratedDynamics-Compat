package org.cyclops.integrateddynamicscompat.proxy;

import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.network.PacketHandler;
import org.cyclops.cyclopscore.proxy.CommonProxyComponent;
import org.cyclops.integrateddynamicscompat.IntegratedDynamicsCompat;
import org.cyclops.integrateddynamicscompat.network.packet.CPacketSetSlot;
import org.cyclops.integrateddynamicscompat.network.packet.CPacketValueTypeRecipeLPElementSetRecipe;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxy extends CommonProxyComponent {

    @Override
    public ModBase getMod() {
        return IntegratedDynamicsCompat._instance;
    }

    @Override
    public void registerPacketHandlers(PacketHandler packetHandler) {
        super.registerPacketHandlers(packetHandler);

        // Register packets.
        packetHandler.register(CPacketSetSlot.ID, CPacketSetSlot.CODEC);
        packetHandler.register(CPacketValueTypeRecipeLPElementSetRecipe.ID, CPacketValueTypeRecipeLPElementSetRecipe.CODEC);

        IntegratedDynamicsCompat.clog("Registered packet handler.");
    }
}
