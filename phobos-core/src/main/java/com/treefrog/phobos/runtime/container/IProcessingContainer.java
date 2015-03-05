package com.treefrog.phobos.runtime.container;

import com.treefrog.phobos.core.ProcessingNode;
import com.treefrog.phobos.core.channel.BaseChannel;
import com.treefrog.phobos.exception.PlatformException;

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

    Map<String, BaseChannel> getInputChannels();

    Map<String, BaseChannel> getOutputChannels();

    void init() throws PlatformException;

    void start();

    void stop();
}
