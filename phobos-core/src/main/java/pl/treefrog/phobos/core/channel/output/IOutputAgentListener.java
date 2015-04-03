package pl.treefrog.phobos.core.channel.output;

import pl.treefrog.phobos.core.api.IMessageAware;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.state.context.IProcessingContext;
import pl.treefrog.phobos.exception.PhobosException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public interface IOutputAgentListener<M extends Message> extends IMessageAware<Message> {

    void beforeSendPhase(M message, IProcessingContext processingContext) throws PhobosException;

    void afterSendPhase(M message, IProcessingContext processingContext) throws PhobosException;

}
