package com.treefrog.phobos.runtime;

import com.treefrog.phobos.core.ProcessingNode;
import com.treefrog.phobos.core.api.IExecutor;
import com.treefrog.phobos.core.channel.output.IOutputAgent;
import com.treefrog.phobos.core.msg.Message;
import com.treefrog.phobos.core.processor.BaseProcessor;
import com.treefrog.phobos.exception.PlatformException;
import com.treefrog.phobos.runtime.container.IProcessingContainer;
import com.treefrog.phobos.runtime.definition.TopologyDefGraph;
import com.treefrog.phobos.runtime.definition.parser.SimpleTopologyDefParser;
import com.treefrog.phobos.transport.QueueTestHelper;
import com.treefrog.phobos.transport.mem.sync.DirectCallTransport;

import java.util.Arrays;
import java.util.List;

public class SyncTopologyRunner {

    public static void main(String[] args) throws PlatformException {
        //parse topology definition
        SimpleTopologyDefParser parser = new SimpleTopologyDefParser();
        TopologyDefGraph defGraph = parser.parse(Arrays.asList(new String[]{"-[InA]->(A)-[A2B]->(B)"}));

        //create runtime structure in single jvm container
        TopologyBuilder syncTopoBuilder = new TopologyBuilder(new BaseInputAgentGenStrategy(),new BaseOutputAgentGenStrategy());
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
            public void processMessage(Message message, IOutputAgent outputAgent) {
                System.out.println("Node A: "+message.id);
                outputAgent.sendMessage("A2B", message);
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return Arrays.asList(new String[]{"A2B"});
            }
        });

        nodeA.setProcessorInternal(procA);
        tInA.setProcessor(procA);

        //processing node B
        ProcessingNode nodeB = runtimeContainer.getProcessingNode("B");
        BaseProcessor procB = new BaseProcessor("baseProcB");
        procB.setExecutor(new IExecutor() {
            @Override
            public void processMessage(Message message, IOutputAgent outputAgent) {
                System.out.println("Node B: "+message.id);
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return null;
            }
        });

        nodeB.setProcessorInternal(procB);
        tAB.setProcessor(procB);

        runtimeContainer.init();
        runtimeContainer.start();

        Message msg = new Message();
        msg.id = 666;

        tInA.sendMessage(msg);
    }
}
