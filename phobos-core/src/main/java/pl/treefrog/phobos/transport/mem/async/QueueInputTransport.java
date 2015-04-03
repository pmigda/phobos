package pl.treefrog.phobos.transport.mem.async;

import pl.treefrog.phobos.core.api.IInputTransport;
import pl.treefrog.phobos.core.handler.IMessageHandler;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.exception.PhobosException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class QueueInputTransport extends AbstractQueueTransport implements IInputTransport {

    @Override
    public void init(IMessageHandler messageHandler) throws PhobosException {
        //NOP async processing
    }

    @Override
    public void start(String channelId) throws PhobosException {
        queueName = channelId;
    }

    @Override
    public void stop() throws PhobosException {
        //NOP
    }

    @Override
    public Message readMessage() throws PhobosException {
        return queManager.getQueue(queueName).poll();
    }
}
