package pl.treefrog.phobos.core.state.manager;

import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.state.context.MessageContext;
import pl.treefrog.phobos.core.state.context.ProcessingContext;
import pl.treefrog.phobos.core.state.context.SecurityContext;

import java.util.UUID;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class MessageStateManager implements IStateManager<MessageContext, Message> {

    @Override
    public void retrieveProcessingContext(Message message, ProcessingContext processingContext) {
        MessageContext messageContext = prepareContext(message);
        processingContext.registerContext(MessageContext.MSG_CTX, messageContext);
    }

    private MessageContext prepareContext(Message message) {
        MessageContext messageContext = new MessageContext(UUID.randomUUID().toString(), this);

        SecurityContext securityContext = new SecurityContext(message.getControlHeader().getTenantId(), message.getControlHeader().getUserId());
        messageContext.setSecurityContext(securityContext);

        messageContext.setBatchId(message.getControlHeader().getBatchId());
        return messageContext;
    }

    @Override
    public void dispose(MessageContext context) {
        //NOP - leave it for GC
    }

    @Override
    public boolean acceptsMessage(Message message) {
        return true;
    }
}
