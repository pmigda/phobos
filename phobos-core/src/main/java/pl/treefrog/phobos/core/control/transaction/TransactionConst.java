package pl.treefrog.phobos.core.control.transaction;

import pl.treefrog.phobos.core.message.MessageType;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class TransactionConst {

    //Transaction message group
    public static final String MSG_GROUP_TX = "TX-GROUP";
    public static final Integer MSG_GROUP_TX_ID = 200;

    //transaction messages
    public static final MessageType MSG_TYPE_TX_COMMIT = new MessageType(200, "TX-COMMIT-MSG", MSG_GROUP_TX_ID, MSG_GROUP_TX);
    public static final MessageType MSG_TYPE_TX_ROLLBACK = new MessageType(201, "TX-ROLLBACK-MSG", MSG_GROUP_TX_ID, MSG_GROUP_TX);
}
