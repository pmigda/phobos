package pl.treefrog.phobos.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.channel.input.IInputAgent;
import pl.treefrog.phobos.core.channel.input.InputAgent;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.channel.output.OutputAgent;
import pl.treefrog.phobos.core.handler.IMessageHandler;
import pl.treefrog.phobos.core.handler.MessageHandler;
import pl.treefrog.phobos.core.handler.StatefulMessageHandler;
import pl.treefrog.phobos.core.message.factory.IMessageFactory;
import pl.treefrog.phobos.core.message.factory.MessageFactory;
import pl.treefrog.phobos.core.processor.AbstractProcessor;
import pl.treefrog.phobos.core.processor.IProcessor;
import pl.treefrog.phobos.core.state.IProcessingStateController;
import pl.treefrog.phobos.core.state.ProcessingStateController;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PhobosException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */

/**
 * ProcessingNode acts as container wrapping & managing processing logic and communication channels.
 */
public class ProcessingNode implements IProcessingNode {

    private static final Logger log = LoggerFactory.getLogger(ProcessingNode.class);

    private String nodeName;
    private String instanceId;

    private IMessageFactory messageFactory = new MessageFactory(); //default factory
    private ProcessingStateController processingStateController = new ProcessingStateController();
    private MessageHandler messageHandler = new StatefulMessageHandler();
    private InputAgent inputAgent;
    private OutputAgent outputAgent;
    private AbstractProcessor processor;

    public ProcessingNode(String nodeName) {
        this.nodeName = nodeName;
    }

    public void init() throws PhobosException {

        log.info("[" + nodeName + "] Beginning INIT lifecycle phase...");

        PhobosAssert.assertNotNull("[" + nodeName + "] No message factory provided. Message factory is a mandatory component for processing", messageFactory);

        PhobosAssert.assertNotNull("[" + nodeName + "] No state controller provided. Processing state controller is a mandatory component for processing", processingStateController);
        log.info("[" + nodeName + "] Initializing processing state controller " + processingStateController.getClass().getName());
        processingStateController.init(this);

        PhobosAssert.assertNotNull("[" + nodeName + "] No message handler provided. Message handler is a mandatory component for processing", messageHandler);
        log.info("[" + nodeName + "] Initializing message handler " + messageHandler.getClass().getName());
        messageHandler.init(this);

        if (outputAgent != null) {
            log.info("[" + nodeName + "] Initializing outputAgent " + outputAgent.getClass().getName());
            outputAgent.init(this);
        } else {
            log.warn("[" + nodeName + "] No outputAgent provided");
        }

        if (inputAgent != null) {
            log.info("[" + nodeName + "] Initializing inputAgent " + inputAgent.getClass().getName());
            inputAgent.init(this);
        } else {
            log.warn("[" + nodeName + "] No inputAgent provided");
        }

        PhobosAssert.assertNotNull("[" + nodeName + "] No processor provided. Processor is a mandatory component for processing", processor);
        log.info("[" + nodeName + "] Initializing processor " + processor.getClass().getName());
        processor.init(this);

        log.info("[" + nodeName + "] INIT lifecycle phase finished");

    }

    public void start() throws PhobosException {

        log.info("[" + nodeName + "] Beginning START lifecycle phase...");

        PhobosAssert.assertNotNull("[" + nodeName + "] No state controller provided. Processing state controller is a mandatory component for processing", processingStateController);
        log.info("[" + nodeName + "] Starting processing state controller");
        processingStateController.start();

        PhobosAssert.assertNotNull("[" + nodeName + "] No message handler provided. Message handler is a mandatory component for processing", messageHandler);
        log.info("[" + messageHandler + "] Starting message handler");
        messageHandler.start();

        if (outputAgent != null) {
            log.info("[" + nodeName + "] Starting outputAgent");
            outputAgent.start();
        }

        if (inputAgent != null) {
            log.info("[" + nodeName + "] Starting inputAgent");
            inputAgent.start();
        }

        PhobosAssert.assertNotNull("[" + nodeName + "] No processor provided. Processor is a mandatory component for processing", processor);
        log.info("[" + nodeName + "] Starting processor");
        processor.start();

        log.info("[" + nodeName + "] START lifecycle phase finished");
    }

    public void stop() throws PhobosException {

        log.info("[" + nodeName + "] Beginning STOP lifecycle phase...");

        PhobosAssert.assertNotNull("[" + nodeName + "] No state controller provided. Processing state controller is a mandatory component for processing", processingStateController);
        log.info("[" + nodeName + "] Stopping processing state controller");
        processingStateController.start();

        PhobosAssert.assertNotNull("[" + nodeName + "] No message handler provided. Message handler is a mandatory component for processing", messageHandler);
        log.info("[" + nodeName + "] Stopping message handler");
        messageHandler.start();

        if (outputAgent != null) {
            log.info("[" + nodeName + "] Stopping outputAgent");
            outputAgent.stop();
        }

        if (inputAgent != null) {
            log.info("[" + nodeName + "] Stopping inputAgent");
            inputAgent.stop();
        }

        PhobosAssert.assertNotNull("[" + nodeName + "] No processor provided. Processor is a mandatory component for processing", processor);
        log.info("[" + nodeName + "] Stopping processor");
        processor.stop();

        log.info("[" + nodeName + "] STOP lifecycle phase finished");
    }

    @Override
    public String getNodeName() {
        return nodeName;
    }

    @Override
    public IMessageFactory getMessageFactory() {
        return messageFactory;
    }

    @Override
    public IProcessingStateController getProcessingStateController() {
        return processingStateController;
    }

    @Override
    public IMessageHandler getMessageHandler() {
        return messageHandler;
    }

    @Override
    public IInputAgent getInputAgent() {
        return inputAgent;
    }

    @Override
    public IOutputAgent getOutputAgent() {
        return outputAgent;
    }

    @Override
    public IProcessor getProcessor() {
        return processor;
    }


    //IoC getters & setters
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public void setMessageFactory(IMessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    //Internal implementation
    public ProcessingStateController getProcessingStateControllerInternal() {
        return processingStateController;
    }

    public void setProcessingStateControllerInternal(ProcessingStateController processingStateController) {
        this.processingStateController = processingStateController;
    }

    public MessageHandler getMessageHandlerInternal() {
        return messageHandler;
    }

    public void setMessageHandlerInternal(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public InputAgent getInputAgentInternal() {
        return inputAgent;
    }

    public void setInputAgentInternal(InputAgent inputAgent) {
        this.inputAgent = inputAgent;
    }

    public OutputAgent getOutputAgentInternal() {
        return outputAgent;
    }

    public void setOutputAgentInternal(OutputAgent outputAgent) {
        this.outputAgent = outputAgent;
    }

    public AbstractProcessor getProcessorInternal() {
        return processor;
    }

    public void setProcessorInternal(AbstractProcessor processor) {
        this.processor = processor;
    }
}
