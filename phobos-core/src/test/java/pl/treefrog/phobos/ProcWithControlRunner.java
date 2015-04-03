package pl.treefrog.phobos;

import pl.treefrog.phobos.core.ProcessingNode;
import pl.treefrog.phobos.core.api.IExecutor;
import pl.treefrog.phobos.core.channel.ChannelSet;
import pl.treefrog.phobos.core.channel.input.InputChannel;
import pl.treefrog.phobos.core.channel.input.async.AsyncInputAgent;
import pl.treefrog.phobos.core.channel.input.async.listener.RoundRobinListener;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.channel.output.OutputAgent;
import pl.treefrog.phobos.core.channel.output.OutputChannel;
import pl.treefrog.phobos.core.message.ControlHeader;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.message.Payload;
import pl.treefrog.phobos.core.processor.BaseProcessor;
import pl.treefrog.phobos.core.state.context.IProcessingContext;
import pl.treefrog.phobos.exception.PhobosException;
import pl.treefrog.phobos.transport.mem.async.QueueInputTransport;
import pl.treefrog.phobos.transport.mem.async.QueueManager;
import pl.treefrog.phobos.transport.mem.async.QueueOutputTransport;

import java.util.Arrays;
import java.util.List;

import static pl.treefrog.phobos.core.message.MessageType.DEFAULT_MESSAGE;

public class ProcWithControlRunner {

    public static void main(String[] args) throws PhobosException {

        //in memory queue based mom
        QueueManager queueManager = new QueueManager();
        queueManager.createQueue("A2A", 100);
        queueManager.createQueue("controlChannel", 100);

        QueueInputTransport queInputTransport = new QueueInputTransport();
        queInputTransport.setQueManager(queueManager);

        QueueOutputTransport queOutputTransport = new QueueOutputTransport();
        queOutputTransport.setQueManager(queueManager);

        QueueInputTransport controlQueInputTransport = new QueueInputTransport();
        controlQueInputTransport.setQueManager(queueManager);

        QueueOutputTransport controlQueOutputTransport = new QueueOutputTransport();
        controlQueOutputTransport.setQueManager(queueManager);


        //create base processing topology
        //input
        InputChannel inputChannel = new InputChannel();
        inputChannel.setChannelId("A2A");
        inputChannel.registerInputTransport(queInputTransport);

        ChannelSet<InputChannel> inputChannelSet = new ChannelSet<>();
        inputChannelSet.registerChannel(inputChannel);

        AsyncInputAgent inputAgent = new AsyncInputAgent();
        inputAgent.setChannelSet(inputChannelSet);
        inputAgent.setMessageListener(new RoundRobinListener());

        //output
        OutputChannel outputChannel = new OutputChannel();
        outputChannel.setChannelId("A2A");
        outputChannel.registerOutputTransport(queOutputTransport);

        OutputChannel controlChannel = new OutputChannel();
        controlChannel.setChannelId("controlChannel");
        controlChannel.registerOutputTransport(controlQueOutputTransport);

        ChannelSet<OutputChannel> outputChannelSet = new ChannelSet<>();
        outputChannelSet.registerChannel(outputChannel);
        outputChannelSet.registerChannel(controlChannel);

        OutputAgent outputAgent = new OutputAgent();
        outputAgent.setChannelSet(outputChannelSet);

        //processor
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

        //base processing node
        ProcessingNode procNode = new ProcessingNode("node_A");
        procNode.setInputAgentInternal(inputAgent);
        procNode.setOutputAgentInternal(outputAgent);
        procNode.setProcessorInternal(controlProcWrapper);
        procNode.init();
        procNode.start();

        //controller node
        InputChannel controlInputChannel = new InputChannel();
        controlInputChannel.setChannelId("controlChannel");
        controlInputChannel.registerInputTransport(controlQueInputTransport);

        ChannelSet<InputChannel> controlInputChannelSet = new ChannelSet<>();
        controlInputChannelSet.registerChannel(controlInputChannel);

        AsyncInputAgent controlInputAgent = new AsyncInputAgent();
        controlInputAgent.setChannelSet(controlInputChannelSet);
        controlInputAgent.setMessageListener(new RoundRobinListener());

        //no output

        //control processor
        BaseProcessor controlProcessor = new BaseProcessor("ControlProc");
        controlProcessor.setExecutor(new IExecutor<Message<ControlHeader, Payload>>() {
            @Override
            public void processMessage(Message<ControlHeader, Payload> message, IOutputAgent outputAgent, IProcessingContext processingContext) {
                System.out.println("[" + Thread.currentThread().getName() + "]" + message.getPayload().getContent());
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

        //control node
        ProcessingNode ctrlNode = new ProcessingNode("node_C");
        ctrlNode.setInputAgentInternal(controlInputAgent);
        ctrlNode.setProcessorInternal(controlProcessor);
        ctrlNode.init();
        ctrlNode.start();

        Message msg = new Message(new ControlHeader());
        msg.setId(666);

        queueManager.getQueue("A2A").add(msg);
    }


}
