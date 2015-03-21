package pl.treefrog.phobos.core.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.IComponentLifecycle;
import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PlatformException;

import java.util.List;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */

/**
 * Manages channels set, acts as an interface to framework components.
 * Processor passes channel agent to provided executor which can send messages thru agent delivered from container.
 * <p>
 * API hint: Agent doesn't expose registered channels. Each executor provides list of required channels which existence is
 * checked against agent. Effectively there's one point of control for all input / output channels (i.e. input and output agent respectively),
 * but executors know only channels they require and are ensured those channels exist during init phase.
 */
public abstract class AbstractChannelAgent<C extends IChannel> implements IChannelAgent, IComponentLifecycle {

    private static final Logger log = LoggerFactory.getLogger(AbstractChannelAgent.class);

    protected IProcessingNode parentProcNode;
    protected ChannelSet<C> channelSet;
    protected IAgentPhaseListener agentPhaseListener;

    @Override
    public void init(IProcessingNode nodeConfig) throws PlatformException {
        parentProcNode = nodeConfig;
        PhobosAssert.assertNotNull("Parent processing node must not be null", parentProcNode);

        log.info("[" + parentProcNode.getNodeName() + "][" + this.hashCode() + "] Initializing agent");

        PhobosAssert.assertNotNull("Channel set must not be null for operation", channelSet);
        channelSet.init(nodeConfig);
    }

    @Override
    public void start() throws PlatformException {
        log.info("[" + parentProcNode.getNodeName() + "][" + this.hashCode() + "] starting agent");
        if (channelSet != null) {
            channelSet.start();
        }
    }

    @Override
    public void stop() throws PlatformException {
        log.info("[" + parentProcNode.getNodeName() + "][" + this.hashCode() + "] stopping agent");
        if (channelSet != null) {
            channelSet.stop();
        }
    }

    @Override
    public boolean checkChannelsRegistered(List<String> channelIds) {
        if (channelSet == null || channelIds == null) {
            return false;
        }
        for (String channelId : channelIds) {
            if (!channelSet.isRegistered(channelId)) {
                return false;
            }
        }
        return true;
    }

    //getters & setters
    public ChannelSet<C> getChannelSet() {
        return channelSet;
    }

    public void setChannelSet(ChannelSet<C> channelSet) {
        this.channelSet = channelSet;
    }

    public void setAgentPhaseListener(IAgentPhaseListener agentPhaseListener) {
        this.agentPhaseListener = agentPhaseListener;
    }
}
