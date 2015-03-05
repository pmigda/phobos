package com.treefrog.phobos.core;

import com.treefrog.phobos.core.channel.input.IInputAgent;
import com.treefrog.phobos.core.channel.output.IOutputAgent;
import com.treefrog.phobos.core.processor.IProcessor;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public interface IProcessingNode {

    IInputAgent getInputAgent();

    IOutputAgent getOutputAgent();

    IProcessor getProcessor();

    String getNodeName();
}
