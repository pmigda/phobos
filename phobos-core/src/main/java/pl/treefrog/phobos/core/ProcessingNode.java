package pl.treefrog.phobos.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.channel.input.IInputAgent;
import pl.treefrog.phobos.core.channel.input.InputAgent;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.channel.output.OutputAgent;
import pl.treefrog.phobos.core.handler.IMessageHandler;
import pl.treefrog.phobos.core.handler.MessageHandler;
import pl.treefrog.phobos.core.processor.AbstractProcessor;
import pl.treefrog.phobos.core.processor.IProcessor;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PlatformException;

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

    private MessageHandler messageHandler = new MessageHandler();
    private InputAgent inputAgent;
    private OutputAgent outputAgent;
    private AbstractProcessor processor;

    public ProcessingNode(String nodeName) {
        this.nodeName = nodeName;
    }

    public void init() throws PlatformException {

        log.info("[" + nodeName + "] Beginning INIT lifecycle phase...");

        if (messageHandler != null) {
            log.info("[" + nodeName + "] Initializing message handler " + messageHandler.getClass().getName());
            messageHandler.init(this);
        } else {
            log.warn("[" + nodeName + "] No message handler provided");
        }

        if (outputAgent != null) {
            log.info("[" + nodeName + "] Initializing outputAgent " + outputAgent.getClass().getName());
            outputAgent.init(this);
        } else {
            log.warn("[" + nodeName + "] No outputAgent provided");
        }

        PhobosAssert.assertNotNull("[" + nodeName + "] No processor provided. Processor is a mandatory component for processing", processor);
        log.info("[" + nodeName + "] Initializing processor " + processor.getClass().getName());
        processor.init(this);

        if (inputAgent != null) {
            log.info("[" + nodeName + "] Initializing inputAgent " + inputAgent.getClass().getName());
            inputAgent.init(this);
        } else {
            log.warn("[" + nodeName + "] No inputAgent provided");
        }

        log.info("[" + nodeName + "] INIT lifecycle phase finished");

    }

    public void start() throws PlatformException {

        log.info("[" + nodeName + "] Beginning START lifecycle phase...");

        if (messageHandler != null) {
            log.info("[" + messageHandler + "] Starting message handler");
            messageHandler.start();
        }

        if (outputAgent != null) {
            log.info("[" + nodeName + "] Starting outputAgent");
            outputAgent.start();
        }

        PhobosAssert.assertNotNull("[" + nodeName + "] No processor provided. Processor is a mandatory component for processing", processor);
        log.info("[" + nodeName + "] Starting processor");
        processor.start();


        if (inputAgent != null) {
            log.info("[" + nodeName + "] Starting inputAgent");
            inputAgent.start();
        }

        log.info("[" + nodeName + "] START lifecycle phase finished");
    }

    public void stop() throws PlatformException {

        log.info("[" + nodeName + "] Beginning STOP lifecycle phase...");

        if (messageHandler != null) {
            log.info("[" + nodeName + "] Stopping message handler");
            messageHandler.stop();
        }

        if (outputAgent != null) {
            log.info("[" + nodeName + "] Stopping outputAgent");
            outputAgent.stop();
        }

        PhobosAssert.assertNotNull("[" + nodeName + "] No processor provided. Processor is a mandatory component for processing", processor);
        log.info("[" + nodeName + "] Stopping processor");
        processor.stop();


        if (inputAgent != null) {
            log.info("[" + nodeName + "] Stopping inputAgent");
            inputAgent.stop();
        }

        log.info("[" + nodeName + "] STOP lifecycle phase finished");
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
    public IProcessor getProcessor() {
        return processor;
    }

    @Override
    public IOutputAgent getOutputAgent() {
        return outputAgent;
    }

    @Override
    public String getNodeName() {
        return nodeName;
    }

    //getters & setters
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }


    //Internal implementation & IoC management
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
