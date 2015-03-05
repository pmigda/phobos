package com.treefrog.phobos.core.channel.output;

import com.treefrog.phobos.core.ILifecycle;
import com.treefrog.phobos.core.channel.AbstractChannelAgent;
import com.treefrog.phobos.core.msg.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class OutputAgent extends AbstractChannelAgent<IOutputChannel> implements IOutputAgent, ILifecycle {

    private static final Logger log = LoggerFactory.getLogger(OutputAgent.class);

    @Override
    public void sendMessage(String channelId, Message msg) {
        IOutputChannel output = channelSet.getChannel(channelId);

        if (output != null) {
            output.sendMessage(msg);
        } else {
            log.error("["+parentProcNode.getNodeName()+"]["+this.hashCode()+"] Can't send message. Possible configuration error. No channel with given id available: "+channelId);
            //TODO add runtime processing exception handling (there are some approaches conceived out there)
        }
    }

}
