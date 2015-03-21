package pl.treefrog.phobos.core.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.state.context.ProcessingContext;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PlatformException;

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

    protected IProcessorPhaseListener processorPhaseListener;

    public PhaseProcessor(String processorId) {
        super(processorId);
    }

    @Override
    public void init(IProcessingNode nodeConfig) throws PlatformException {
        super.init(nodeConfig);

        log.info("[" + parentProcNode.getNodeName() + "][" + processorId + "] Initializing base processor");

        PhobosAssert.assertNotNull("ChainPhaseListener is required for processing", processorPhaseListener);

        if (processorPhaseListener != null && outputAgent != null &&
                !outputAgent.checkChannelsRegistered(processorPhaseListener.getRequiredChannelsIds())) {
            throw new RuntimeException("No required channels registered in outputAgent: " + processorPhaseListener.getRequiredChannelsIds().toString());
        }

    }

    @Override
    public void processMessage(Message message, ProcessingContext context) throws PlatformException {

        if (processorPhaseListener != null) {
            processorPhaseListener.preProcessPhase(message, context, outputAgent);
        }

        forwardMessage(message, context);

        if (processorPhaseListener != null) {
            processorPhaseListener.postProcessPhase(message, context, outputAgent);
        }

    }

    //getters & setters
    public void setProcessorPhaseListener(IProcessorPhaseListener processorPhaseListener) {
        this.processorPhaseListener = processorPhaseListener;
    }
}
