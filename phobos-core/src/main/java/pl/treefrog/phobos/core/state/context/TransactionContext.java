package pl.treefrog.phobos.core.state.context;

import pl.treefrog.phobos.core.state.manager.IStateManager;
import pl.treefrog.phobos.exception.PhobosException;
import pl.treefrog.phobos.exception.PhobosInvalidStateException;

import java.util.concurrent.locks.ReentrantLock;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class TransactionContext extends AbstractContext implements ITransactionContext {

    public static final String TX_CTX = "tx";

    private String txId;
    private ReentrantLock lock = new ReentrantLock();

    public TransactionContext(String objectId, String txId, IStateManager parentStateManager) {
        super(TX_CTX, objectId, parentStateManager);
        this.txId = txId;
    }

    @Override
    public String getTxId() {
        return txId;
    }

    @Override
    public void beforeMessageProcessing() throws PhobosException {
        lock.lock();
        if (disposed) {
            throw new PhobosInvalidStateException("Transaction context was disposed before all threads finished processing.");
        }
    }

    @Override
    public void afterMessageProcessing() throws PhobosException {
        lock.unlock();
    }

}
