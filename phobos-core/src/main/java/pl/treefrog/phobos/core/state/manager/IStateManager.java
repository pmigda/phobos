package pl.treefrog.phobos.core.state.manager;

import pl.treefrog.phobos.core.api.IMessageAware;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.state.context.AbstractContext;
import pl.treefrog.phobos.core.state.context.ProcessingContext;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
/*
 * State manages context pool of given type. Optionally it can wrap and manage external storage of state context.
 **/
public interface IStateManager<CTX extends AbstractContext, M extends Message> extends IMessageAware<Message> {

    void retrieveProcessingContext(M msg, ProcessingContext processingContext);

    void dispose(CTX context);
}
