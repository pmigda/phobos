package pl.treefrog.phobos.core.processor;

import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.state.context.ProcessingContext;
import pl.treefrog.phobos.exception.PlatformException;

import java.util.List;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public interface IProcessorPhaseListener<M extends Message> {

    void preProcessPhase(M message, ProcessingContext context, IOutputAgent outputAgent) throws PlatformException;

    void postProcessPhase(M message, ProcessingContext context, IOutputAgent outputAgent) throws PlatformException;

    List<String> getRequiredChannelsIds();

}
