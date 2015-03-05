package com.treefrog.phobos.core.channel.input.async;

import com.treefrog.phobos.core.channel.IChannelSet;
import com.treefrog.phobos.core.processor.IProcessor;
import com.treefrog.phobos.exception.PlatformException;

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
