package pl.treefrog.phobos.runtime;

import pl.treefrog.phobos.core.ProcessingNode;
import pl.treefrog.phobos.core.api.IExecutor;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.message.PayloadMessage;
import pl.treefrog.phobos.core.processor.BaseProcessor;
import pl.treefrog.phobos.core.state.context.ProcessingContext;
import pl.treefrog.phobos.exception.PlatformException;
import pl.treefrog.phobos.runtime.container.IProcessingContainer;
import pl.treefrog.phobos.runtime.definition.TopologyDefGraph;
import pl.treefrog.phobos.runtime.definition.parser.SimpleTopologyDefParser;
import pl.treefrog.phobos.transport.QueueTestHelper;
import pl.treefrog.phobos.transport.mem.sync.DirectCallTransport;

import java.util.Arrays;
import java.util.List;

public class SyncTopologyRunner {

    public static void main(String[] args) throws PlatformException {
        //parse topology definition
        SimpleTopologyDefParser parser = new SimpleTopologyDefParser();
        TopologyDefGraph defGraph = parser.parse(Arrays.asList(new String[]{"-[InA]->(A)-[A2B]->(B)"}));

        //create runtime structure in single jvm container
        TopologyBuilder syncTopoBuilder = new TopologyBuilder(new BaseInputAgentGenStrategy(), new BaseOutputAgentGenStrategy());
        IProcessingContainer runtimeContainer = syncTopoBuilder.buildProcessingTopology(defGraph);

        //inject low level implementations
        DirectCallTransport tInA = QueueTestHelper.createSyncTransport();
        runtimeContainer.getInputChannels().get("InA").setTransport(tInA);
        DirectCallTransport tAB = QueueTestHelper.createSyncTransport();
        runtimeContainer.getInputChannels().get("A2B").setTransport(tAB);
        runtimeContainer.getOutputChannels().get("A2B").setTransport(tAB);

        //processing node A
        ProcessingNode nodeA = runtimeContainer.getProcessingNode("A");
        BaseProcessor procA = new BaseProcessor("baseProcA");
        procA.setExecutor(new IExecutor() {
            @Override
            public void processMessage(Message message, IOutputAgent outputAgent, ProcessingContext context) throws PlatformException {
                System.out.println("Node A: " + message.getId());
                outputAgent.sendMessage("A2B", message, context);
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return Arrays.asList(new String[]{"A2B"});
            }
        });

        nodeA.setProcessorInternal(procA);

        //processing node B
        ProcessingNode nodeB = runtimeContainer.getProcessingNode("B");
        BaseProcessor procB = new BaseProcessor("baseProcB");
        procB.setExecutor(new IExecutor() {
            @Override
            public void processMessage(Message message, IOutputAgent outputAgent, ProcessingContext context) {
                System.out.println("Node B: " + message.getId());
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return null;
            }
        });

        nodeB.setProcessorInternal(procB);

        runtimeContainer.init();
        runtimeContainer.start();

        Message msg = new PayloadMessage(666);

        tInA.sendMessage(msg);
    }
}
