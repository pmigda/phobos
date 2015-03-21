package pl.treefrog.phobos;

import pl.treefrog.phobos.core.ProcessingNode;
import pl.treefrog.phobos.core.api.IExecutor;
import pl.treefrog.phobos.core.channel.ChannelSet;
import pl.treefrog.phobos.core.channel.input.async.AsyncInputAgent;
import pl.treefrog.phobos.core.channel.input.async.AsyncInputChannel;
import pl.treefrog.phobos.core.channel.input.async.IAsyncInputChannel;
import pl.treefrog.phobos.core.channel.input.async.listener.RoundRobinListener;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.channel.output.IOutputChannel;
import pl.treefrog.phobos.core.channel.output.OutputAgent;
import pl.treefrog.phobos.core.channel.output.OutputChannel;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.message.PayloadMessage;
import pl.treefrog.phobos.core.processor.BaseProcessor;
import pl.treefrog.phobos.core.state.context.ProcessingContext;
import pl.treefrog.phobos.exception.PlatformException;
import pl.treefrog.phobos.transport.mem.async.QueueManager;
import pl.treefrog.phobos.transport.mem.async.QueueTransport;

import java.util.Arrays;
import java.util.List;

public class ProcWithControlRunner {

    public static void main(String[] args) throws PlatformException {

        //in memory queue based mom
        QueueManager queueManager = new QueueManager();
        queueManager.createQueue("A2A", 100);
        queueManager.createQueue("controlChannel", 100);

        QueueTransport queTransport = new QueueTransport();
        queTransport.setQueManager(queueManager);

        QueueTransport controlQueTransport = new QueueTransport();
        controlQueTransport.setQueManager(queueManager);

        //create base processing topology
        //input
        AsyncInputChannel inputChannel = new AsyncInputChannel();
        inputChannel.setChannelId("A2A");
        inputChannel.setTransport(queTransport);

        ChannelSet<IAsyncInputChannel> inputChannelSet = new ChannelSet<>();
        inputChannelSet.registerChannel(inputChannel);

        AsyncInputAgent inputAgent = new AsyncInputAgent();
        inputAgent.setChannelSet(inputChannelSet);
        inputAgent.setMessageListener(new RoundRobinListener());

        //output
        OutputChannel outputChannel = new OutputChannel();
        outputChannel.setChannelId("A2A");
        outputChannel.setTransport(queTransport);

        OutputChannel controlChannel = new OutputChannel();
        controlChannel.setChannelId("controlChannel");
        controlChannel.setTransport(controlQueTransport);

        ChannelSet<IOutputChannel> outputChannelSet = new ChannelSet<>();
        outputChannelSet.registerChannel(outputChannel);
        outputChannelSet.registerChannel(controlChannel);

        OutputAgent outputAgent = new OutputAgent();
        outputAgent.setChannelSet(outputChannelSet);

        //processor
        BaseProcessor controlProcWrapper = new BaseProcessor("ChainedControlProcAgent");
        controlProcWrapper.setExecutor(new IExecutor() {
            @Override
            public void processMessage(Message message, IOutputAgent outputAgent, ProcessingContext context) throws PlatformException {

                PayloadMessage<String> ctrlMsg = new PayloadMessage<>(-999);
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

        //base processing node
        ProcessingNode procNode = new ProcessingNode("node_A");
        procNode.setInputAgentInternal(inputAgent);
        procNode.setOutputAgentInternal(outputAgent);
        procNode.setProcessorInternal(controlProcWrapper);
        procNode.init();
        procNode.start();

        //controller node
        AsyncInputChannel controlInputChannel = new AsyncInputChannel();
        controlInputChannel.setChannelId("controlChannel");
        controlInputChannel.setTransport(controlQueTransport);

        ChannelSet<IAsyncInputChannel> controlInputChannelSet = new ChannelSet<>();
        controlInputChannelSet.registerChannel(controlInputChannel);

        AsyncInputAgent controlInputAgent = new AsyncInputAgent();
        controlInputAgent.setChannelSet(controlInputChannelSet);
        controlInputAgent.setMessageListener(new RoundRobinListener());

        //no output

        //control processor
        BaseProcessor controlProcessor = new BaseProcessor("ControlProc");
        controlProcessor.setExecutor(new IExecutor<PayloadMessage>() {
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

        //control node
        ProcessingNode ctrlNode = new ProcessingNode("node_C");
        ctrlNode.setInputAgentInternal(controlInputAgent);
        ctrlNode.setProcessorInternal(controlProcessor);
        ctrlNode.init();
        ctrlNode.start();

        Message msg = new Message(666);

        queueManager.getQueue("A2A").add(msg);
    }


}
