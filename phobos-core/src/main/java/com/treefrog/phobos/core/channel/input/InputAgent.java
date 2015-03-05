package com.treefrog.phobos.core.channel.input;

import com.treefrog.phobos.core.IProcessingNode;
import com.treefrog.phobos.core.channel.AbstractChannelAgent;
import com.treefrog.phobos.core.channel.IChannel;
import com.treefrog.phobos.core.processor.IProcessor;
import com.treefrog.phobos.exception.PhobosAssert;
import com.treefrog.phobos.exception.PlatformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class InputAgent<C extends IChannel> extends AbstractChannelAgent<C> implements IInputAgent {

    private static final Logger log = LoggerFactory.getLogger(InputAgent.class);

    protected IProcessor processor;

    @Override
    public void init(IProcessingNode nodeConfig) throws PlatformException {
        super.init(nodeConfig);

        log.info("["+parentProcNode.getNodeName()+"]["+this.hashCode()+"] Initializing input agent");

        processor = nodeConfig.getProcessor();
        PhobosAssert.assertNotNull("Processor must not be null for operation", processor);
    }

}
