package pl.treefrog.phobos.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.state.IProcessingStateController;
import pl.treefrog.phobos.core.state.context.ProcessingContext;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PhobosException;

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
public class StatefulMessageHandler extends MessageHandler {

    private static final Logger log = LoggerFactory.getLogger(StatefulMessageHandler.class);

    protected IProcessingStateController processingStateController;

    @Override
    public void init(IProcessingNode nodeConfig) throws PhobosException {
        super.init(nodeConfig);

        processingStateController = nodeConfig.getProcessingStateController();
        if (processingStateController == null) {
            log.warn("[" + parentProcNode.getNodeName() + "] MessageHandler has no processing state controller provided");
        }

    }

    @Override
    public void processMessage(Message message) throws PhobosException {

        ProcessingContext processingContext = processingStateController.retrieveProcessingContext(message);
        PhobosAssert.assertNotNull("Processing context retrieved should not be null", processingContext);

        try {

            processingContext.beforeMessageProcessing(message);

            processor.processMessage(message, processingContext);

        } finally {
            processingContext.afterMessageProcessing(message);
        }
    }
}
