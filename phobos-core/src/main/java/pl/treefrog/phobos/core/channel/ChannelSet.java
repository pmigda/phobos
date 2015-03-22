package pl.treefrog.phobos.core.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.IComponentLifecycle;
import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PlatformException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */

/**
 * ChannelSet is a container for channels, it simply manages channels lifecycle.
 * As an generic container it can hold channels of the both input and output types
 */
public class ChannelSet<T extends AbstractChannel> implements IChannelSet<T>, IComponentLifecycle {

    private static final Logger log = LoggerFactory.getLogger(ChannelSet.class);

    protected IProcessingNode parentProcNode;
    protected Map<String, T> channels = new HashMap<>();

    @Override
    public void init(IProcessingNode nodeConfig) throws PlatformException {
        parentProcNode = nodeConfig;
        PhobosAssert.assertNotNull("Parent processing node must not be null", parentProcNode);

        log.info("[" + parentProcNode.getNodeName() + "][" + this.hashCode() + "] Initializing channel set");

        for (T channel : channels.values()) {
            channel.init(nodeConfig);
        }
    }

    @Override
    public void start() throws PlatformException {
        log.info("[" + parentProcNode.getNodeName() + "][" + this.hashCode() + "] starting channel set");

        for (AbstractChannel channel : channels.values()) {
            channel.start();
        }
    }

    @Override
    public void stop() throws PlatformException {
        log.info("[" + parentProcNode.getNodeName() + "][" + this.hashCode() + "] stopping channel set");

        for (AbstractChannel channel : channels.values()) {
            channel.stop();
        }
    }

    @Override
    public T getChannel(String channelId) {
        return channels.get(channelId);
    }

    @Override
    public List<String> getRegisteredChannelIds() {
        return new LinkedList<>(channels.keySet());
    }

    @Override
    public boolean isRegistered(String channelId) {
        return channels.containsKey(channelId);
    }

    //getters & setters
    public void registerChannels(Map<String, T> channels) {
        this.channels = channels;
    }

    public void registerChannel(T channel) {
        channels.put(channel.getChannelId(), channel);
    }

}
