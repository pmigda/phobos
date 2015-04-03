package pl.treefrog.phobos.core.control.termination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.channel.output.IOutputAgentListener;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.message.TransactionHeader;
import pl.treefrog.phobos.core.state.context.IProcessingContext;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PhobosException;

import static pl.treefrog.phobos.core.control.termination.WeightThrowingConst.NODE_WEIGHT_CTX_SUM;
import static pl.treefrog.phobos.core.control.termination.WeightThrowingConst.NODE_WEIGHT_MSG_HEADER;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class WTOutputAgentListener implements IOutputAgentListener<Message<TransactionHeader, ?>> {

    private static final Logger log = LoggerFactory.getLogger(WTOutputAgentListener.class);

    @Override
    public void beforeSendPhase(Message<TransactionHeader, ?> message, IProcessingContext processingContext) throws PhobosException {

        if (message != null && !message.isOfType(WeightThrowingConst.MSG_TYPE_WT_CONTROL)) { //skip control messages in algorithm

            Integer nodeWeight = (Integer) processingContext.mx().get(NODE_WEIGHT_CTX_SUM);
            PhobosAssert.assertNotNull("Node weight should not be null. ", nodeWeight);

            int newNodeWeight = nodeWeight >>> 1;
            int newMsgWeight = newNodeWeight; //nodeWeight - newNodeWeight;

            processingContext.mx().put(NODE_WEIGHT_CTX_SUM, newNodeWeight); //one half here
            message.getControlHeader().put(NODE_WEIGHT_MSG_HEADER, newMsgWeight); //other half here

            log.debug(WTOutputAgentListener.class + " message " + message.getId() + " weight set to: " + newMsgWeight);
        }
    }

    @Override
    public void afterSendPhase(Message<TransactionHeader, ?> message, IProcessingContext processingContext) throws PhobosException {
        //NOP
    }

    @Override
    public boolean acceptsMessage(Message message) {
        return message.getControlHeader() instanceof TransactionHeader ? true : false;
    }
}
