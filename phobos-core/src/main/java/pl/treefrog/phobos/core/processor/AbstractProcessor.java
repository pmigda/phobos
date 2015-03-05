package pl.treefrog.phobos.core.processor;

import pl.treefrog.phobos.core.ILifecycle;
import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
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
public abstract class AbstractProcessor implements IProcessor, ILifecycle {

    private static final Logger log = LoggerFactory.getLogger(AbstractProcessor.class);

    protected String processorId;
    protected IProcessingNode parentProcNode;
    protected IOutputAgent outputAgent;

    protected AbstractProcessor(String processorId) {
        this.processorId = processorId;
    }

    @Override
    public void init(IProcessingNode nodeConfig) throws PlatformException {
        parentProcNode = nodeConfig;
        PhobosAssert.assertNotNull("Parent processing node must not be null", parentProcNode);

        outputAgent = nodeConfig.getOutputAgent();
        if (outputAgent == null){
            log.warn("["+parentProcNode.getNodeName()+"]["+processorId+"] Processor has no output agent provided");
        }
    }

    @Override
    public void start() {
        log.info("["+parentProcNode.getNodeName()+"]["+processorId+"] starting processor");
    }

    @Override
    public void stop() {
        log.info("["+parentProcNode.getNodeName()+"]["+processorId+"] stopping processor");
    }

    //IOP getters & setters
    public String getProcessorId() {
        return processorId;
    }
}
