package pl.treefrog.phobos.core.channel.output;

import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.state.context.ProcessingContext;
import pl.treefrog.phobos.exception.PlatformException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public interface IOutputAgentPhaseListener<M extends Message> {

    void beforeSendPhase(M message, ProcessingContext context) throws PlatformException;

    void afterSendPhase(M message, ProcessingContext context) throws PlatformException;

}
