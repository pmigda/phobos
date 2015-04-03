package pl.treefrog.phobos.core.state;

import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.state.context.ProcessingContext;
import pl.treefrog.phobos.core.state.manager.IStateManager;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public interface IProcessingStateController {

    ProcessingContext retrieveProcessingContext(Message message);

    IStateManager getStateManager(String managerKey);

}
