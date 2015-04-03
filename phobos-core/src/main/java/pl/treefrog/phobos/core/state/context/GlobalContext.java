package pl.treefrog.phobos.core.state.context;

import pl.treefrog.phobos.core.state.manager.IStateManager;

import java.util.concurrent.ConcurrentHashMap;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class GlobalContext extends AbstractContext {

    public static final String GLOBAL_CTX_TYPE = "global";

    public GlobalContext(String objectId, IStateManager parentStateManager) {
        super(GLOBAL_CTX_TYPE, objectId, parentStateManager);

        //simple thread "safe" implementation for now
        valueStore = new ConcurrentHashMap<>();
    }

    @Override
    public void beforeMessageProcessing() {
        //NOP
    }

    @Override
    public void afterMessageProcessing() {
        //NOP
    }
}
