package org.cyclops.integrateddynamicscompat.modcompat.top;

import com.google.common.base.Function;
import mcjty.theoneprobe.api.ITheOneProbe;
import org.cyclops.integrateddynamicscompat.modcompat.top.TopDryingBasinData;
import org.cyclops.integrateddynamicscompat.modcompat.top.TopPartData;
import org.cyclops.integrateddynamicscompat.modcompat.top.TopProxyData;
import org.cyclops.integrateddynamicscompat.modcompat.top.TopSqueezerData;

/**
 * Retriever for The One Probe.
 * @author rubensworks
 *
 */
public class TheOneProbe implements Function<ITheOneProbe, Void> {

	@Override
	public Void apply(ITheOneProbe probe) {
		probe.registerProvider(new TopPartData());
		probe.registerProvider(new TopProxyData());
		probe.registerProvider(new TopDryingBasinData());
		probe.registerProvider(new TopSqueezerData());
		return null;
	}
}
