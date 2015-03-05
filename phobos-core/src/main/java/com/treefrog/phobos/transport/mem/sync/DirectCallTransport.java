package com.treefrog.phobos.transport.mem.sync;

import com.treefrog.phobos.core.IProcessingNode;
import com.treefrog.phobos.core.api.ITransport;
import com.treefrog.phobos.core.msg.Message;
import com.treefrog.phobos.core.processor.IProcessor;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class DirectCallTransport implements ITransport {

    private IProcessor processor;

    @Override
    public void init(IProcessingNode nodeConfig) {
        //NOP
    }

    @Override
    public void start(String channelId) {
        //NOP
    }

    @Override
    public void stop() {
        //NOP
    }

    @Override
    public void sendMessage(Message msg) {
        processor.processMessage(msg);
    }

    @Override
    public Message readMessage() {
        throw new UnsupportedOperationException("Reading input is not applicable for sync transport");
    }

    public void setProcessor(IProcessor processor) {
        this.processor = processor;
    }
}
