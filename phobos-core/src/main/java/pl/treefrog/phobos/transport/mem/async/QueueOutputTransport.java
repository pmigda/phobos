package pl.treefrog.phobos.transport.mem.async;

import pl.treefrog.phobos.core.api.IOutputTransport;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.exception.PlatformException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class QueueOutputTransport extends AbstractQueueTransport implements IOutputTransport {

    @Override
    public void init() throws PlatformException {
        //NOP
    }

    @Override
    public void start(String channelId) throws PlatformException {
        queueName = channelId;
    }

    @Override
    public void stop() throws PlatformException {

    }

    @Override
    public void sendMessage(Message msg) throws PlatformException {
        queManager.getQueue(queueName).add(msg);
    }
}
