package pl.treefrog.phobos.core.control.transaction;

import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.processor.IForwardPredicate;
import pl.treefrog.phobos.core.state.context.IProcessingContext;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class TxForwardPredicate implements IForwardPredicate {
    @Override
    public boolean shouldForward(Message message, IProcessingContext processingContext) {
        return message.isOfType(TransactionConst.MSG_TYPE_TX_COMMIT) ? false : true;
    }
}
