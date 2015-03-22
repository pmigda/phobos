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
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.processor.BaseProcessor;
import pl.treefrog.phobos.core.state.context.ProcessingContext;
import pl.treefrog.phobos.exception.PlatformException;
import pl.treefrog.phobos.transport.mem.async.QueueInputTransport;
import pl.treefrog.phobos.transport.mem.async.QueueManager;
import pl.treefrog.phobos.transport.mem.async.QueueOutputTransport;

import java.util.Arrays;
import java.util.List;

public class AsyncRunner {

    public static void main(String[] args) throws PlatformException {

        //in memory queue based mom
        QueueManager queueManager = new QueueManager();
        queueManager.createQueue("A2A", 100);

        QueueInputTransport queInputTransport = new QueueInputTransport();
        queInputTransport.setQueManager(queueManager);

        QueueOutputTransport queOutputTransport = new QueueOutputTransport();
        queOutputTransport.setQueManager(queueManager);

        //create topology
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

        ChannelSet<OutputChannel> outputChannelSet = new ChannelSet<>();
        outputChannelSet.registerChannel(outputChannel);

        OutputAgent outputAgent = new OutputAgent();
        outputAgent.setChannelSet(outputChannelSet);

        //processor
        BaseProcessor proc = new BaseProcessor("PrintOutProc");
        proc.setExecutor(new IExecutor() {
            @Override
            public void processMessage(Message message, IOutputAgent outputAgent, ProcessingContext context) throws PlatformException {
                System.out.println(message.getId());
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

        //node
        ProcessingNode procNode = new ProcessingNode("node_A");
        procNode.setInputAgentInternal(inputAgent);
        procNode.setOutputAgentInternal(outputAgent);
        procNode.setProcessorInternal(proc);

        procNode.init();
        procNode.start();

        Message msg = new Message(666);

        queueManager.getQueue("A2A").add(msg);
    }


}
