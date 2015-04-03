package pl.treefrog.phobos.core.state.manager;

import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.message.TransactionHeader;
import pl.treefrog.phobos.core.state.context.ProcessingContext;
import pl.treefrog.phobos.core.state.context.TransactionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class TransactionStateManager implements IStateManager<TransactionContext, Message<TransactionHeader, ?>> {

    private Lock contextContainerLock = new ReentrantLock();
    private Map<String, TransactionContext> contextContainerMap = new HashMap<>();

    @Override
    public void retrieveProcessingContext(Message<TransactionHeader, ?> message, ProcessingContext processingContext) {
        String txId = message.getControlHeader().getTxId();
        contextContainerLock.lock();
        try {

            TransactionContext transactionContext = contextContainerMap.get(txId);

            if (transactionContext == null) {
                transactionContext = prepareContext(txId);
                contextContainerMap.put(txId, transactionContext);
            }

            processingContext.registerContext(TransactionContext.TX_CTX, transactionContext);

        } finally {
            contextContainerLock.unlock();
        }
    }

    private TransactionContext prepareContext(String txId) {
        return new TransactionContext(UUID.randomUUID().toString(), txId, this);
    }

    @Override
    public void dispose(TransactionContext context) {
        contextContainerMap.remove(context.getTxId());
    }

    @Override
    public boolean acceptsMessage(Message message) {
        return message.getControlHeader() instanceof TransactionHeader ? true : false;
    }
}
