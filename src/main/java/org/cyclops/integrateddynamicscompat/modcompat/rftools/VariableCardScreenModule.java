package org.cyclops.integrateddynamicscompat.modcompat.rftools;

import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValue;
import org.cyclops.integrateddynamics.api.evaluate.variable.IVariable;
import org.cyclops.integrateddynamics.api.item.IVariableFacade;
import org.cyclops.integrateddynamics.api.network.INetwork;
import org.cyclops.integrateddynamics.capability.network.NetworkCarrierConfig;
import org.cyclops.integrateddynamics.core.helper.NetworkHelpers;
import org.cyclops.integrateddynamics.core.item.VariableFacadeHandlerRegistry;

import mcjty.rftools.api.screens.IScreenDataHelper;
import mcjty.rftools.api.screens.IScreenModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class VariableCardScreenModule implements IScreenModule<ModuleDataValue> {
	private TileEntity tile;
	private IVariableFacade facade;

	@Override
	public ModuleDataValue getData(IScreenDataHelper helper, World worldObj, long millis) {
		INetwork network = tile.getCapability(NetworkCarrierConfig.CAPABILITY, null).getNetwork();
		IVariable variable = facade.getVariable(NetworkHelpers.getPartNetwork(network));
		IValue value;
		try {
			value = variable.getValue();
		} catch(EvaluationException e) {
			return new ModuleDataValue(e.getMessage());
		}
		return new ModuleDataValue(value);
	}

	@Override
	public void setupFromNBT(NBTTagCompound tagCompound, int dim, BlockPos pos) {
		tile = DimensionManager.getWorld(dim).getTileEntity(pos);
		facade = VariableFacadeHandlerRegistry.getInstance().handle(tagCompound);
	}

	@Override
	public int getRfPerTick() {
		return 4; // TODO make this configurable
	}

	@Override
	public void mouseClick(World world, int x, int y, boolean clicked, EntityPlayer player) {
		// do nothing
	}

}
