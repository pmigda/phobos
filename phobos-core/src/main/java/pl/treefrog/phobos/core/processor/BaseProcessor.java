package pl.treefrog.phobos.core.processor;

import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.core.api.IExecutor;
import pl.treefrog.phobos.core.msg.Message;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PlatformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */

/**
 * Handles base processing logic (provided through executor)
 */
public class BaseProcessor extends AbstractProcessor {

    private static final Logger log = LoggerFactory.getLogger(BaseProcessor.class);

    protected IExecutor executor;

    public BaseProcessor(String processorId) {
        super(processorId);
    }

    @Override
    public void init(IProcessingNode nodeConfig) throws PlatformException {
        super.init(nodeConfig);

        log.info("["+parentProcNode.getNodeName()+"]["+processorId+"] Initializing base processor");

        PhobosAssert.assertNotNull("Executor is required for processing", executor);

        if (outputAgent != null &&
                !outputAgent.checkChannelsRegistered(executor.getRequiredChannelsIds())) {
            throw new RuntimeException("No required channels registered in outputAgent: " + executor.getRequiredChannelsIds().toString());
        }

    }

    @Override
    public void processMessage(Message message) {
        if (executor != null) {
            executor.processMessage(message, outputAgent);
        }else{
            log.error("["+parentProcNode.getNodeName()+"]["+processorId+"] No executor in place while processing message");
        }
    }

    //getters & setters
    public void setExecutor(IExecutor executor) {
        this.executor = executor;
    }
}
