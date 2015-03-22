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
import pl.treefrog.phobos.transport.mem.async.QueueManager;

import java.util.Arrays;
import java.util.List;

public class ProcWithControlTopologyRunner {

    public static void main(String[] args) throws PlatformException {
        //parse topology definition
        SimpleTopologyDefParser parser = new SimpleTopologyDefParser();
        TopologyDefGraph defGraph = parser.parse(Arrays.asList(new String[]{"(A)-[A2A]->(A)", "(A)-[controlChannel]->(C)"}));

        //create runtime structure in single jvm container
        TopologyBuilder asyncTopoBuilder = new TopologyBuilder(new AsyncInputAgentGenStrategy(), new BaseOutputAgentGenStrategy());
        IProcessingContainer runtimeContainer = asyncTopoBuilder.buildProcessingTopology(defGraph);

        //provide low level implementations
        QueueManager queueManager = new QueueManager();
        QueueTestHelper.populateAsyncInputChannelsImpl(runtimeContainer.getInputChannels(), queueManager);
        QueueTestHelper.populateAsyncOutputChannelsImpl(runtimeContainer.getOutputChannels(), queueManager);


        //Processing Node A
        ProcessingNode node = runtimeContainer.getProcessingNode("A");
        BaseProcessor controlProcWrapper = new BaseProcessor("ChainedControlProcAgent");
        controlProcWrapper.setExecutor(new IExecutor() {
            @Override
            public void processMessage(Message message, IOutputAgent outputAgent, ProcessingContext context) throws PlatformException {

                PayloadMessage<String> ctrlMsg = new PayloadMessage(-999);
                ctrlMsg.setPayload("ControlMsg:" + message.getId());
                outputAgent.sendMessage("controlChannel", ctrlMsg, context);
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return Arrays.asList(new String[]{"controlChannel"});
            }
        });

        BaseProcessor proc = new BaseProcessor("PrintOutProc");
        proc.setExecutor(new IExecutor() {
            @Override
            public void processMessage(Message message, IOutputAgent outputAgent, ProcessingContext context) throws PlatformException {
                System.out.println("[" + Thread.currentThread().getName() + "]" + message.getId());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                message.setId(message.getId() + 1);
                outputAgent.sendMessage("A2A", message, context);
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
        controlProcessor.setExecutor(new IExecutor<PayloadMessage<String>>() {
            @Override
            public void processMessage(PayloadMessage message, IOutputAgent outputAgent, ProcessingContext context) {
                System.out.println("[" + Thread.currentThread().getName() + "]" + message.getPayload());
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

        Message msg = new Message(666);

        queueManager.getQueue("A2A").add(msg);
    }
}
