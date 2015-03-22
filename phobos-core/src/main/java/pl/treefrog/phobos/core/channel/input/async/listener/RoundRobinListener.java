package pl.treefrog.phobos.core.channel.input.async.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.channel.IChannelSet;
import pl.treefrog.phobos.core.channel.input.IInputChannel;
import pl.treefrog.phobos.core.channel.input.InputChannel;
import pl.treefrog.phobos.core.handler.IMessageHandler;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PlatformException;

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
 * <p>
 * Listener is a plug point for various listening strategies on input channels, scaling and thread pooling.
 */
public class RoundRobinListener implements IMessageListener {

    private static final Logger log = LoggerFactory.getLogger(RoundRobinListener.class);

    private IMessageHandler messageHandler;
    private IChannelSet<InputChannel> channelSet;

    @Override
    public void init(IMessageHandler handler, IChannelSet<InputChannel> channelSet) throws PlatformException {
        log.info("[" + this.hashCode() + "] Initializing round robin listener");

        this.messageHandler = handler;
        PhobosAssert.assertNotNull("Message handler is required for processing", handler);

        this.channelSet = channelSet;
        PhobosAssert.assertNotNull("Channel set is required for processing", channelSet);
    }

    @Override
    public void start() {
        log.info("[" + this.hashCode() + "] starting round robin listener");
        Thread thread = new Thread(() -> {
            while (true) {
                for (String inputTopic : channelSet.getRegisteredChannelIds()) {
                    try {
                        IInputChannel input = channelSet.getChannel(inputTopic);
                        Message msg = input.readMessage();

                        if (msg != null) {
                            messageHandler.processMessage(msg);
                        }

                    } catch (PlatformException e) {
                        log.error("Listener exception: " + e.getMessage());
                    }
                }
            }
        });

        thread.start();
    }

    @Override
    public void stop() {
        log.info("[" + this.hashCode() + "] stopping round robin listener");
    }

}
