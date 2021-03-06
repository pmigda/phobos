package pl.treefrog.phobos.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.IComponentLifecycle;
import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.processor.IProcessor;
import pl.treefrog.phobos.core.state.context.ProcessingContext;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PhobosException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class MessageHandler implements IMessageHandler, IComponentLifecycle {

    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    protected IProcessingNode parentProcNode;
    protected IProcessor processor;

    @Override
    public void init(IProcessingNode nodeConfig) throws PhobosException {
        parentProcNode = nodeConfig;
        PhobosAssert.assertNotNull("Parent processing node must not be null", parentProcNode);

        processor = nodeConfig.getProcessor();
        if (processor == null) {
            log.warn("[" + parentProcNode.getNodeName() + "] MessageHandler has no processor provided");
        }

    }

    @Override
    public void start() {
        //NOP
    }

    @Override
    public void stop() {
        //NOP
    }

    @Override
    public void processMessage(Message message) throws PhobosException {
        processor.processMessage(message, new ProcessingContext(parentProcNode.getNodeName()));
    }

}
