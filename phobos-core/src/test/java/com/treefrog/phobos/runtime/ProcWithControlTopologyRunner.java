package com.treefrog.phobos.runtime;

import com.treefrog.phobos.core.ProcessingNode;
import com.treefrog.phobos.core.api.IExecutor;
import com.treefrog.phobos.core.channel.output.IOutputAgent;
import com.treefrog.phobos.core.msg.Message;
import com.treefrog.phobos.core.processor.BaseProcessor;
import com.treefrog.phobos.core.processor.ChainedProcessor;
import com.treefrog.phobos.exception.PlatformException;
import com.treefrog.phobos.runtime.container.IProcessingContainer;
import com.treefrog.phobos.runtime.definition.TopologyDefGraph;
import com.treefrog.phobos.runtime.definition.parser.SimpleTopologyDefParser;
import com.treefrog.phobos.transport.QueueTestHelper;
import com.treefrog.phobos.transport.mem.async.QueueManager;

import java.util.Arrays;
import java.util.List;

public class ProcWithControlTopologyRunner {

    public static void main(String[] args) throws PlatformException {
        //parse topology definition
        SimpleTopologyDefParser parser = new SimpleTopologyDefParser();
        TopologyDefGraph defGraph = parser.parse(Arrays.asList(new String[]{"(A)-[A2A]->(A)","(A)-[controlChannel]->(C)"}));

        //create runtime structure in single jvm container
        TopologyBuilder asyncTopoBuilder = new TopologyBuilder(new AsyncInputAgentGenStrategy(),new BaseOutputAgentGenStrategy());
        IProcessingContainer runtimeContainer = asyncTopoBuilder.buildProcessingTopology(defGraph);

        //provide low level implementations
        QueueManager queueManager = new QueueManager();
        QueueTestHelper.populateAsyncChannelsImpl(runtimeContainer.getInputChannels(), queueManager);
        QueueTestHelper.populateAsyncChannelsImpl(runtimeContainer.getOutputChannels(), queueManager);


        //Processing Node A
        ProcessingNode node = runtimeContainer.getProcessingNode("A");
        ChainedProcessor controlProcWrapper = new ChainedProcessor("ChainedControlProcAgent");
        controlProcWrapper.setExecutor(new IExecutor() {
            @Override
            public void processMessage(Message message, IOutputAgent outputAgent) {

                Message ctrlMsg = new Message();
                ctrlMsg.id = -999;
                ctrlMsg.content = "ControlMsg:" + message.id;
                outputAgent.sendMessage("controlChannel", ctrlMsg);
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return Arrays.asList(new String[]{"controlChannel"});
            }
        });

        BaseProcessor proc = new BaseProcessor("PrintOutProc");
        proc.setExecutor(new IExecutor() {
            @Override
            public void processMessage(Message message, IOutputAgent outputAgent) {
                System.out.println("["+Thread.currentThread().getName()+"]"+message.id);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                message.id++;
                outputAgent.sendMessage("A2A",message);
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return Arrays.asList(new String[]{"A2A"});
            }
        });

        controlProcWrapper.setNextProcessor(proc);
        node.setProcessorInternal(controlProcWrapper);

        //Control Node C
        ProcessingNode controlNode = runtimeContainer.getProcessingNode("C");
        BaseProcessor controlProcessor = new BaseProcessor("ControlProc");
        controlProcessor.setExecutor(new IExecutor() {
            @Override
            public void processMessage(Message message, IOutputAgent outputAgent) {
                System.out.println("["+Thread.currentThread().getName()+"]"+message.content);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return Arrays.asList(new String[]{});
            }
        });
        controlNode.setProcessorInternal(controlProcessor);

        runtimeContainer.init();
        runtimeContainer.start();

        Message msg = new Message();
        msg.id = 666;

        queueManager.getQueue("A2A").add(msg);
    }
}
