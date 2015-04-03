package pl.treefrog.phobos;

import pl.treefrog.phobos.core.ProcessingNode;
import pl.treefrog.phobos.core.api.IExecutor;
import pl.treefrog.phobos.core.channel.ChannelSet;
import pl.treefrog.phobos.core.channel.input.InputAgent;
import pl.treefrog.phobos.core.channel.input.InputChannel;
import pl.treefrog.phobos.core.channel.output.IOutputAgent;
import pl.treefrog.phobos.core.channel.output.OutputAgent;
import pl.treefrog.phobos.core.channel.output.OutputChannel;
import pl.treefrog.phobos.core.message.ControlHeader;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.core.processor.BaseProcessor;
import pl.treefrog.phobos.core.state.context.IProcessingContext;
import pl.treefrog.phobos.exception.PhobosException;
import pl.treefrog.phobos.transport.mem.sync.DirectCallTransport;

import java.util.Arrays;
import java.util.List;

public class SyncRunner {

    public static void main(String[] args) throws PhobosException {

        //create topology
        //Node1 Def

        //input
        DirectCallTransport tsync_input1 = new DirectCallTransport();
        InputChannel inputChannel = new InputChannel();
        inputChannel.setChannelId("InA");
        inputChannel.registerInputTransport(tsync_input1);

        ChannelSet<InputChannel> inputChannelSet = new ChannelSet<>();
        inputChannelSet.registerChannel(inputChannel);

        InputAgent inputAgent1 = new InputAgent();
        inputAgent1.setChannelSet(inputChannelSet);

        //output
        DirectCallTransport tsync_input2 = new DirectCallTransport();
        OutputChannel outputChannel = new OutputChannel();
        outputChannel.setChannelId("A2B");
        outputChannel.registerOutputTransport(tsync_input2);

        ChannelSet<OutputChannel> outputChannelSet = new ChannelSet<>();
        outputChannelSet.registerChannel(outputChannel);

        OutputAgent outputAgent = new OutputAgent();
        outputAgent.setChannelSet(outputChannelSet);

        //processor 1
        BaseProcessor proc1 = new BaseProcessor("PrintOutProc1");
        proc1.setExecutor(new IExecutor<Message>() {
            @Override
            public void processMessage(Message message, IOutputAgent outputAgent, IProcessingContext processingContext) throws PhobosException {
                System.out.println("Node A: " + message.getId());
                outputAgent.sendMessage("A2B", message, processingContext);
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return Arrays.asList(new String[]{"A2B"});
            }

            @Override
            public boolean acceptsMessage(Message message) {
                return true;
            }
        });

        //node_1
        ProcessingNode procNode = new ProcessingNode("node_A");
        procNode.setInputAgentInternal(inputAgent1);
        procNode.setOutputAgentInternal(outputAgent);
        procNode.setProcessorInternal(proc1);

        //Node2 Def
        //input

        InputChannel inputChannel2 = new InputChannel();
        inputChannel2.registerInputTransport(tsync_input2);

        ChannelSet<InputChannel> inputChannelSet2 = new ChannelSet<>();
        inputChannelSet2.registerChannel(inputChannel2);

        InputAgent inputAgent2 = new InputAgent();
        inputAgent2.setChannelSet(inputChannelSet2);

        //no output

        //processor 2
        BaseProcessor proc2 = new BaseProcessor("PrintOutProc2");
        proc2.setExecutor(new IExecutor<Message>() {
            @Override
            public void processMessage(Message message, IOutputAgent outputAgent, IProcessingContext processingContext) {
                System.out.println("Node B: " + message.getId());
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return null;
            }

            @Override
            public boolean acceptsMessage(Message message) {
                return true;
            }
        });

        //node_2
        ProcessingNode procNode2 = new ProcessingNode("node_B");
        procNode2.setInputAgentInternal(inputAgent2);
        procNode2.setProcessorInternal(proc2);

        procNode.init();
        procNode2.init();

        Message msg = new Message(new ControlHeader());
        msg.setId(666);

        tsync_input1.sendMessage(msg);
    }


}
