package org.cyclops.integrateddynamicscompat.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
    @SideOnly(Side.CLIENT)
    public void actionClient(World world, EntityPlayer player) {

    }

    @Override
    public void actionServer(World world, EntityPlayerMP player) {
        if(player.openContainer instanceof ContainerLogicProgrammerBase && player.openContainer.windowId == windowId) {
            if (player.openContainer.inventorySlots.size() <= slot) return;
            final Slot itemSlot = player.openContainer.getSlot(slot);

            if (itemSlot instanceof SlotExtended && ((SlotExtended) itemSlot).isPhantom()) itemSlot.putStack(itemStack.copy());
        }
    }

}
