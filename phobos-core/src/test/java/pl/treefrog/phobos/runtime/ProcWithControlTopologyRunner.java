package pl.treefrog.phobos.runtime;

import pl.treefrog.phobos.core.ProcessingNode;
import pl.treefrog.phobos.core.api.IExecutor;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.message.ControlHeader;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.message.Payload;
import pl.treefrog.phobos.core.processor.BaseProcessor;
import pl.treefrog.phobos.core.state.context.IProcessingContext;
import pl.treefrog.phobos.exception.PhobosException;
import pl.treefrog.phobos.runtime.container.IProcessingContainer;
import pl.treefrog.phobos.runtime.definition.TopologyDefGraph;
import pl.treefrog.phobos.runtime.definition.parser.SimpleTopologyDefParser;
import pl.treefrog.phobos.transport.QueueTestHelper;
import pl.treefrog.phobos.transport.mem.async.QueueManager;

import java.util.Arrays;
import java.util.List;

import static pl.treefrog.phobos.core.message.MessageType.DEFAULT_MESSAGE;

public class ProcWithControlTopologyRunner {

    public static void main(String[] args) throws PhobosException {
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
        controlProcWrapper.setExecutor(new IExecutor<Message>() {
            @Override
            public void processMessage(Message message, IOutputAgent outputAgent, IProcessingContext processingContext) throws PhobosException {

                Message ctrlMsg = outputAgent.createMessage(DEFAULT_MESSAGE, processingContext);
                ctrlMsg.setPayload(new Payload("ControlMsg:" + message.getId()));
                outputAgent.sendMessage("controlChannel", ctrlMsg, processingContext);

            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return Arrays.asList(new String[]{"controlChannel"});
            }

            @Override
            public boolean acceptsMessage(Message message) {
                return true;
            }
        });

        BaseProcessor proc = new BaseProcessor("PrintOutProc");
        proc.setExecutor(new IExecutor<Message>() {
            @Override
            public void processMessage(Message message, IOutputAgent outputAgent, IProcessingContext processingContext) throws PhobosException {
                System.out.println("[" + Thread.currentThread().getName() + "]" + message.getId());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                message.setId(message.getId() + 1);
                outputAgent.sendMessage("A2A", message, processingContext);
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return Arrays.asList(new String[]{"A2A"});
            }

            @Override
            public boolean acceptsMessage(Message message) {
                return true;
            }
        });

        controlProcWrapper.setNextProcessor(proc);
        node.setProcessorInternal(controlProcWrapper);

        //Control Node C
        ProcessingNode controlNode = runtimeContainer.getProcessingNode("C");
        BaseProcessor controlProcessor = new BaseProcessor("ControlProc");
        controlProcessor.setExecutor(new IExecutor<Message<?, Payload>>() {
            @Override
            public void processMessage(Message<?, Payload> message, IOutputAgent outputAgent, IProcessingContext processingContext) {
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

            @Override
            public boolean acceptsMessage(Message message) {
                return true;
            }
        });
        controlNode.setProcessorInternal(controlProcessor);

        runtimeContainer.init();
        runtimeContainer.start();

        Message msg = new Message(new ControlHeader());
        msg.setId(666);

        queueManager.getQueue("A2A").add(msg);
    }
}
