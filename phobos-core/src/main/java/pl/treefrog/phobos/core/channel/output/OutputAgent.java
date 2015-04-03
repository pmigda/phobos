package pl.treefrog.phobos.core.channel.output;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.IComponentLifecycle;
import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.core.channel.AbstractChannelAgent;
import pl.treefrog.phobos.core.message.ControlHeader;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.message.MessageType;
import pl.treefrog.phobos.core.message.Payload;
import pl.treefrog.phobos.core.message.factory.IMessageFactory;
import pl.treefrog.phobos.core.state.context.IProcessingContext;
import pl.treefrog.phobos.exception.PhobosException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class OutputAgent extends AbstractChannelAgent<OutputChannel> implements IOutputAgent, IComponentLifecycle {

    private static final Logger log = LoggerFactory.getLogger(OutputAgent.class);
    private IOutputAgentListener agentPhaseListener;
    private IMessageFactory messageFactory;

    @Override
    public void init(IProcessingNode nodeConfig) throws PhobosException {
        super.init(nodeConfig);

        messageFactory = nodeConfig.getMessageFactory();
        if (messageFactory == null) {
            log.warn("[" + parentProcNode.getNodeName() + "] Output agent has no message factory provided");
        }
    }

    @Override
    public void sendMessage(String channelId, Message message, IProcessingContext processingContext) throws PhobosException {
        IOutputChannel output = channelSet.getChannel(channelId);

        if (output != null) {

            if (agentPhaseListener != null && agentPhaseListener.acceptsMessage(message)) {
                agentPhaseListener.beforeSendPhase(message, processingContext);
            }

            output.sendMessage(message);

            if (agentPhaseListener != null && agentPhaseListener.acceptsMessage(message)) {
                agentPhaseListener.afterSendPhase(message, processingContext);
            }

        } else {
            log.error("[" + parentProcNode.getNodeName() + "][" + this.hashCode() + "] Can't send message. Possible configuration error. No channel with given id available: " + channelId);
            //TODO add runtime processing exception handling (there are some approaches conceived out there)
        }
    }

    @Override
    public <M extends Message<? extends ControlHeader, ? extends Payload>> M createMessage(MessageType messageType, IProcessingContext processingContext) {
        return messageFactory.createMessage(messageType, processingContext);
    }

    //IoC getters & setters
    public void setAgentPhaseListener(IOutputAgentListener agentPhaseListener) {
        this.agentPhaseListener = agentPhaseListener;
    }

}
