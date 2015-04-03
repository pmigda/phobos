package pl.treefrog.phobos.core.message.factory;

import pl.treefrog.phobos.core.message.ControlHeader;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.message.MessageType;
import pl.treefrog.phobos.core.message.Payload;
import pl.treefrog.phobos.core.state.context.IProcessingContext;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public interface IMessageFactory {

    public <M extends Message<? extends ControlHeader, ? extends Payload>> M createMessage(MessageType messageType, IProcessingContext processingContext);

}
