package org.cyclops.integrateddynamicscompat.modcompat.waila;

import com.google.common.collect.Lists;
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
import org.cyclops.cyclopscore.persist.nbt.NBTClassType;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.tileentity.BlockEntitySqueezer;

import java.util.List;

/**
 * Waila data provider for the squeezer.
 * @author rubensworks
 *
 */
public class SqueezerDataProvider implements IComponentProvider, IServerDataProvider<BlockEntityEntity> {

    public static final ResourceLocation ID = ResourceLocation.parse(org.cyclops.integrateddynamicscompat.Reference.MOD_ID, "squeezer");

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if(config.get(SqueezerDataProvider.ID)) {
            tooltip.addAll(NBTClassType.getClassType(List.class).readPersistedField("tooltip", accessor.getServerData()));
        }
    }

    @Override
    public void appendServerData(CompoundNBT tag, ServerPlayerEntity player, World world, BlockEntityEntity tileEntity) {
        BlockEntitySqueezer tile = (BlockEntitySqueezer) tileEntity;
        List<ITextComponent> tooltip = Lists.newArrayList();
        if (!tile.getInventory().getStackInSlot(0).isEmpty()) {
            tooltip.add(new TranslationTextComponent("gui." + Reference.MOD_ID + ".waila.item",
                    tile.getInventory().getStackInSlot(0).getDisplayName()));
        }
        if (!tile.getTank().isEmpty()) {
            tooltip.add(new TranslationTextComponent("gui." + Reference.MOD_ID + ".waila.fluid",
                    tile.getTank().getFluid().getDisplayName(), tile.getTank().getFluidAmount()));
        }
        NBTClassType.getClassType(List.class).writePersistedField("tooltip", tooltip, tag);
    }

}
