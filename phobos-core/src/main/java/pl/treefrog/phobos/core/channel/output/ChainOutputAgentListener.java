package pl.treefrog.phobos.core.channel.output;

import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.state.context.IProcessingContext;
import pl.treefrog.phobos.exception.PhobosException;

import java.util.LinkedList;
import java.util.List;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class ChainOutputAgentListener implements IOutputAgentListener {

    private List<IOutputAgentListener> outputAgentListeners = new LinkedList<>();

    @Override
    public void beforeSendPhase(Message message, IProcessingContext processingContext) throws PhobosException {
        for (IOutputAgentListener listener : outputAgentListeners) {
            if (listener.acceptsMessage(message)) {
                listener.beforeSendPhase(message, processingContext);
            }
        }
    }

    @Override
    public void afterSendPhase(Message message, IProcessingContext processingContext) throws PhobosException {
        for (IOutputAgentListener listener : outputAgentListeners) {
            if (listener.acceptsMessage(message)) {
                listener.afterSendPhase(message, processingContext);
            }
        }
    }

    public void registerListener(IOutputAgentListener listener) {
        outputAgentListeners.add(listener);
    }

    @Override
    public boolean acceptsMessage(Message message) {
        return true;
    }
}
