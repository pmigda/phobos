package pl.treefrog.phobos.core.control.termination;

import pl.treefrog.phobos.core.api.IExecutor;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.message.ControlHeader;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.message.TransactionHeader;
import pl.treefrog.phobos.core.state.context.IProcessingContext;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PhobosException;

import java.util.Arrays;
import java.util.List;

import static pl.treefrog.phobos.core.control.termination.WeightThrowingConst.*;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class WTPostProcessExecutor implements IExecutor<Message<TransactionHeader, ?>> {

    private static final String CONTROL_CHANNEL_ID = "controlChannel";

    @Override
    public void processMessage(Message<TransactionHeader, ?> message, IOutputAgent outputAgent, IProcessingContext processingContext) throws PhobosException {

        Integer nodeWeight = (Integer) processingContext.mx().get(NODE_WEIGHT_CTX_SUM);
        PhobosAssert.assertNotNull("Node weight should not be null. ", nodeWeight);
        ControlHeader x = message.getControlHeader();
        //new control message
        Message<TransactionHeader, ?> weightReportMsg = outputAgent.createMessage(MSG_TYPE_WT_CONTROL, processingContext);

        weightReportMsg.getControlHeader().put(NODE_WEIGHT_MSG_HEADER, nodeWeight);
        processingContext.mx().put(NODE_WEIGHT_CTX_SUM, 0);
        outputAgent.sendMessage(CONTROL_CHANNEL_ID, weightReportMsg, processingContext);
    }

    @Override
    public List<String> getRequiredChannelsIds() {
        return Arrays.asList(new String[]{CONTROL_CHANNEL_ID});
    }

    @Override
    public boolean acceptsMessage(Message message) {
        return message.getControlHeader() instanceof TransactionHeader ? true : false;
    }
}
