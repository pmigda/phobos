package pl.treefrog.phobos.core.channel.output;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.core.api.IOutputTransport;
import pl.treefrog.phobos.core.channel.AbstractChannel;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PhobosException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class OutputChannel extends AbstractChannel implements IOutputChannel {

    private static final Logger log = LoggerFactory.getLogger(OutputChannel.class);

    protected IOutputTransport outputTransport;

    @Override
    public void init(IProcessingNode nodeConfig) throws PhobosException {
        super.init(nodeConfig);

        log.info("[" + parentProcNode.getNodeName() + "][" + channelId + "] Initializing output channel");

        PhobosAssert.assertNotNull("Output transport must not be null for operation", outputTransport);
        outputTransport.init();
    }

    @Override
    public void start() throws PhobosException {
        log.info("[" + parentProcNode.getNodeName() + "][" + channelId + "] starting output channel");
        outputTransport.start(channelId);
    }

    @Override
    public void stop() throws PhobosException {
        log.info("[" + parentProcNode.getNodeName() + "][" + channelId + "] stopping output channel");
        outputTransport.stop();
    }

    @Override
    public void sendMessage(Message msg) throws PhobosException {
        outputTransport.sendMessage(msg);
    }


    //IoC getters & setters
    public void registerOutputTransport(IOutputTransport outputTransport) {
        this.outputTransport = outputTransport;
    }

}
