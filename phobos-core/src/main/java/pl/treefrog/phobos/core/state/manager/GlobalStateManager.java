package pl.treefrog.phobos.core.state.manager;

import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.state.context.GlobalContext;
import pl.treefrog.phobos.core.state.context.ProcessingContext;

import java.util.UUID;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class GlobalStateManager implements IStateManager<GlobalContext, Message> {

    private GlobalContext globalContext;

    public GlobalStateManager() {
        globalContext = new GlobalContext(UUID.randomUUID().toString(), this);
    }

    @Override
    public void retrieveProcessingContext(Message message, ProcessingContext processingContext) {
        processingContext.registerContext(GlobalContext.GLOBAL_CTX_TYPE, globalContext);
    }

    @Override
    public void dispose(GlobalContext context) {
        //NOP
    }

    @Override
    public boolean acceptsMessage(Message message) {
        return true;
    }
}
