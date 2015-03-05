package pl.treefrog.phobos.core.channel.output;

import pl.treefrog.phobos.core.msg.Message;

import java.util.List;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public interface IOutputAgent {

    boolean checkChannelsRegistered(List<String> channelIds);

    void sendMessage(String channelId, Message msg);

}
