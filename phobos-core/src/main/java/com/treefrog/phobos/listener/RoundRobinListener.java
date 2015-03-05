package com.treefrog.phobos.listener;

import com.treefrog.phobos.core.channel.IChannelSet;
import com.treefrog.phobos.core.channel.input.async.IAsyncInputChannel;
import com.treefrog.phobos.core.channel.input.async.IListener;
import com.treefrog.phobos.core.msg.Message;
import com.treefrog.phobos.core.processor.IProcessor;
import com.treefrog.phobos.exception.PhobosAssert;
import com.treefrog.phobos.exception.PlatformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */

/**
 * Unlike sync processing where active party is sender (thread triggering output point of the sync "communication" channel)
 * for queues and async processing there is a need for such an active part in the processing node so as to receive message
 * from the input channel (effectively the thread on the receiving end).
 *
 * Listener is a plug point for various listening strategies on input channels, scaling and thread pooling.
 */
public class RoundRobinListener implements IListener {

    private static final Logger log = LoggerFactory.getLogger(RoundRobinListener.class);

    private IProcessor processor;
    private IChannelSet<IAsyncInputChannel> channelSet;

    @Override
    public void init(IProcessor processor, IChannelSet<IAsyncInputChannel> channelSet) throws PlatformException {
        log.info("["+this.hashCode()+"] Initializing round robin listener");

        this.processor = processor;
        PhobosAssert.assertNotNull("Processor is required for processing", processor);

        this.channelSet = channelSet;
        PhobosAssert.assertNotNull("Channel set is required for processing", channelSet);
    }

    @Override
    public void start() {
        log.info("["+this.hashCode()+"] starting round robin listener");
        Thread thread = new Thread(() -> {
            while (true) {
                for (String inputTopic : channelSet.getRegisteredChannelIds()) {
                    IAsyncInputChannel input = channelSet.getChannel(inputTopic);
                    Message msg = input.readMessage();
                    if (msg != null) {
                        processor.processMessage(msg);
                    }
                }
            }
        });

        thread.start();
    }

    @Override
    public void stop() {
        log.info("["+this.hashCode()+"] stopping round robin listener");
    }

}
