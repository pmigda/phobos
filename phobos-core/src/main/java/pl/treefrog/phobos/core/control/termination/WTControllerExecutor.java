package pl.treefrog.phobos.core.control.termination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.api.IExecutor;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.message.TransactionHeader;
import pl.treefrog.phobos.core.state.context.IProcessingContext;
import pl.treefrog.phobos.core.state.context.TransactionContext;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PhobosException;

import java.util.Arrays;
import java.util.List;

import static pl.treefrog.phobos.core.control.termination.WeightThrowingConst.NODE_WEIGHT_MSG_HEADER;
import static pl.treefrog.phobos.core.control.transaction.TransactionConst.MSG_TYPE_TX_COMMIT;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class WTControllerExecutor implements IExecutor<Message<TransactionHeader, ?>> {

    private static final String ROOT_PROC_QUEUE = "rootInputQ";
    private static final String CONTROL_BCAST_QUEUE = "controlBcastTopic";

    private static final Logger log = LoggerFactory.getLogger(WTControllerExecutor.class);

    int LIMIT = 0x80000000;//Integer.MAX_VALUE;

    @Override
    public void processMessage(Message<TransactionHeader, ?> message, IOutputAgent outputAgent, IProcessingContext processingContext) throws PhobosException {
        if (message.isOfType(WeightThrowingConst.MSG_TYPE_WT_CONTROL)) {
            processControlMessage(message, outputAgent, processingContext);
        } else {
            processPayloadMessage(message, outputAgent, processingContext);
        }
    }

    private void processControlMessage(Message<TransactionHeader, ?> message, IOutputAgent outputAgent, IProcessingContext processingContext) throws PhobosException {
        Integer msgWeight = (Integer) message.getControlHeader().get(NODE_WEIGHT_MSG_HEADER);
        PhobosAssert.assertNotNull("Malformed control message, no weight header, message id: " + message.getId(), msgWeight);
        log.debug("Received weight: " + msgWeight);

        Integer accu = (Integer) processingContext.tx().get("accu");
        accu += msgWeight;
        processingContext.tx().put("accu", accu);
        log.debug("New accu val: " + accu);

        if (accu == LIMIT) {
            ((TransactionContext) processingContext.tx()).dispose();
            Message<TransactionHeader, ?> weightProcFinishMsg = outputAgent.createMessage(MSG_TYPE_TX_COMMIT, processingContext);

            outputAgent.sendMessage(CONTROL_BCAST_QUEUE, weightProcFinishMsg, processingContext);
            log.debug("Hoorrraaay processing ends");
        }

    }

    private void processPayloadMessage(Message<TransactionHeader, ?> message, IOutputAgent outputAgent, IProcessingContext processingContext) throws PhobosException {
        //new message in topo

        processingContext.tx().put("accu", new Integer(0));
        message.getControlHeader().put(NODE_WEIGHT_MSG_HEADER, LIMIT);

        outputAgent.sendMessage(ROOT_PROC_QUEUE, message, processingContext); //forward
    }

    @Override
    public List<String> getRequiredChannelsIds() {
        return Arrays.asList(new String[]{ROOT_PROC_QUEUE});
    }

    @Override
    public boolean acceptsMessage(Message message) {
        return message.getControlHeader() instanceof TransactionHeader ? true : false;
    }
}
