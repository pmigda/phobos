package com.treefrog.phobos.core.channel;

import java.util.List;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public interface IChannelSet<T> {

    T getChannel(String topic);

    List<String> getRegisteredChannelIds();

    boolean isRegistered(String channelId);

}
