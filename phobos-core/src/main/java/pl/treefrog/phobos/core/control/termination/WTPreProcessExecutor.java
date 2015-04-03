package pl.treefrog.phobos.core.control.termination;

import pl.treefrog.phobos.core.api.IExecutor;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.control.transaction.TransactionConst;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.message.TransactionHeader;
import pl.treefrog.phobos.core.state.context.IProcessingContext;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PhobosException;

import java.util.LinkedList;
import java.util.List;

import static pl.treefrog.phobos.core.control.termination.WeightThrowingConst.NODE_WEIGHT_CTX_SUM;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class WTPreProcessExecutor implements IExecutor<Message<TransactionHeader, ?>> {

    @Override
    public void processMessage(Message<TransactionHeader, ?> message, IOutputAgent outputAgent, IProcessingContext processingContext) throws PhobosException {
        if (!message.isOfType(TransactionConst.MSG_TYPE_TX_COMMIT)) {
            Integer nodeWeight = (Integer) processingContext.mx().get(NODE_WEIGHT_CTX_SUM);
            if (nodeWeight == null) { //init
                nodeWeight = 0;
            }
            Integer msgWeight = (Integer) message.getControlHeader().get(WeightThrowingConst.NODE_WEIGHT_MSG_HEADER);
            PhobosAssert.assertNotNull("Malformed control message, no weight header, message id: " + message.getId(), msgWeight);
            nodeWeight += msgWeight;
            processingContext.mx().put(NODE_WEIGHT_CTX_SUM, nodeWeight);

        }
    }

    @Override
    public List<String> getRequiredChannelsIds() {
        return new LinkedList<>();
    }

    @Override
    public boolean acceptsMessage(Message message) {
        return (message.getControlHeader() instanceof TransactionHeader && !message.isOfType(TransactionConst.MSG_TYPE_TX_COMMIT)) ? true : false;
    }
}
