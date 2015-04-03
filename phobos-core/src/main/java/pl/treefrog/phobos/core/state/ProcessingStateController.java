package pl.treefrog.phobos.core.state;

import pl.treefrog.phobos.core.IComponentLifecycle;
import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.state.context.GlobalContext;
import pl.treefrog.phobos.core.state.context.MessageContext;
import pl.treefrog.phobos.core.state.context.ProcessingContext;
import pl.treefrog.phobos.core.state.manager.GlobalStateManager;
import pl.treefrog.phobos.core.state.manager.IStateManager;
import pl.treefrog.phobos.core.state.manager.MessageStateManager;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PhobosException;

import java.util.HashMap;
import java.util.Map;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class ProcessingStateController implements IProcessingStateController, IComponentLifecycle {

    protected IProcessingNode parentProcNode;

    private Map<String, IStateManager> stateManagers = new HashMap<>();

    @Override
    public void init(IProcessingNode nodeConfig) throws PhobosException {
        parentProcNode = nodeConfig;
        PhobosAssert.assertNotNull("Parent processing node must not be null", parentProcNode);

        //register default managers
        stateManagers.put(GlobalContext.GLOBAL_CTX_TYPE, new GlobalStateManager());
        stateManagers.put(MessageContext.MSG_CTX, new MessageStateManager());
    }

    @Override
    public void start() throws PhobosException {

    }

    @Override
    public void stop() throws PhobosException {

    }

    @Override
    public ProcessingContext retrieveProcessingContext(Message message) {
        ProcessingContext processingContext = new ProcessingContext(parentProcNode.getNodeName());

        stateManagers.values().stream().filter(sm -> sm.acceptsMessage(message)).forEach(sm -> sm.retrieveProcessingContext(message, processingContext));

        return processingContext;
    }

    @Override
    public IStateManager getStateManager(String managerKey) {
        return stateManagers.get(managerKey);
    }

    public void registerStateManager(String managerKey, IStateManager stateManager) {
        stateManagers.put(managerKey, stateManager);
    }
}
