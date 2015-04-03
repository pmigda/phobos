package pl.treefrog.phobos.runtime;

import pl.treefrog.phobos.core.ProcessingNode;
import pl.treefrog.phobos.core.api.IExecutor;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.control.termination.WTControllerExecutor;
import pl.treefrog.phobos.core.control.termination.WTOutputAgentListener;
import pl.treefrog.phobos.core.control.termination.WTPostProcessExecutor;
import pl.treefrog.phobos.core.control.termination.WTPreProcessExecutor;
import pl.treefrog.phobos.core.control.transaction.TxForwardPredicate;
import pl.treefrog.phobos.core.control.transaction.TxPreProcessExecutor;
import pl.treefrog.phobos.core.handler.MessageHandler;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.message.Payload;
import pl.treefrog.phobos.core.message.TransactionHeader;
import pl.treefrog.phobos.core.message.factory.TxMessageFactory;
import pl.treefrog.phobos.core.processor.BaseProcessor;
import pl.treefrog.phobos.core.processor.PhaseProcessor;
import pl.treefrog.phobos.core.state.context.IProcessingContext;
import pl.treefrog.phobos.core.state.context.TransactionContext;
import pl.treefrog.phobos.core.state.manager.TransactionStateManager;
import pl.treefrog.phobos.exception.PhobosException;
import pl.treefrog.phobos.executor.TopicChannelExecutor;
import pl.treefrog.phobos.runtime.container.IProcessingContainer;
import pl.treefrog.phobos.runtime.definition.TopologyDefGraph;
import pl.treefrog.phobos.runtime.definition.parser.SimpleTopologyDefParser;
import pl.treefrog.phobos.transport.QueueTestHelper;
import pl.treefrog.phobos.transport.mem.async.QueueManager;

import java.util.Arrays;
import java.util.List;

