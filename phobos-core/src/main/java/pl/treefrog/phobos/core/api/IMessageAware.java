package pl.treefrog.phobos.core.api;

import pl.treefrog.phobos.core.message.Message;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public interface IMessageAware<M extends Message> {

    boolean acceptsMessage(M message);

}
