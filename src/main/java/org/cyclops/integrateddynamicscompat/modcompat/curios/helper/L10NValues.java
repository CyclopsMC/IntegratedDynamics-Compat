package org.cyclops.integrateddynamicscompat.modcompat.curios.helper;

import org.cyclops.integrateddynamics.api.APIReference;
import org.cyclops.integrateddynamicscompat.Reference;

/**
 * L10N entries for curios modcompat.
 * @author met4000
 */
public class L10NValues {
    
    public static final String LOCAL_NS = Reference.MOD_CURIOS;
    public static final String GLOBAL_NS = APIReference.API_OWNER;
    
    public static final String ERROR_NS = "valuetype." + GLOBAL_NS + ".error." + LOCAL_NS;
    public static final String VALUETYPE_ERROR_BADPROXYSTACKSHANDLER = ERROR_NS + ".bad_proxy_stackshandler";
    public static final String VALUETYPE_ERROR_BADPROXYSLOTINDEX = ERROR_NS + ".bad_proxy_slotindex";
    public static final String VALUETYPE_ERROR_BADPROXYVALUE = ERROR_NS + ".bad_proxy_value";
    
}
