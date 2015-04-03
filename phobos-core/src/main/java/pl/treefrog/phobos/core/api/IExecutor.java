package pl.treefrog.phobos.core.api;

import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.state.context.IProcessingContext;
import pl.treefrog.phobos.exception.PhobosException;

import java.util.List;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */

/**
 * Platform plug point for message processing logic
 */
public interface IExecutor<M extends Message> extends IMessageAware<Message> {

    void processMessage(M message, IOutputAgent outputAgent, IProcessingContext processingContext) throws PhobosException;

    List<String> getRequiredChannelsIds();

}
