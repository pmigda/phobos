package pl.treefrog.phobos.core.processor;

import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.core.msg.Message;
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
 * Handles chained processors (e.g. control processor can precede actual processing logic)
 * Input / output agents wrap not only base processing messaging channels but control messaging channels in addition
 */
public class ChainedProcessor extends BaseProcessor {

    private static final Logger log = LoggerFactory.getLogger(ChainedProcessor.class);

    private AbstractProcessor nextProcessor;

    public ChainedProcessor(String processorId) {
        super(processorId);
    }

    @Override
    public void init(IProcessingNode nodeConfig) throws PlatformException {
        super.init(nodeConfig);

        if(nextProcessor != null) {
            nextProcessor.init(nodeConfig);
        }else{
            log.warn("["+parentProcNode.getNodeName()+"] Chained processor has no successor in chain");
        }
    }

    @Override
    public void processMessage(Message message) {

        super.processMessage(message);

        if (nextProcessor != null && shouldForward(message)) {
            nextProcessor.processMessage(message);
        }
    }

    //filter out control messages by msg type and don't forward to processing logic
    protected boolean shouldForward(Message message) {
        return true; //default impl
    }

    //IoC getters & setters
    public void setNextProcessor(AbstractProcessor nextProcessor) {
        this.nextProcessor = nextProcessor;
    }
}
