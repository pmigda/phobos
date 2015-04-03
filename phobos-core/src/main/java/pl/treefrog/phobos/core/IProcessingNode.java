package pl.treefrog.phobos.core;

import pl.treefrog.phobos.core.channel.input.IInputAgent;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.handler.IMessageHandler;
import pl.treefrog.phobos.core.message.factory.IMessageFactory;
import pl.treefrog.phobos.core.processor.IProcessor;
import pl.treefrog.phobos.core.state.IProcessingStateController;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public interface IProcessingNode {

    String getNodeName();

    IMessageFactory getMessageFactory();

    IProcessingStateController getProcessingStateController();

    IMessageHandler getMessageHandler();

    IInputAgent getInputAgent();

    IOutputAgent getOutputAgent();

    IProcessor getProcessor();

}
