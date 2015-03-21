package pl.treefrog.phobos.core.api;

import pl.treefrog.phobos.core.handler.IMessageHandler;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.exception.PlatformException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */

/**
 * Platform plug point for messages xchange transport
 */
public interface ITransport {

    void init(IMessageHandler messageHandler) throws PlatformException;

    void start(String channelId) throws PlatformException;

    void stop() throws PlatformException;

    void sendMessage(Message msg) throws PlatformException;

    Message readMessage() throws PlatformException;

}
