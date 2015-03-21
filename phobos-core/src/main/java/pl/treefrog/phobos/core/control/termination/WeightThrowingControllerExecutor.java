package pl.treefrog.phobos.core.control.termination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.api.IExecutor;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.message.ControlMessage;
import pl.treefrog.phobos.core.message.MessageType;
import pl.treefrog.phobos.core.state.context.ProcessingContext;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PlatformException;

import java.util.Arrays;
import java.util.List;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class WeightThrowingControllerExecutor implements IExecutor<ControlMessage> {

    private static final String ROOT_PROC_QUEUE = "rootInputQ";

    private static final Logger log = LoggerFactory.getLogger(WeightThrowingControllerExecutor.class);

    private int accu = 0;

    int LIMIT = 0x80000000;//Integer.MAX_VALUE;

    @Override
    public void processMessage(ControlMessage message, IOutputAgent outputAgent, ProcessingContext context) throws PlatformException {
        if (message.getType() == MessageType.CONTROL.getIdx()) {
            processControlMessage(message);
        } else if (message.getType() == MessageType.PAYLOAD.getIdx()) {
            processPayloadMessage(message, outputAgent, context);
        }
    }

    private void processControlMessage(ControlMessage message) throws PlatformException {
        Integer msgWeight = (Integer) message.getControlHeader().get(WeightThrowingConst.NODE_WEIGHT_MSG_HEADER);
        PhobosAssert.assertNotNull("Malformed control message, no weight header, message id: " + message.getId(), msgWeight);
        log.debug("Received weight: " + msgWeight);

        accu += msgWeight;
        log.debug("New accu val: " + accu);

        if (accu == LIMIT) {
            accu = 0;
            log.debug("Hoorrraaay processing ends");
        }


    }

    private void processPayloadMessage(ControlMessage message, IOutputAgent outputAgent, ProcessingContext context) throws PlatformException {
        //new message in topo
        accu = 0;
        message.getControlHeader().put(WeightThrowingConst.NODE_WEIGHT_MSG_HEADER, LIMIT);
        outputAgent.sendMessage(ROOT_PROC_QUEUE, message, context); //forward
    }

    @Override
    public List<String> getRequiredChannelsIds() {
        return Arrays.asList(new String[]{ROOT_PROC_QUEUE});
    }
}
