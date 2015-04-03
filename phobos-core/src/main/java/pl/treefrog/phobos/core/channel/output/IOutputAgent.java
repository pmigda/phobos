package pl.treefrog.phobos.core.channel.output;

import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.message.factory.IMessageFactory;
import pl.treefrog.phobos.core.state.context.IProcessingContext;
import pl.treefrog.phobos.exception.PhobosException;

import java.util.List;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public interface IOutputAgent extends IMessageFactory {

    boolean checkChannelsRegistered(List<String> channelIds);

    void sendMessage(String channelId, Message msg, IProcessingContext processingContext) throws PhobosException;

}
