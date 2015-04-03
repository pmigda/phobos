package pl.treefrog.phobos.core.control.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.api.IExecutor;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.message.TransactionHeader;
import pl.treefrog.phobos.core.state.context.IProcessingContext;
import pl.treefrog.phobos.core.state.context.TransactionContext;
import pl.treefrog.phobos.exception.PhobosException;

import java.util.LinkedList;
import java.util.List;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class TxPreProcessExecutor implements IExecutor<Message<TransactionHeader, ?>> {

    private static final Logger log = LoggerFactory.getLogger(TxPreProcessExecutor.class);

    @Override
    public void processMessage(Message<TransactionHeader, ?> message, IOutputAgent outputAgent, IProcessingContext processingContext) throws PhobosException {
        //cleanup state
        if (message.isOfType(TransactionConst.MSG_TYPE_TX_COMMIT)) {
            log.debug(processingContext.getNodeId() + " : Finishing transaction: " + processingContext.tx().getTxId());
            ((TransactionContext) processingContext.tx()).dispose();
        }
    }

    @Override
    public List<String> getRequiredChannelsIds() {
        return new LinkedList<>();
    }

    @Override
    public boolean acceptsMessage(Message message) {
        return message.isOfType(TransactionConst.MSG_TYPE_TX_COMMIT);
    }
}
