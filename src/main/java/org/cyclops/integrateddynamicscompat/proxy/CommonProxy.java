package org.cyclops.integrateddynamicscompat.proxy;

import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.cyclopscore.network.IPacketHandler;
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
    public ModBaseNeoForge getMod() {
        return IntegratedDynamicsCompat._instance;
    }

    @Override
    public void registerPackets(IPacketHandler packetHandler) {
        super.registerPackets(packetHandler);

        // Register packets.
        packetHandler.register(CPacketSetSlot.class, CPacketSetSlot.ID, CPacketSetSlot.CODEC);
        packetHandler.register(CPacketValueTypeRecipeLPElementSetRecipe.class, CPacketValueTypeRecipeLPElementSetRecipe.ID, CPacketValueTypeRecipeLPElementSetRecipe.CODEC);

        IntegratedDynamicsCompat.clog("Registered packet handler.");
    }
}
