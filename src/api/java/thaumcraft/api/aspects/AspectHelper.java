package thaumcraft.api.aspects;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class AspectHelper {
	
	public static AspectList cullTags(AspectList temp) {
		return cullTags(temp,7);
	}

	public static AspectList cullTags(AspectList temp, int cap) {
		return null;
	}

	public static AspectList getObjectAspects(ItemStack is) {
		return null;
	}

	public static AspectList generateTags(ItemStack is) {
		return null;
	}

	public static AspectList getEntityAspects(Entity entity) {
		return null;
	}

	public static Aspect getCombinationResult(Aspect aspect1, Aspect aspect2) {
		return null;
	}

	public static Aspect getRandomPrimal(Random rand, Aspect aspect) {
		return null;
	}

	public static AspectList reduceToPrimals(AspectList in) {
		return null;
	}

	public static AspectList getPrimalAspects(AspectList in) {
		return null;
	}
	
	public static AspectList getAuraAspects(AspectList in) {
		return null;
	}

}
