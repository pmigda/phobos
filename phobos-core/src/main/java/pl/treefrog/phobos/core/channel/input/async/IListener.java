package pl.treefrog.phobos.core.channel.input.async;

import pl.treefrog.phobos.core.channel.IChannelSet;
import pl.treefrog.phobos.core.processor.IProcessor;
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
public interface IListener {

    void init(IProcessor proc, IChannelSet<IAsyncInputChannel> channelSet) throws PlatformException;

    void start();

    void stop();

}
