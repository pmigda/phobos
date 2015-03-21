package pl.treefrog.phobos.core.control.termination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.channel.IAgentPhaseListener;
import pl.treefrog.phobos.core.message.ControlMessage;
import pl.treefrog.phobos.core.message.MessageType;
import pl.treefrog.phobos.core.state.context.ProcessingContext;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PlatformException;

import static pl.treefrog.phobos.core.control.termination.WeightThrowingConst.NODE_WEIGHT_CTX_SUM;
import static pl.treefrog.phobos.core.control.termination.WeightThrowingConst.NODE_WEIGHT_MSG_HEADER;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class WeightThrowingChannelListener implements IAgentPhaseListener<ControlMessage> {

    private static final Logger log = LoggerFactory.getLogger(WeightThrowingChannelListener.class);

    @Override
    public void preProcessPhase(ControlMessage message, ProcessingContext context) throws PlatformException {

        if (message.getType() != MessageType.CONTROL.getIdx()) { //skip control messages in algorithm

            Integer nodeWeight = (Integer) context.get(NODE_WEIGHT_CTX_SUM);
            PhobosAssert.assertNotNull("Node weight should not be null. ", nodeWeight);

            int newNodeWeight = nodeWeight >>> 1;
            int newMsgWeight = newNodeWeight; //nodeWeight - newNodeWeight;

            context.put(NODE_WEIGHT_CTX_SUM, newNodeWeight); //one half here
            message.getControlHeader().put(NODE_WEIGHT_MSG_HEADER, newMsgWeight); //other half here

            log.debug(WeightThrowingChannelListener.class + " message " + message.getId() + " weight set to: " + newMsgWeight);
        }

    }

    @Override
    public void postProcessPhase(ControlMessage message, ProcessingContext context) throws PlatformException {
        //NOP
    }
}
