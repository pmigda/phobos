package pl.treefrog.phobos.core.channel.output;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.IComponentLifecycle;
import pl.treefrog.phobos.core.channel.AbstractChannelAgent;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.state.context.ProcessingContext;
import pl.treefrog.phobos.exception.PlatformException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class OutputAgent extends AbstractChannelAgent<IOutputChannel> implements IOutputAgent, IComponentLifecycle {

    private static final Logger log = LoggerFactory.getLogger(OutputAgent.class);

    @Override
    public void sendMessage(String channelId, Message msg, ProcessingContext context) throws PlatformException {
        IOutputChannel output = channelSet.getChannel(channelId);

        if (output != null) {

            if (agentPhaseListener != null) {
                agentPhaseListener.preProcessPhase(msg, context);
            }

            output.sendMessage(msg);

            if (agentPhaseListener != null) {
                agentPhaseListener.postProcessPhase(msg, context);
            }

        } else {
            log.error("[" + parentProcNode.getNodeName() + "][" + this.hashCode() + "] Can't send message. Possible configuration error. No channel with given id available: " + channelId);
            //TODO add runtime processing exception handling (there are some approaches conceived out there)
        }
    }

}
