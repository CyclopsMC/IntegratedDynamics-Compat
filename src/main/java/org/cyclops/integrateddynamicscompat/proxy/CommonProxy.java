package org.cyclops.integrateddynamicscompat.proxy;

import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.proxy.CommonProxyComponent;
import org.cyclops.integrateddynamicscompat.IntegratedDynamicsCompat;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxy extends CommonProxyComponent {

    @Override
    public ModBase getMod() {
        return IntegratedDynamicsCompat._instance;
    }

}
