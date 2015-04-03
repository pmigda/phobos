package pl.treefrog.phobos.core.channel.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.core.channel.AbstractChannelAgent;
import pl.treefrog.phobos.core.handler.IMessageHandler;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PhobosException;

import java.util.List;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class InputAgent extends AbstractChannelAgent<InputChannel> implements IInputAgent {

    private static final Logger log = LoggerFactory.getLogger(InputAgent.class);

    private IInputAgentPhaseListener agentPhaseListener;
    protected IMessageHandler messageHandler;

    @Override
    public void init(IProcessingNode nodeConfig) throws PhobosException {
        super.init(nodeConfig);

        log.info("[" + parentProcNode.getNodeName() + "][" + this.hashCode() + "] Initializing input agent");

        messageHandler = nodeConfig.getMessageHandler();
        PhobosAssert.assertNotNull("Message handler must not be null for operation", messageHandler);
    }

    @Override
    public List<String> getRegisteredChannelIds() {
        return channelSet.getRegisteredChannelIds();
    }

    @Override
    public Message readMessage(String channelId) throws PhobosException {
        IInputChannel input = channelSet.getChannel(channelId);
        Message result = null;

        if (input != null) {

            if (agentPhaseListener != null) {
                agentPhaseListener.beforeReadPhase();
            }

            result = input.readMessage();

            if (agentPhaseListener != null) {
                agentPhaseListener.afterReadPhase(result);
            }

        } else {
            log.error("[" + parentProcNode.getNodeName() + "][" + this.hashCode() + "] Can't read message. Possible configuration error. No channel with given id available: " + channelId);
            //TODO add runtime processing exception handling (there are some approaches conceived out there)
        }

        return result;
    }

    //IoC getters & setters
    public void setAgentPhaseListener(IInputAgentPhaseListener agentPhaseListener) {
        this.agentPhaseListener = agentPhaseListener;
    }
}
