package org.cyclops.integrateddynamicscompat.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

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
        if(player.openContainer.windowId == windowId) {
            player.openContainer.putStackInSlot(slot, itemStack.copy());
        }
    }

}
