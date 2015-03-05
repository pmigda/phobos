package pl.treefrog.phobos.transport.mem.async;

import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.core.api.ITransport;
import pl.treefrog.phobos.core.msg.Message;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class QueueTransport implements ITransport {

    private String queueName;
    private QueueManager queManager;

    @Override
    public void init(IProcessingNode nodeConfig) {
        //NOP
    }

    @Override
    public void start(String channelId) {
        queueName = channelId;
    }

    @Override
    public void stop() {

    }

    @Override
    public void sendMessage(Message msg) {
        queManager.getQueue(queueName).add(msg);
    }

    @Override
    public Message readMessage() {
        return queManager.getQueue(queueName).poll();
    }

    public void setQueManager(QueueManager queManager) {
        this.queManager = queManager;
    }
}
