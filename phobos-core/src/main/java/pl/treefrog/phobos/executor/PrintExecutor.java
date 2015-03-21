package pl.treefrog.phobos.executor;

import pl.treefrog.phobos.core.api.IExecutor;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.state.context.ProcessingContext;

import java.util.LinkedList;
import java.util.List;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */

/**
 * Basic processing executor example. It prints out message id, no output channels in use.
 */
public class PrintExecutor implements IExecutor {

    @Override
    public void processMessage(Message message, IOutputAgent outputAgent, ProcessingContext context) {
        System.out.println(message.getId());
    }

    @Override
    public List<String> getRequiredChannelsIds() {
        return new LinkedList<>();
    }

}