package pl.treefrog.phobos.transport;

import pl.treefrog.phobos.core.channel.input.InputChannel;
import pl.treefrog.phobos.core.channel.output.OutputChannel;
import pl.treefrog.phobos.transport.mem.async.QueueInputTransport;
import pl.treefrog.phobos.transport.mem.async.QueueManager;
import pl.treefrog.phobos.transport.mem.async.QueueOutputTransport;
import pl.treefrog.phobos.transport.mem.sync.DirectCallTransport;

import java.util.AbstractQueue;
import java.util.Map;

public class QueueTestHelper {

    public static void populateAsyncInputChannelsImpl(Map<String, InputChannel> channels, QueueManager queueManager) {
        for (InputChannel channel : channels.values()) {
            AbstractQueue que = queueManager.getQueue(channel.getChannelId());
            if (que == null) {
                queueManager.createQueue(channel.getChannelId(), 100);
            }

            QueueInputTransport queTransport = new QueueInputTransport();
            queTransport.setQueManager(queueManager);

            channel.registerInputTransport(queTransport);
        }
    }

    public static void populateAsyncOutputChannelsImpl(Map<String, OutputChannel> channels, QueueManager queueManager) {
        for (OutputChannel channel : channels.values()) {
            AbstractQueue que = queueManager.getQueue(channel.getChannelId());
            if (que == null) {
                queueManager.createQueue(channel.getChannelId(), 100);
            }

            QueueOutputTransport queTransport = new QueueOutputTransport();
            queTransport.setQueManager(queueManager);

            channel.registerOutputTransport(queTransport);
        }
    }

    public static DirectCallTransport createSyncTransport() {
        return new DirectCallTransport();
    }
}
