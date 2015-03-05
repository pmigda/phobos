package pl.treefrog.phobos.transport;

import pl.treefrog.phobos.core.channel.BaseChannel;
import pl.treefrog.phobos.transport.mem.async.QueueManager;
import pl.treefrog.phobos.transport.mem.async.QueueTransport;
import pl.treefrog.phobos.transport.mem.sync.DirectCallTransport;

import java.util.AbstractQueue;
import java.util.Map;

public class QueueTestHelper {

    public static void populateAsyncChannelsImpl(Map<String, BaseChannel> channels, QueueManager queueManager){
        for (BaseChannel channel : channels.values()){
            AbstractQueue que = queueManager.getQueue(channel.getChannelId());
            if(que == null) {
                queueManager.createQueue(channel.getChannelId(), 100);
            }

            QueueTransport queTransport = new QueueTransport();
            queTransport.setQueManager(queueManager);

            channel.setTransport(queTransport);
        }
    }

    public static DirectCallTransport createSyncTransport(){
        return new DirectCallTransport();
    }
}
