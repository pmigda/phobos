package com.treefrog.phobos.core.channel;

import com.treefrog.phobos.core.ILifecycle;
import com.treefrog.phobos.core.IProcessingNode;
import com.treefrog.phobos.exception.PhobosAssert;
import com.treefrog.phobos.exception.PlatformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ChannelSet<T extends IChannel> implements IChannelSet<T>, ILifecycle {

    private static final Logger log = LoggerFactory.getLogger(ChannelSet.class);

    protected IProcessingNode parentProcNode;
    protected Map<String, BaseChannel> channels = new HashMap<>();

    @Override
    public void init(IProcessingNode nodeConfig) throws PlatformException {
        parentProcNode = nodeConfig;
        PhobosAssert.assertNotNull("Parent processing node must not be null", parentProcNode);

        log.info("["+parentProcNode.getNodeName()+"]["+this.hashCode()+"] Initializing channel set");

        for (BaseChannel channel : channels.values()) {
            channel.init(nodeConfig);
        }
    }

    @Override
    public void start() {
        log.info("["+parentProcNode.getNodeName()+"]["+this.hashCode()+"] starting channel set");

        for (BaseChannel channel : channels.values()) {
            channel.start();
        }
    }

    @Override
    public void stop() {
        log.info("["+parentProcNode.getNodeName()+"]["+this.hashCode()+"] stopping channel set");

        for (BaseChannel channel : channels.values()) {
            channel.stop();
        }
    }

    @Override
    public T getChannel(String channelId) {
        return (T) channels.get(channelId);
    }

    @Override
    public List<String> getRegisteredChannelIds() {
        return new LinkedList<String>(channels.keySet());
    }

    @Override
    public boolean isRegistered(String channelId) {
        return channels.containsKey(channelId);
    }

    //getters & setters
    public void registerChannels(Map<String, BaseChannel> channels) {
        this.channels = channels;
    }

    public void registerChannel(BaseChannel channel) {
        channels.put(channel.getChannelId(), channel);
    }

    public BaseChannel getChannelInternal(String channelId) {
        return channels.get(channelId);
    }

}
