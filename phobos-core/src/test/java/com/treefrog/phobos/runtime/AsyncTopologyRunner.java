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
import com.treefrog.phobos.transport.mem.async.QueueManager;

import java.util.Arrays;
import java.util.List;

public class AsyncTopologyRunner {

    public static void main(String[] args) throws PlatformException {
        //parse topology definition
        SimpleTopologyDefParser parser = new SimpleTopologyDefParser();
        TopologyDefGraph defGraph = parser.parse(Arrays.asList(new String[]{"(A)-[A2A]->(A)"}));

        //create runtime structure in single jvm container
        TopologyBuilder asyncTopoBuilder = new TopologyBuilder(new AsyncInputAgentGenStrategy(),new BaseOutputAgentGenStrategy());
        IProcessingContainer runtimeContainer = asyncTopoBuilder.buildProcessingTopology(defGraph);

        //provide low level implementations
        QueueManager queueManager = new QueueManager();
        QueueTestHelper.populateAsyncChannelsImpl(runtimeContainer.getInputChannels(), queueManager);
        QueueTestHelper.populateAsyncChannelsImpl(runtimeContainer.getOutputChannels(), queueManager);

        //processing node A
        ProcessingNode node = runtimeContainer.getProcessingNode("A");
        BaseProcessor proc = new BaseProcessor("PrintOutProc");
        proc.setExecutor(new IExecutor() {
            @Override
            public void processMessage(Message message, IOutputAgent outputAgent) {
                System.out.println(message.id);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                message.id++;
                outputAgent.sendMessage("A2A", message);
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return Arrays.asList(new String[]{"A2A"});
            }
        });

        node.setProcessorInternal(proc);

        runtimeContainer.init();
        runtimeContainer.start();

        Message msg = new Message();
        msg.id = 666;

        queueManager.getQueue("A2A").add(msg);
    }


}
