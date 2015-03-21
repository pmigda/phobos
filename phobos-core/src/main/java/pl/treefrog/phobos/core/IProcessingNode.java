package pl.treefrog.phobos.core;

import pl.treefrog.phobos.core.channel.input.IInputAgent;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.handler.IMessageHandler;
import pl.treefrog.phobos.core.processor.IProcessor;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public interface IProcessingNode {

    IMessageHandler getMessageHandler();

    IInputAgent getInputAgent();

    IOutputAgent getOutputAgent();

    IProcessor getProcessor();

    String getNodeName();
}
