package com.treefrog.phobos;

import com.treefrog.phobos.core.ProcessingNode;
import com.treefrog.phobos.core.api.IExecutor;
import com.treefrog.phobos.core.channel.BaseChannel;
import com.treefrog.phobos.core.channel.ChannelSet;
import com.treefrog.phobos.core.channel.IChannel;
import com.treefrog.phobos.core.channel.input.InputAgent;
import com.treefrog.phobos.core.channel.output.IOutputAgent;
import com.treefrog.phobos.core.channel.output.IOutputChannel;
import com.treefrog.phobos.core.channel.output.OutputAgent;
import com.treefrog.phobos.core.channel.output.OutputChannel;
import com.treefrog.phobos.core.msg.Message;
import com.treefrog.phobos.core.processor.BaseProcessor;
import com.treefrog.phobos.exception.PlatformException;
import com.treefrog.phobos.transport.mem.sync.DirectCallTransport;

import java.util.Arrays;
import java.util.List;

public class SyncRunner {

    public static void main(String[] args) throws PlatformException {

        //create topology
        //Node1 Def

        //input
        DirectCallTransport tsync_input1 = new DirectCallTransport();
        BaseChannel inputChannel = new BaseChannel();
        inputChannel.setChannelId("InA");
        inputChannel.setTransport(tsync_input1);

        ChannelSet<IChannel> inputChannelSet = new ChannelSet<>();
        inputChannelSet.registerChannel(inputChannel);

        InputAgent inputAgent1 = new InputAgent<>();
        inputAgent1.setChannelSet(inputChannelSet);

        //output
        DirectCallTransport tsync_input2 = new DirectCallTransport();
        OutputChannel outputChannel = new OutputChannel();
        outputChannel.setChannelId("A2B");
        outputChannel.setTransport(tsync_input2);

        ChannelSet<IOutputChannel> outputChannelSet = new ChannelSet<>();
        outputChannelSet.registerChannel(outputChannel);

        OutputAgent outputAgent = new OutputAgent();
        outputAgent.setChannelSet(outputChannelSet);

        //processor 1
        BaseProcessor proc1 = new BaseProcessor("PrintOutProc1");
        proc1.setExecutor(new IExecutor() {
            @Override
            public void processMessage(Message message, IOutputAgent outputAgent) {
                System.out.println("Node A: "+message.id);
                outputAgent.sendMessage("A2B", message);
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return Arrays.asList(new String[]{"A2B"});
            }
        });

        //node_1
        ProcessingNode procNode = new ProcessingNode("node_A");
        procNode.setInputAgentInternal(inputAgent1);
        procNode.setOutputAgentInternal(outputAgent);
        procNode.setProcessorInternal(proc1);
        tsync_input1.setProcessor(proc1); //direct call

        //Node2 Def
        //input

        BaseChannel inputChannel2 = new BaseChannel();
        inputChannel2.setTransport(tsync_input2);

        ChannelSet<BaseChannel> inputChannelSet2 = new ChannelSet<>();
        inputChannelSet2.registerChannel(inputChannel2);

        InputAgent<BaseChannel> inputAgent2 = new InputAgent();
        inputAgent2.setChannelSet(inputChannelSet2);

        //no output

        //processor 2
        BaseProcessor proc2 = new BaseProcessor("PrintOutProc2");
        proc2.setExecutor(new IExecutor() {
            @Override
            public void processMessage(Message message, IOutputAgent outputAgent) {
                System.out.println("Node B: "+message.id);
            }

            @Override
            public List<String> getRequiredChannelsIds() {
                return null;
            }
        });

        //node_2
        ProcessingNode procNode2 = new ProcessingNode("node_B");
        procNode2.setInputAgentInternal(inputAgent2);
        procNode2.setProcessorInternal(proc2);
        tsync_input2.setProcessor(proc2);  //direct call

        procNode.init();
        procNode2.init();

        Message msg = new Message();
        msg.id = 666;

        tsync_input1.sendMessage(msg);
    }



}
