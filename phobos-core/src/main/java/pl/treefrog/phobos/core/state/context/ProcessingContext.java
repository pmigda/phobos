package pl.treefrog.phobos.core.state.context;

import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.exception.PhobosException;

import java.util.HashMap;
import java.util.Map;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
/*
 * ProcessingContext is a main container for contexts with different lifespan and scope.
 * For the sake of convenience it exposes major contexts.
 *
 **/
public class ProcessingContext implements IProcessingContext {

    private String nodeId;

    private Map<String, AbstractContext> contextContainer = new HashMap<>();

    public ProcessingContext(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public String getNodeId() {
        return nodeId;
    }

    @Override
    public IContext getCtx(String contextKey) {
        return contextContainer.get(contextKey);
    }

    ;

    @Override
    public IContext gx() {
        return contextContainer.get(GlobalContext.GLOBAL_CTX_TYPE);
    }

    @Override
    public ITransactionContext tx() {
        return (ITransactionContext) contextContainer.get(TransactionContext.TX_CTX);
    }

    @Override
    public IMessageContext mx() {
        return (IMessageContext) contextContainer.get(MessageContext.MSG_CTX);
    }

    public void registerContext(String contextKey, AbstractContext context) {
        contextContainer.put(contextKey, context);
    }

    public void beforeMessageProcessing(Message msg) throws PhobosException {
        for (AbstractContext ctx : contextContainer.values()) {
            ctx.beforeMessageProcessing();
        }
    }

    public void afterMessageProcessing(Message msg) throws PhobosException {
        for (AbstractContext ctx : contextContainer.values()) {
            ctx.afterMessageProcessing();
        }
    }


}
