package pl.treefrog.phobos.runtime.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.ProcessingNode;
import pl.treefrog.phobos.core.channel.ChannelSet;
import pl.treefrog.phobos.core.channel.input.InputAgent;
import pl.treefrog.phobos.core.channel.input.InputChannel;
import pl.treefrog.phobos.core.channel.output.OutputAgent;
import pl.treefrog.phobos.core.channel.output.OutputChannel;
import pl.treefrog.phobos.exception.PhobosException;

import java.util.HashMap;
import java.util.Map;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */

/*
 * This basic implementation for processing topology container (single jvm)
 * It holds all processing nodes, input / output channels -> i.e. skeleton of processing topology.
 *
 * Having processing nodes and channels in one place, it's easy to "stuff" them with processor / executors implementations
 * and with desired transports
 */
public class ProcessingContainer implements IProcessingContainer {

    private static final Logger log = LoggerFactory.getLogger(ProcessingContainer.class);

    private Map<String, ProcessingNode> processingNodes = new HashMap<>();
    private Map<String, InputChannel> inputChannels = new HashMap<>();
    private Map<String, OutputChannel> outputChannels = new HashMap<>();

    @Override
    public void registerProcessingNode(ProcessingNode node) {
        log.debug("Registering processing node: " + node.getNodeName());

        processingNodes.put(node.getNodeName(), node);

        OutputAgent outputAgent = node.getOutputAgentInternal();
        if (outputAgent != null) {
            ChannelSet<OutputChannel> outputs = outputAgent.getChannelSet();
            if (outputs != null && outputs.getRegisteredChannelIds() != null
                    && !outputs.getRegisteredChannelIds().isEmpty()) {
                for (String outputId : outputs.getRegisteredChannelIds()) {
                    OutputChannel output = outputs.getChannel(outputId);
                    outputChannels.put(output.getChannelId(), output);
                }
            }
        } else {
            log.warn("No output agent found for given node: " + node.getNodeName());
        }

        InputAgent inputAgent = node.getInputAgentInternal();

        ChannelSet<InputChannel> inputs = inputAgent.getChannelSet();
        if (inputs != null && inputs.getRegisteredChannelIds() != null
                && !inputs.getRegisteredChannelIds().isEmpty()) {
            for (String inputId : inputs.getRegisteredChannelIds()) {
                InputChannel input = inputs.getChannel(inputId);
                inputChannels.put(input.getChannelId(), input);
            }
        } else {
            log.warn("No input agent found for given node: " + node.getNodeName());
        }
    }

    public ProcessingNode getProcessingNode(String nodeId) {
        return processingNodes.get(nodeId);
    }

    @Override
    public Map<String, InputChannel> getInputChannels() {
        return inputChannels;
    }

    @Override
    public Map<String, OutputChannel> getOutputChannels() {
        return outputChannels;
    }

    @Override
    public void init() throws PhobosException {
        log.info("INIT processing phase triggered");
        for (ProcessingNode procNode : processingNodes.values()) {
            procNode.init();
        }
    }

    @Override
    public void start() throws PhobosException {
        log.info("START processing phase triggered");
        for (ProcessingNode procNode : processingNodes.values()) {
            procNode.start();
        }
    }

    @Override
    public void stop() throws PhobosException {
        log.info("STOP processing phase triggered");
        for (ProcessingNode procNode : processingNodes.values()) {
            procNode.stop();
        }
    }
}
