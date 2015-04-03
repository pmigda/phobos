package pl.treefrog.phobos.runtime.container;

import pl.treefrog.phobos.core.ProcessingNode;
import pl.treefrog.phobos.core.channel.input.InputChannel;
import pl.treefrog.phobos.core.channel.output.OutputChannel;
import pl.treefrog.phobos.exception.PhobosException;

import java.util.Map;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public interface IProcessingContainer {
    ProcessingNode getProcessingNode(String name);

    void registerProcessingNode(ProcessingNode node);

    Map<String, InputChannel> getInputChannels();

    Map<String, OutputChannel> getOutputChannels();

    void init() throws PhobosException;

    void start() throws PhobosException;

    void stop() throws PhobosException;
}
