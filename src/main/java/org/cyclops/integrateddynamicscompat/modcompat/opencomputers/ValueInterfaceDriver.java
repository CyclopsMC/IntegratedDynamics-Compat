package org.cyclops.integrateddynamicscompat.modcompat.opencomputers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.api.evaluate.IValueInterface;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValue;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueTypeListProxy;
import org.cyclops.integrateddynamics.capability.valueinterface.ValueInterfaceConfig;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeBlock.ValueBlock;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeEntity.ValueEntity;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeFluidStack.ValueFluidStack;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeItemStack.ValueItemStack;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeBoolean.ValueBoolean;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeDouble.ValueDouble;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeInteger.ValueInteger;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeList.ValueList;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeLong.ValueLong;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeNbt.ValueNbt;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeString.ValueString;

import com.google.common.collect.ImmutableMap;

import li.cil.oc.api.Network;
import li.cil.oc.api.driver.DriverBlock;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagLongArray;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class ValueInterfaceDriver implements DriverBlock {

	@Override
	public boolean worksWith(World world, BlockPos pos, EnumFacing side) {
		TileEntity te = world.getTileEntity(pos);
		return te != null && te.hasCapability(ValueInterfaceConfig.CAPABILITY, side);
	}

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing side) {
		return new ValueInterfaceManagedEnvironment(world, pos, side);
	}

	public static class ValueInterfaceManagedEnvironment extends AbstractManagedEnvironment implements NamedBlock {
		private final World world;
		private final BlockPos pos;
		private final EnumFacing side;
		
		public ValueInterfaceManagedEnvironment(World world, BlockPos pos, EnumFacing side) {
			this.world = world;
			this.pos = pos;
			this.side = side;
			setNode(Network.newNode(this, Visibility.Network).withComponent("value_interface").create());
		}
		
		private static final Field longArrayDataField = NBTTagLongArray.class.getDeclaredFields()[0];
		static {
			longArrayDataField.setAccessible(true);
		}
		
		private static Object convertNbtForLua(NBTBase nbt) {
			if(nbt == null) return null;
			int i;
			Map<Object, Object> map;
			switch(nbt.getId()) {
			case 0: // END
				return null;
			case 1: // BYTE
				return ((NBTTagByte)nbt).getByte();
			case 2: // SHORT
				return ((NBTTagShort)nbt).getShort();
			case 3: // INT
				return ((NBTTagInt)nbt).getInt();
			case 4: // LONG
				return ((NBTTagLong)nbt).getLong();
			case 5: // FLOAT
				return ((NBTTagFloat)nbt).getFloat();
			case 6: // DOUBLE
				return ((NBTTagDouble)nbt).getDouble();
			case 7: // BYTE[]
				return ((NBTTagByteArray)nbt).getByteArray();
			case 8: // STRING
				return ((NBTTagString)nbt).getString();
			case 9: // LIST
				NBTTagList list = (NBTTagList)nbt;
				map = new HashMap<>(list.tagCount() + 1);
				map.put("tagType", NBTBase.NBT_TYPES[list.getTagType()]);
				i = 0; // first one will actually be 1 because of preincrement
				for(NBTBase n : list) {
					map.put(++i, convertNbtForLua(n));
				}
				return map;
			case 10: // COMPOUND
				NBTTagCompound compound = (NBTTagCompound)nbt;
				map = new HashMap<>(compound.getSize());
				for(String key : compound.getKeySet()) {
					NBTBase tag = compound.getTag(key);
					map.put(key, ImmutableMap.of(1, NBTBase.NBT_TYPES[tag.getId()], 2, convertNbtForLua(compound.getTag(key))));
				}
				return map;
			case 11: // INT[]
				return ((NBTTagIntArray)nbt).getIntArray();
			case 12: // LONG[]
				try {
					return longArrayDataField.get(nbt);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
			return "UNKNOWN";
		}
		
		private static Object convertValueForLua(IValue value) {
			if(value instanceof ValueBoolean) {
				return ((ValueBoolean)value).getRawValue();
			} else if(value instanceof ValueDouble) {
				return ((ValueDouble)value).getRawValue();
			} else if(value instanceof ValueInteger) {
				return ((ValueInteger)value).getRawValue();
			} else if(value instanceof ValueLong) {
				return ((ValueLong)value).getRawValue();
			} else if(value instanceof ValueString) {
				return ((ValueString)value).getRawValue();
			} else if(value instanceof ValueList) {
				IValueTypeListProxy<?, ?> list = ((ValueList)value).getRawValue();
				if(!list.isInfinite()) {
					int len;
					try {
						len = list.getLength();
					} catch (EvaluationException e) {
						throw new RuntimeException(e);
					}
					Map<Object, Object> map = new HashMap<>(len + 1);
					map.put("valueType", list.getValueType().getTypeName());
					int i = 0; // first one will actually be 1 because of preincrement
					for(IValue v : list) {
						map.put(++i, ImmutableMap.of(1, v.getType().getTypeName(), 2, convertValueForLua(v)));
					}
					return map;
				}
			} else if(value instanceof ValueNbt) {
				return convertNbtForLua(((ValueNbt)value).getRawValue());
			} else if(value instanceof ValueFluidStack) {
				com.google.common.base.Optional<FluidStack> maybeFluidStack = ((ValueFluidStack)value).getRawValue();
				if(!maybeFluidStack.isPresent()) return null;
				FluidStack fluidStack = maybeFluidStack.get();
				return ImmutableMap.of("name", fluidStack.getFluid().getName(), "amount", fluidStack.amount, "tag", convertNbtForLua(fluidStack.tag));
			} else if(value instanceof ValueBlock) {
				com.google.common.base.Optional<IBlockState> maybeState = ((ValueBlock)value).getRawValue();
				if(!maybeState.isPresent()) return null;
				IBlockState state = maybeState.get();
				ImmutableMap<IProperty<?>, Comparable<?>> properties = state.getProperties();
				Map<Object, Object> luaProperties = new HashMap<>(properties.size());
				for(Map.Entry<IProperty<?>, Comparable<?>> entry : properties.entrySet()) {
					IProperty key = entry.getKey();
					luaProperties.put(key.getName(), key.getName(entry.getValue()));
				}
				return ImmutableMap.of("id", state.getBlock().getRegistryName().toString(), "properties", luaProperties);
			} else if(value instanceof ValueEntity) {
				com.google.common.base.Optional<UUID> uuid = ((ValueEntity)value).getUuid();
				if(!uuid.isPresent()) return null;
				return uuid.get().toString();
			} else if(value instanceof ValueItemStack) {
				ItemStack itemStack = ((ValueItemStack)value).getRawValue();
				return ImmutableMap.of("id", itemStack.getItem().getRegistryName(), "damage", itemStack.getItemDamage(), "count", itemStack.getCount(), "tag", convertNbtForLua(itemStack.getTagCompound()));
			}
			return value.getType().toCompactString(value); // TODO handle Operator and Aspect better
		}

		@Callback(doc = "function():string -- The Integrated Dynamics value being exposed.")
		public Object[] getValue(Context context, Arguments arguments) throws Exception {
			TileEntity te = world.getTileEntity(pos);
			if(te != null) {
				IValueInterface ivalueinterface = te.getCapability(ValueInterfaceConfig.CAPABILITY, side);
				if(ivalueinterface != null) {
					java.util.Optional<IValue> maybeValue = world.getTileEntity(pos).getCapability(ValueInterfaceConfig.CAPABILITY, side).getValue();
					if(maybeValue.isPresent()) {
						IValue value = maybeValue.get();
						return new Object[] {value.getType().getTypeName(), convertValueForLua(value)};
					}
				}
			}
			return new Object[] {null};
		}

		@Override
		public String preferredName() {
			return "value_interface";
		}

		@Override
		public int priority() {
			return 0;
		}
	}
}
