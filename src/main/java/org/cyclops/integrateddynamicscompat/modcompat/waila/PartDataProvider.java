package org.cyclops.integrateddynamicscompat.modcompat.waila;

import com.google.common.collect.Lists;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.BlockEntityEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.persist.nbt.NBTClassType;
import org.cyclops.integrateddynamics.api.part.IPartState;
import org.cyclops.integrateddynamics.api.part.IPartType;
import org.cyclops.integrateddynamics.core.helper.PartHelpers;
import org.cyclops.integrateddynamics.core.tileentity.BlockEntityMultipartTicking;
import org.cyclops.integrateddynamicscompat.Reference;

import java.util.List;

/**
 * Waila data provider for parts.
 * @author rubensworks
 *
 */
public class PartDataProvider implements IComponentProvider, IServerDataProvider<BlockEntityEntity> {

    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "part");

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if(config.get(PartDataProvider.ID)) {
            tooltip.addAll(NBTClassType.getClassType(List.class).readPersistedField("tooltip", accessor.getServerData()));
        }
    }

    @Override
    public void appendServerData(CompoundNBT tag, ServerPlayerEntity player, World world, BlockEntityEntity tile) {
        PartHelpers.getPartContainer(world, tile.getPos(), null)
                .ifPresent(partContainer -> {
                    Direction side = partContainer.getWatchingSide(world, tile.getPos(), player);
                    if (side != null && partContainer.hasPart(side)) {
                        IPartType partType = partContainer.getPart(side);
                        IPartState partState = partContainer.getPartState(side);
                        List<ITextComponent> tooltip = Lists.newArrayList();
                        partType.loadTooltip(partState, tooltip);
                        NBTClassType.getClassType(List.class).writePersistedField("tooltip", tooltip, tag);
                    }
                });
    }
}
