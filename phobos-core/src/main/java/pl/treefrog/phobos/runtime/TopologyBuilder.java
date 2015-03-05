package pl.treefrog.phobos.runtime;

import pl.treefrog.phobos.core.ProcessingNode;
import pl.treefrog.phobos.core.channel.BaseChannel;
import pl.treefrog.phobos.core.channel.input.InputAgent;
import pl.treefrog.phobos.core.channel.output.OutputAgent;
import pl.treefrog.phobos.runtime.container.IProcessingContainer;
import pl.treefrog.phobos.runtime.container.ProcessingContainer;
import pl.treefrog.phobos.runtime.definition.EdgeDef;
import pl.treefrog.phobos.runtime.definition.ProcNodeDef;
import pl.treefrog.phobos.runtime.definition.TopologyDefGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */

/**
 * This is simple builder taking abstract topology definition and converting it into processing skeleton.
 * No node processors / channels transports implementation injected yet. Only processing framework artifacts.
 * In order to make management possible all bootstrapped topology is wrapped into processing container.
 *
 * Generation strategies enable to plug eligible implementation of processing framework artifacts
 * (sync / async communication agents and channels, async listeners)
 */
public class TopologyBuilder {

    private static final Logger log = LoggerFactory.getLogger(TopologyBuilder.class);

    protected InputAgentGenStrategy inputAgentGenerator;
    protected OutputAgentGenStrategy outputAgentGenerator;

    public TopologyBuilder(InputAgentGenStrategy inputAgentGenerator, OutputAgentGenStrategy outputAgentGenerator) {
        this.inputAgentGenerator = inputAgentGenerator;
        this.outputAgentGenerator = outputAgentGenerator;
    }

    private ProcessingNode buildProcessingNode(String nodeName, boolean inputAgentOpt, boolean outputAgentOpt) {
        ProcessingNode result = new ProcessingNode(nodeName);

        if (inputAgentOpt) {
            InputAgent inputAgent = inputAgentGenerator.buildInputAgent();
            result.setInputAgentInternal(inputAgent);
        }

        if (outputAgentOpt) {
            OutputAgent outputAgent = outputAgentGenerator.buildOutputAgent();
            result.setOutputAgentInternal(outputAgent);
        }

        return result;
    }

    public IProcessingContainer buildProcessingTopology(TopologyDefGraph topologyDefGraph) {
        log.info("*** Processing topology build bootstrap process started ***");
        IProcessingContainer procContainer = new ProcessingContainer();

        for (ProcNodeDef nodeDef : topologyDefGraph.getProcNodeDefs()) {
            String nodeId = nodeDef.getNodeId();

            if (procContainer.getProcessingNode(nodeId) == null) {
                log.debug("Node with id: "+nodeId+" not known. Creating new processing node.");

                boolean hasInputs = nodeDef.getInputEdgesMap().size() != 0;
                boolean hasOutputs = nodeDef.getOutputEdgesMap().size() != 0;
                ProcessingNode newNode = buildProcessingNode(nodeId, hasInputs, hasOutputs);

                if (hasInputs) {
                    for (EdgeDef inputEdge : nodeDef.getInputEdgesMap().values()) {
                        BaseChannel inputChannel = procContainer.getInputChannels().get(inputEdge.getEdgeId());
                        if (inputChannel == null) {
                            log.debug("Input channel with id: "+inputEdge.getEdgeId()+" not known. Creating new input channel.");
                            inputChannel = inputAgentGenerator.buildInputChannel();
                            inputChannel.setChannelId(inputEdge.getEdgeId());
                        }
                        newNode.getInputAgentInternal().getChannelSet().registerChannel(inputChannel);
                    }
                }

                if (hasOutputs) {
                    for (EdgeDef outputEdge : nodeDef.getOutputEdgesMap().values()) {
                        BaseChannel outputChannel = procContainer.getOutputChannels().get(outputEdge.getEdgeId());
                        if (outputChannel == null) {
                            log.debug("Output channel with id: "+outputEdge.getEdgeId()+" not known. Creating new output channel.");
                            outputChannel = outputAgentGenerator.buildOutputChannel();
                            outputChannel.setChannelId(outputEdge.getEdgeId());
                        }
                        newNode.getOutputAgentInternal().getChannelSet().registerChannel(outputChannel);
                    }
                }

                //All channels adjacent to node recognized are registered within container automatically.
                //No need to register them separately.
                procContainer.registerProcessingNode(newNode);
            }

        }

        log.info("*** Processing topology build bootstrap process finished ***");
        return procContainer;
    }


}
