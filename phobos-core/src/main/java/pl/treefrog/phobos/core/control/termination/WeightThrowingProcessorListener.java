package pl.treefrog.phobos.core.control.termination;

import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.message.ControlMessage;
import pl.treefrog.phobos.core.processor.IProcessorPhaseListener;
import pl.treefrog.phobos.core.state.context.ProcessingContext;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PlatformException;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static pl.treefrog.phobos.core.control.termination.WeightThrowingConst.NODE_WEIGHT_CTX_SUM;
import static pl.treefrog.phobos.core.control.termination.WeightThrowingConst.NODE_WEIGHT_MSG_HEADER;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class WeightThrowingProcessorListener implements IProcessorPhaseListener<ControlMessage> {

    private static final String CONTROL_CHANNEL_ID = "controlChannel";
    private Random random = new Random();

    @Override
    public void preProcessPhase(ControlMessage message, ProcessingContext context, IOutputAgent outputAgent) throws PlatformException {
        Integer nodeWeight = (Integer) context.get(NODE_WEIGHT_CTX_SUM);
        if (nodeWeight == null) { //init
            nodeWeight = 0;
        }
        Integer msgWeight = (Integer) message.getControlHeader().get(WeightThrowingConst.NODE_WEIGHT_MSG_HEADER);
        PhobosAssert.assertNotNull("Malformed control message, no weight header, message id: " + message.getId(), msgWeight);
        nodeWeight += msgWeight;
        context.put(NODE_WEIGHT_CTX_SUM, nodeWeight);
    }

    @Override
    public void postProcessPhase(ControlMessage message, ProcessingContext context, IOutputAgent outputAgent) throws PlatformException {

        Integer nodeWeight = (Integer) context.get(NODE_WEIGHT_CTX_SUM);
        PhobosAssert.assertNotNull("Node weight should not be null. ", nodeWeight);

        //new control message
        ControlMessage weightReportMsg = new ControlMessage(random.nextInt());
        weightReportMsg.getControlHeader().put(NODE_WEIGHT_MSG_HEADER, nodeWeight);
        context.put(NODE_WEIGHT_CTX_SUM, 0);
        outputAgent.sendMessage(CONTROL_CHANNEL_ID, weightReportMsg, context);
    }

    @Override
    public List<String> getRequiredChannelsIds() {
        return Arrays.asList(new String[]{CONTROL_CHANNEL_ID});
    }
}
