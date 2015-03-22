package pl.treefrog.phobos.core.channel.input.async.listener;

import pl.treefrog.phobos.core.channel.IChannelSet;
import pl.treefrog.phobos.core.channel.input.InputChannel;
import pl.treefrog.phobos.core.handler.IMessageHandler;
import pl.treefrog.phobos.exception.PlatformException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */

/**
 * Interface for listening strategy logic
 */
public interface IMessageListener {

    void init(IMessageHandler handler, IChannelSet<InputChannel> channelSet) throws PlatformException;

    void start() throws PlatformException;

    void stop() throws PlatformException;

}
