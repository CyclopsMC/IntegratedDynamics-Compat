package org.cyclops.integrateddynamicscompat.modcompat.rftools;

import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValue;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueType;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;
import org.cyclops.integrateddynamicscompat.Reference;

import io.netty.buffer.ByteBuf;
import mcjty.lib.network.NetworkTools;
import mcjty.rftools.api.screens.data.IModuleData;

public class ModuleDataValue implements IModuleData {
	public final IValueType valueType;
	public final IValue materializedValue;
	public final String errorMessage;

	public ModuleDataValue(IValue value) {
		IValue mv = null;
		try {
			mv = value.getType().materialize(value);
		} catch (EvaluationException e) {
			valueType = null;
			materializedValue = null;
			errorMessage = e.getMessage();
			return;
		}
		valueType = mv.getType();
		materializedValue = mv;
		errorMessage = null;
	}

	public ModuleDataValue(String err) {
		valueType = null;
		materializedValue = null;
		errorMessage = err;
	}

	public ModuleDataValue(ByteBuf buf) {
		if(buf.readBoolean()) {
			valueType = ValueTypes.REGISTRY.getValueType(NetworkTools.readString(buf));
			materializedValue = valueType.deserialize(NetworkTools.readString(buf));
			errorMessage = null;
		} else {
			valueType = null;
			materializedValue = null;
			errorMessage = NetworkTools.readString(buf);
		}
	}
	
	public static final String ID = Reference.MOD_ID + ":module_data_value";

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void writeToBuf(ByteBuf buf) {
		if(materializedValue != null) {
			buf.writeBoolean(true);
			NetworkTools.writeString(buf, valueType.getUnlocalizedName());
			NetworkTools.writeString(buf, valueType.serialize(materializedValue));
		} else {
			buf.writeBoolean(false);
			NetworkTools.writeString(buf, errorMessage);
		}
	}

}