import static pl.treefrog.phobos.core.message.MessageType.DEFAULT_MESSAGE;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class WeightThrowingRunner {

    public static void main(String[] args) throws PhobosException {
        //parse topology definition
        SimpleTopologyDefParser parser = new SimpleTopologyDefParser();
        TopologyDefGraph defGraph = parser.parse(Arrays.asList(new String[]{
                "-[beginQ]->(Z)-[rootInputQ]->(A)",
                "(A)-[controlChannel]->(Z)",
                "(A)-[A2B]->(B)",
                "(B)-[controlChannel]->(Z)",
                "(A)-[A2C]->(C)",
                "(C)-[controlChannel]->(Z)",
                "(Z)-[controlBcastTopic]->(T)", //this emulates topic behaviour
                "(T)-[T2A]->(A)",
                "(T)-[T2B]->(B)",
                "(T)-[T2C]->(C)"}));

        //create runtime structure in single jvm container
        TopologyBuilder asyncTopoBuilder = new TopologyBuilder(new AsyncInputAgentGenStrategy(), new BaseOutputAgentGenStrategy());
        IProcessingContainer runtimeContainer = asyncTopoBuilder.buildProcessingTopology(defGraph);

        //provide low level implementations
        QueueManager queueManager = new QueueManager();
        QueueTestHelper.populateAsyncInputChannelsImpl(runtimeContainer.getInputChannels(), queueManager);
        QueueTestHelper.populateAsyncOutputChannelsImpl(runtimeContainer.getOutputChannels(), queueManager);

        ProcessingNode controlNode = runtimeContainer.getProcessingNode("Z");
        BaseProcessor controlProc = new BaseProcessor("Weight Throwing Controller Processor");
        controlProc.setExecutor(new WTControllerExecutor());
        controlNode.setProcessorInternal(controlProc);
        controlNode.setMessageFactory(new TxMessageFactory());
        controlNode.getProcessingStateControllerInternal().registerStateManager(TransactionContext.TX_CTX, new TransactionStateManager());

        ProcessingNode topicEmulatorNode = runtimeContainer.getProcessingNode("T");
        topicEmulatorNode.setMessageHandlerInternal(new MessageHandler());
        BaseProcessor topicProc = new BaseProcessor("Topic Processor");
        TopicChannelExecutor topicExec = new TopicChannelExecutor();
        topicExec.setBroadcastChannels(topicEmulatorNode.getOutputAgentInternal().getChannelSet().getRegisteredChannelIds());
        topicProc.setExecutor(topicExec);
        topicEmulatorNode.setProcessorInternal(topicProc);

        ProcessingNode nodeA = runtimeContainer.getProcessingNode("A");
        IExecutor executorA = new IExecutor<Message<TransactionHeader, ?>>() {
            @Override
            public void processMessage(Message<TransactionHeader, ?> message, IOutputAgent outputAgent, IProcessingContext processingContext) throws PhobosException {
                System.out.println("ProcA received: " + message.getId());

                Message<TransactionHeader, Payload> msg2B = outputAgent.createMessage(DEFAULT_MESSAGE, processingContext);
                msg2B.setPayload(new Payload("MSG A2B: " + message.getId()));
                System.out.println("ProcA sending to B: " + msg2B.getId());
                outputAgent.sendMessage("A2B", msg2B, processingContext);

                Message<TransactionHeader, Payload> msg2C = outputAgent.createMessage(DEFAULT_MESSAGE, processingContext);
                msg2C.setPayload(new Payload("MSG A2C"));
                System.out.println("ProcA sending to C: " + msg2C.getId());
                outputAgent.sendMessage("A2C", msg2C, processingContext);
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return Arrays.asList(new String[]{"A2B", "A2C"});
            }

            @Override
            public boolean acceptsMessage(Message message) {
                return message.getControlHeader() instanceof TransactionHeader ? true : false;
            }
        };
        setupProcessingNode(nodeA, "Proc A0", "Proc A1", "Proc A2", executorA);

        ProcessingNode nodeB = runtimeContainer.getProcessingNode("B");
        IExecutor executorB = new IExecutor<Message<TransactionHeader, ?>>() {
            @Override
            public void processMessage(Message<TransactionHeader, ?> message, IOutputAgent outputAgent, IProcessingContext processingContext) throws PhobosException {
                System.out.println("ProcB received: " + message.getId());
                System.out.println("ProcB payload: " + message.getPayload().getContent());
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return Arrays.asList(new String[]{});
            }

            @Override
            public boolean acceptsMessage(Message message) {
                return message.getControlHeader() instanceof TransactionHeader ? true : false;
            }
        };
        setupProcessingNode(nodeB, "Proc B0", "Proc B1", "Proc B2", executorB);

        ProcessingNode nodeC = runtimeContainer.getProcessingNode("C");
        IExecutor executorC = new IExecutor<Message<TransactionHeader, ?>>() {
            @Override
            public void processMessage(Message<TransactionHeader, ?> message, IOutputAgent outputAgent, IProcessingContext processingContext) throws PhobosException {
                System.out.println("ProcC received: " + message.getId());
                System.out.println("ProcC payload: " + message.getPayload().getContent());
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return Arrays.asList(new String[]{});
            }

            @Override
            public boolean acceptsMessage(Message message) {
                return message.getControlHeader() instanceof TransactionHeader ? true : false;
            }
        };
        setupProcessingNode(nodeC, "Proc C0", "Proc C1", "Proc C2", executorC);

        runtimeContainer.init();
        runtimeContainer.start();

        Message<TransactionHeader, ?> msg = new Message<>(new TransactionHeader());
        msg.setId(666);
        msg.getControlHeader().setTxId("1");
        queueManager.getQueue("beginQ").add(msg);

        Message<TransactionHeader, ?> msg2 = new Message<>(new TransactionHeader());
        msg2.setId(777);
        msg2.getControlHeader().setTxId("2");
        queueManager.getQueue("beginQ").add(msg2);

    }

    private static void setupProcessingNode(ProcessingNode node, String txProcName, String phaseProcName, String baseProcName, IExecutor executor) {
        PhaseProcessor proc0 = new PhaseProcessor(txProcName);
        proc0.setPreProcessExecutor(new TxPreProcessExecutor());
        proc0.setForwardPredicate(new TxForwardPredicate());

        PhaseProcessor proc1 = new PhaseProcessor(phaseProcName);
        proc1.setPreProcessExecutor(new WTPreProcessExecutor());
        proc1.setPostProcessExecutor(new WTPostProcessExecutor());
        proc0.setNextProcessor(proc1);

        BaseProcessor proc2 = new BaseProcessor(baseProcName);
        proc2.setExecutor(executor);

        proc1.setNextProcessor(proc2);

        node.setProcessorInternal(proc0);

        node.getProcessingStateControllerInternal().registerStateManager(TransactionContext.TX_CTX, new TransactionStateManager());
        node.setMessageFactory(new TxMessageFactory());

        node.getOutputAgentInternal().setAgentPhaseListener(new WTOutputAgentListener());
    }
}
