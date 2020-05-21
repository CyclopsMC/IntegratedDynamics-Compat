package org.cyclops.integrateddynamicscompat.modcompat.waila;

import com.google.common.collect.Lists;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.persist.nbt.NBTClassType;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.tileentity.TileMechanicalSqueezer;

import java.util.List;

/**
 * Waila data provider for the mechanical squeezer.
 * @author rubensworks
 *
 */
public class MechanicalSqueezerDataProvider extends SqueezerDataProvider implements IComponentProvider, IServerDataProvider<TileEntity> {

    public static final ResourceLocation ID = new ResourceLocation(org.cyclops.integrateddynamicscompat.Reference.MOD_ID, "mechanical_squeezer");

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if(config.get(MechanicalSqueezerDataProvider.ID)) {
            tooltip.addAll(NBTClassType.getClassType(List.class).readPersistedField("tooltip", accessor.getServerData()));
        }
    }

    @Override
    public void appendServerData(CompoundNBT tag, ServerPlayerEntity player, World world, TileEntity tileEntity) {
        TileMechanicalSqueezer tile = (TileMechanicalSqueezer) tileEntity;
        List<ITextComponent> tooltip = Lists.newArrayList();
        tooltip.add(new TranslationTextComponent("gui." + Reference.MOD_ID + ".waila.energy",
                tile.getEnergyStored(), tile.getMaxEnergyStored()));
        if (!tile.getInventory().getStackInSlot(0).isEmpty()) {
            tooltip.add(new TranslationTextComponent("gui." + Reference.MOD_ID + ".waila.item.in",
                    tile.getInventory().getStackInSlot(0).getDisplayName()));
        }
        for (int i = 1; i < tile.getInventory().getSizeInventory(); i++) {
            if (!tile.getInventory().getStackInSlot(i).isEmpty()) {
                tooltip.add(new TranslationTextComponent("gui." + Reference.MOD_ID + ".waila.item.out",
                        tile.getInventory().getStackInSlot(i).getDisplayName()));
            }
        }
        if (!tile.getTank().isEmpty()) {
            tooltip.add(new TranslationTextComponent("gui." + Reference.MOD_ID + ".waila.fluid",
                    tile.getTank().getFluid().getDisplayName(), tile.getTank().getFluidAmount()));
        }
        if (tile.getProgress() > 0) {
            tooltip.add(new TranslationTextComponent("gui." + Reference.MOD_ID + ".waila.progress",
                    tile.getProgress() * 100 / tile.getMaxProgress()));
        }
        NBTClassType.getClassType(List.class).writePersistedField("tooltip", tooltip, tag);
    }

}
