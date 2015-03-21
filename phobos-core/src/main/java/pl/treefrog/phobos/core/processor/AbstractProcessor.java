package pl.treefrog.phobos.core.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.IComponentLifecycle;
import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
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
public abstract class AbstractProcessor implements IProcessor, IComponentLifecycle {

    private static final Logger log = LoggerFactory.getLogger(AbstractProcessor.class);

    protected String processorId;
    protected IProcessingNode parentProcNode;
    protected IOutputAgent outputAgent;

    private AbstractProcessor nextProcessor;

    protected AbstractProcessor(String processorId) {
        this.processorId = processorId;
    }

    @Override
    public void init(IProcessingNode nodeConfig) throws PlatformException {
        parentProcNode = nodeConfig;
        PhobosAssert.assertNotNull("Parent processing node must not be null", parentProcNode);

        if (nextProcessor != null) {
            nextProcessor.init(nodeConfig);
        } else {
            log.warn("[" + parentProcNode.getNodeName() + "] Chained processor has no successor in chain");
        }

        outputAgent = nodeConfig.getOutputAgent();
        if (outputAgent == null) {
            log.warn("[" + parentProcNode.getNodeName() + "][" + processorId + "] Processor has no output agent provided");
        }
    }

    protected void forwardMessage(Message message, ProcessingContext context) throws PlatformException {
        //filter out messages by message type if necessary
        if (nextProcessor != null && acceptMessage(message)) {
            nextProcessor.processMessage(message, context);
        }
    }

    protected boolean acceptMessage(Message message) {
        return true;
    }

    @Override
    public void start() {
        log.info("[" + parentProcNode.getNodeName() + "][" + processorId + "] starting processor");
    }

    @Override
    public void stop() {
        log.info("[" + parentProcNode.getNodeName() + "][" + processorId + "] stopping processor");
    }

    //IOP getters & setters
    public String getProcessorId() {
        return processorId;
    }

    public void setNextProcessor(AbstractProcessor nextProcessor) {
        this.nextProcessor = nextProcessor;
    }

}
