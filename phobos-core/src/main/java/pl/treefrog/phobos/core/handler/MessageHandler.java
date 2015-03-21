package pl.treefrog.phobos.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.IComponentLifecycle;
import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.processor.IProcessor;
import pl.treefrog.phobos.core.state.context.ProcessingContext;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PlatformException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */

/*
 *  Message Handler is processing entry point of the processing node.
 *  It's triggered by message listener (in case active thread is managed within node) or
 *  by (input) transport when active thread is outside the processing node
 *  Another responsibility of the handler is to recreate processing context (state) and
 *  pass it along processing components path.
 **/
public class MessageHandler implements IMessageHandler, IComponentLifecycle {

    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    protected IProcessingNode parentProcNode;
    protected IProcessor processor;

    @Override
    public void init(IProcessingNode nodeConfig) throws PlatformException {
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
    public void processMessage(Message message) throws PlatformException {
        processor.processMessage(message, new ProcessingContext());
    }
}
