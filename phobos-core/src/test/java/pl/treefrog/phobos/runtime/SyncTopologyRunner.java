package pl.treefrog.phobos.runtime;

import pl.treefrog.phobos.core.ProcessingNode;
import pl.treefrog.phobos.core.api.IExecutor;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.message.ControlHeader;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.processor.BaseProcessor;
import pl.treefrog.phobos.core.state.context.IProcessingContext;
import pl.treefrog.phobos.exception.PhobosException;
import pl.treefrog.phobos.runtime.container.IProcessingContainer;
import pl.treefrog.phobos.runtime.definition.TopologyDefGraph;
import pl.treefrog.phobos.runtime.definition.parser.SimpleTopologyDefParser;
import pl.treefrog.phobos.transport.QueueTestHelper;
import pl.treefrog.phobos.transport.mem.sync.DirectCallTransport;

import java.util.Arrays;
import java.util.List;

public class SyncTopologyRunner {

    public static void main(String[] args) throws PhobosException {
        //parse topology definition
        SimpleTopologyDefParser parser = new SimpleTopologyDefParser();
        TopologyDefGraph defGraph = parser.parse(Arrays.asList(new String[]{"-[InA]->(A)-[A2B]->(B)"}));

        //create runtime structure in single jvm container
        TopologyBuilder syncTopoBuilder = new TopologyBuilder(new BaseInputAgentGenStrategy(), new BaseOutputAgentGenStrategy());
        IProcessingContainer runtimeContainer = syncTopoBuilder.buildProcessingTopology(defGraph);

        //inject low level implementations
        DirectCallTransport tInA = QueueTestHelper.createSyncTransport();
        runtimeContainer.getInputChannels().get("InA").registerInputTransport(tInA);
        DirectCallTransport tAB = QueueTestHelper.createSyncTransport();
        runtimeContainer.getInputChannels().get("A2B").registerInputTransport(tAB);
        runtimeContainer.getOutputChannels().get("A2B").registerOutputTransport(tAB);

        //processing node A
        ProcessingNode nodeA = runtimeContainer.getProcessingNode("A");
        BaseProcessor procA = new BaseProcessor("baseProcA");
        procA.setExecutor(new IExecutor<Message>() {
            @Override
            public void processMessage(Message message, IOutputAgent outputAgent, IProcessingContext processingContext) throws PhobosException {
                System.out.println("Node A: " + message.getId());
                outputAgent.sendMessage("A2B", message, processingContext);
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return Arrays.asList(new String[]{"A2B"});
            }

            @Override
            public boolean acceptsMessage(Message message) {
                return true;
            }
        });

        nodeA.setProcessorInternal(procA);

        //processing node B
        ProcessingNode nodeB = runtimeContainer.getProcessingNode("B");
        BaseProcessor procB = new BaseProcessor("baseProcB");
        procB.setExecutor(new IExecutor<Message>() {
            @Override
            public void processMessage(Message message, IOutputAgent outputAgent, IProcessingContext processingContext) {
                System.out.println("Node B: " + message.getId());
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return null;
            }

            @Override
            public boolean acceptsMessage(Message message) {
                return true;
            }
        });

        nodeB.setProcessorInternal(procB);

        runtimeContainer.init();
        runtimeContainer.start();

        Message msg = new Message(new ControlHeader());
        msg.setId(666);

        tInA.sendMessage(msg);
    }
}
