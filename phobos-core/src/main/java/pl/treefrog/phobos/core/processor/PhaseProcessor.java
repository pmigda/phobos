package pl.treefrog.phobos.core.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.core.api.IExecutor;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.state.context.IProcessingContext;
import pl.treefrog.phobos.exception.PhobosException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */

/**
 * Handles base processing logic (provided through executor)
 */
public class PhaseProcessor extends AbstractProcessor {

    private static final Logger log = LoggerFactory.getLogger(PhaseProcessor.class);

    protected IExecutor preProcessExecutor;
    protected IExecutor postProcessExecutor;

    public PhaseProcessor(String processorId) {
        super(processorId);
    }


    @Override
    public void init(IProcessingNode nodeConfig) throws PhobosException {
        super.init(nodeConfig);

        log.info("[" + parentProcNode.getNodeName() + "][" + processorId + "] Initializing phase processor");

        if (outputAgent != null && preProcessExecutor != null &&
                !outputAgent.checkChannelsRegistered(preProcessExecutor.getRequiredChannelsIds())) {
            throw new RuntimeException("No required channels registered in outputAgent for preProcessExecutor: " + preProcessExecutor.getRequiredChannelsIds().toString());
        }

        if (outputAgent != null && postProcessExecutor != null &&
                !outputAgent.checkChannelsRegistered(postProcessExecutor.getRequiredChannelsIds())) {
            throw new RuntimeException("No required channels registered in outputAgent for postProcessExecutor: " + postProcessExecutor.getRequiredChannelsIds().toString());
        }

    }

    @Override
    public void processMessage(Message message, IProcessingContext processingContext) throws PhobosException {

        boolean shouldForward = true;

        if (preProcessExecutor != null && preProcessExecutor.acceptsMessage(message)) {
            preProcessExecutor.processMessage(message, outputAgent, processingContext);
        }
        if (shouldForward) {
            forwardMessage(message, processingContext);
        }

        if (postProcessExecutor != null && postProcessExecutor.acceptsMessage(message)) {
            postProcessExecutor.processMessage(message, outputAgent, processingContext);
        }

    }

    //getters & setters
    public void setPreProcessExecutor(IExecutor preProcessExecutor) {
        this.preProcessExecutor = preProcessExecutor;
    }

    public void setPostProcessExecutor(IExecutor postProcessExecutor) {
        this.postProcessExecutor = postProcessExecutor;
    }
}
