package pl.treefrog.phobos.core.control.termination;

import pl.treefrog.phobos.core.message.MessageType;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class WeightThrowingConst {

    //Weight throwing message group
    public static final String MSG_GROUP_WT = "WT-GROUP";
    public static final Integer MSG_GROUP_WT_ID = 100;

    //messages
    public static final MessageType MSG_TYPE_WT_CONTROL = new MessageType(100, "WEIGHT-CONTROL-MSG", MSG_GROUP_WT_ID, MSG_GROUP_WT);

    public static final String NODE_WEIGHT_CTX_SUM = "WT-SUM";
    public static final String NODE_WEIGHT_MSG_HEADER = "WT-CTL";
}
