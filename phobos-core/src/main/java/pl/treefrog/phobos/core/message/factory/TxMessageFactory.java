package pl.treefrog.phobos.core.message.factory;

import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.message.MessageType;
import pl.treefrog.phobos.core.message.TransactionHeader;
import pl.treefrog.phobos.core.state.context.IProcessingContext;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class TxMessageFactory implements IMessageFactory {

    private MessageFactory basicMessageFactory = new MessageFactory();

    @Override
    public Message<TransactionHeader, ?> createMessage(MessageType messageType, IProcessingContext processingContext) {
        Message<TransactionHeader, ?> message = new Message<>(new TransactionHeader());
        message.setType(messageType);
        setupMessage(message, processingContext);
        return message;
    }

    public void setupMessage(Message<? extends TransactionHeader, ?> message, IProcessingContext processingContext) {
        basicMessageFactory.setupMessage(message, processingContext);
        String txId = processingContext.tx().getTxId();
        if (txId != null) {
            message.getControlHeader().setTxId(txId);
        }
    }
}
