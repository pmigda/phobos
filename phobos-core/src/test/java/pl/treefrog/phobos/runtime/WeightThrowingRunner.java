package pl.treefrog.phobos.runtime;

import pl.treefrog.phobos.core.ProcessingNode;
import pl.treefrog.phobos.core.api.IExecutor;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.control.termination.WeightThrowingChannelListener;
import pl.treefrog.phobos.core.control.termination.WeightThrowingControllerExecutor;
import pl.treefrog.phobos.core.control.termination.WeightThrowingProcessorListener;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.message.PayloadMessage;
import pl.treefrog.phobos.core.processor.BaseProcessor;
import pl.treefrog.phobos.core.processor.PhaseProcessor;
import pl.treefrog.phobos.core.state.context.ProcessingContext;
import pl.treefrog.phobos.exception.PlatformException;
import pl.treefrog.phobos.runtime.container.IProcessingContainer;
import pl.treefrog.phobos.runtime.definition.TopologyDefGraph;
import pl.treefrog.phobos.runtime.definition.parser.SimpleTopologyDefParser;
import pl.treefrog.phobos.transport.QueueTestHelper;
import pl.treefrog.phobos.transport.mem.async.QueueManager;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class WeightThrowingRunner {

    public static void main(String[] args) throws PlatformException {
        //parse topology definition
        SimpleTopologyDefParser parser = new SimpleTopologyDefParser();
        TopologyDefGraph defGraph = parser.parse(Arrays.asList(new String[]{
                "-[beginQ]->(Z)-[rootInputQ]->(A)",
                "(A)-[controlChannel]->(Z)",
                "(A)-[A2B]->(B)",
                "(B)-[controlChannel]->(Z)",
                "(A)-[A2C]->(C)",
                "(C)-[controlChannel]->(Z)"}));

        //create runtime structure in single jvm container
        TopologyBuilder asyncTopoBuilder = new TopologyBuilder(new AsyncInputAgentGenStrategy(), new BaseOutputAgentGenStrategy());
        IProcessingContainer runtimeContainer = asyncTopoBuilder.buildProcessingTopology(defGraph);

        //provide low level implementations
        QueueManager queueManager = new QueueManager();
        QueueTestHelper.populateAsyncInputChannelsImpl(runtimeContainer.getInputChannels(), queueManager);
        QueueTestHelper.populateAsyncOutputChannelsImpl(runtimeContainer.getOutputChannels(), queueManager);

        ProcessingNode controlNode = runtimeContainer.getProcessingNode("Z");
        BaseProcessor controlProc = new BaseProcessor("Weight Throwing Controller Processor");
        controlProc.setExecutor(new WeightThrowingControllerExecutor());
        controlNode.setProcessorInternal(controlProc);

        ProcessingNode nodeA = runtimeContainer.getProcessingNode("A");
        IExecutor executorA = new IExecutor() {
            @Override
            public void processMessage(Message message, IOutputAgent outputAgent, ProcessingContext context) throws PlatformException {
                Random random = new Random();
                System.out.println("ProcA received: " + message.getId());

                PayloadMessage<String> msg2B = new PayloadMessage(random.nextInt());
                msg2B.setPayload("MSG A2B");
                System.out.println("ProcA sending to B: " + msg2B.getId());
                outputAgent.sendMessage("A2B", msg2B, context);

                PayloadMessage<String> msg2C = new PayloadMessage(random.nextInt());
                msg2C.setPayload("MSG A2C");
                System.out.println("ProcA sending to C: " + msg2C.getId());
                outputAgent.sendMessage("A2C", msg2C, context);
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return Arrays.asList(new String[]{"A2B", "A2C"});
            }
        };
        setupProcessingNode(nodeA, "Proc A1", "Proc A2", executorA);

        ProcessingNode nodeB = runtimeContainer.getProcessingNode("B");
        IExecutor executorB = new IExecutor<PayloadMessage<String>>() {
            @Override
            public void processMessage(PayloadMessage<String> message, IOutputAgent outputAgent, ProcessingContext context) throws PlatformException {
                System.out.println("ProcB received: " + message.getId());
                System.out.println("ProcB payload: " + message.getPayload());
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return Arrays.asList(new String[]{});
            }
        };
        setupProcessingNode(nodeB, "Proc B1", "Proc B2", executorB);

        ProcessingNode nodeC = runtimeContainer.getProcessingNode("C");
        IExecutor executorC = new IExecutor<PayloadMessage<String>>() {
            @Override
            public void processMessage(PayloadMessage<String> message, IOutputAgent outputAgent, ProcessingContext context) throws PlatformException {
                System.out.println("ProcC received: " + message.getId());
                System.out.println("ProcC payload: " + message.getPayload());
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return Arrays.asList(new String[]{});
            }
        };
        setupProcessingNode(nodeC, "Proc C1", "Proc C2", executorC);

        runtimeContainer.init();
        runtimeContainer.start();

        PayloadMessage msg = new PayloadMessage(666);

        queueManager.getQueue("beginQ").add(msg);

    }

    private static void setupProcessingNode(ProcessingNode node, String phaseProcName, String baseProcName, IExecutor executor) {
        PhaseProcessor proc1 = new PhaseProcessor(phaseProcName);
        proc1.setProcessorPhaseListener(new WeightThrowingProcessorListener());
        BaseProcessor proc2 = new BaseProcessor(baseProcName);
        proc2.setExecutor(executor);
        proc1.setNextProcessor(proc2);
        node.setProcessorInternal(proc1);

        node.getOutputAgentInternal().setAgentPhaseListener(new WeightThrowingChannelListener());
    }
}
