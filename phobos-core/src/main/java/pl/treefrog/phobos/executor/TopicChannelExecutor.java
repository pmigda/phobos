package pl.treefrog.phobos.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.api.IExecutor;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.state.context.IProcessingContext;
import pl.treefrog.phobos.exception.PhobosException;

import java.util.LinkedList;
import java.util.List;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */

/*
 * Main purpose of this executor is to resemble and emulate topic like
 * pattern of in memory (jvm) communication.
 **/
public class TopicChannelExecutor implements IExecutor {

    private static final Logger log = LoggerFactory.getLogger(TopicChannelExecutor.class);

    private List<String> broadcastChannels = new LinkedList<>();

    @Override
    public void processMessage(Message message, IOutputAgent outputAgent, IProcessingContext processingContext) throws PhobosException {
        for (String outputChannel : broadcastChannels) {

            log.debug("Sending message: " + message.getId() + " to " + outputChannel);
            outputAgent.sendMessage(outputChannel, message, processingContext);
        }
    }

    @Override
    public List<String> getRequiredChannelsIds() {
        return broadcastChannels;
    }

    @Override
    public boolean acceptsMessage(Message message) {
        return true;
    }

    //IoC getters & setters
    public void setBroadcastChannels(List<String> broadcastChannels) {
        this.broadcastChannels = broadcastChannels;
    }
}
