package pl.treefrog.phobos.core.message.factory;

import pl.treefrog.phobos.core.message.ControlHeader;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.message.MessageType;
import pl.treefrog.phobos.core.state.context.IProcessingContext;

import java.util.Random;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class MessageFactory implements IMessageFactory {

    private Random random = new Random();

    @Override
    public Message<ControlHeader, ?> createMessage(MessageType messageType, IProcessingContext processingContext) {
        Message message = new Message<>(new ControlHeader());
        message.setType(messageType);
        setupMessage(message, processingContext);
        return message;
    }

    public void setupMessage(Message<? extends ControlHeader, ?> message, IProcessingContext processingContext) {
        message.setId(random.nextInt());
    }

}
