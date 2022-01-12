package org.cyclops.integrateddynamicscompat.network.packet;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.inventory.slot.SlotExtended;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerBase;

/**
 * @author rubensworks
 */
public class CPacketSetSlot extends PacketCodec {

    @CodecField
    private int windowId;
    @CodecField
    private int slot;
    @CodecField
    private ItemStack itemStack;

    public CPacketSetSlot() {

    }

    public CPacketSetSlot(int windowId, int slot, ItemStack itemStack) {
        this.windowId = windowId;
        this.slot = slot;
        this.itemStack = itemStack;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void actionClient(Level world, Player player) {

    }

    @Override
    public void actionServer(Level world, ServerPlayer player) {
        if(player.containerMenu instanceof ContainerLogicProgrammerBase
                && player.containerMenu.containerId == windowId
                && slot < player.containerMenu.slots.size()) {
            final Slot itemSlot = player.containerMenu.getSlot(slot);
            if (itemSlot instanceof SlotExtended && ((SlotExtended) itemSlot).isPhantom()) {
                player.containerMenu.setItem(slot, 0, itemStack.copy());
            }
        }
    }

}
