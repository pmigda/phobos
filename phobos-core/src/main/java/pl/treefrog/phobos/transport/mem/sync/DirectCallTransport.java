package pl.treefrog.phobos.transport.mem.sync;

import pl.treefrog.phobos.core.api.IInputTransport;
import pl.treefrog.phobos.core.api.IOutputTransport;
import pl.treefrog.phobos.core.handler.IMessageHandler;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.exception.PlatformException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class DirectCallTransport implements IInputTransport, IOutputTransport {

    private IMessageHandler messageHandler;

    @Override
    public void init(IMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void init() throws PlatformException {
        //NOP - init output transport
    }

    @Override
    public void start(String channelId) {
        //NOP
    }

    @Override
    public void stop() {
        //NOP
    }

    @Override
    public void sendMessage(Message msg) throws PlatformException {
        messageHandler.processMessage(msg);
    }

    @Override
    public Message readMessage() {
        throw new UnsupportedOperationException("Reading input is not applicable for sync transport");
    }

}
