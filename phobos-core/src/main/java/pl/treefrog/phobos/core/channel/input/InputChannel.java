package pl.treefrog.phobos.core.channel.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.core.api.IInputTransport;
import pl.treefrog.phobos.core.channel.AbstractChannel;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PlatformException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class InputChannel extends AbstractChannel implements IInputChannel {

    private static final Logger log = LoggerFactory.getLogger(InputChannel.class);

    protected IInputTransport inputTransport;

    @Override
    public void init(IProcessingNode nodeConfig) throws PlatformException {
        super.init(nodeConfig);

        log.info("[" + parentProcNode.getNodeName() + "][" + channelId + "] Initializing input channel");

        PhobosAssert.assertNotNull("Input transport must not be null for operation", inputTransport);
        inputTransport.init(nodeConfig.getMessageHandler());
    }

    @Override
    public void start() throws PlatformException {
        log.info("[" + parentProcNode.getNodeName() + "][" + channelId + "] starting input channel");
        inputTransport.start(channelId);
    }

    @Override
    public void stop() throws PlatformException {
        log.info("[" + parentProcNode.getNodeName() + "][" + channelId + "] stopping input channel");
        inputTransport.stop();
    }

    @Override
    public Message readMessage() throws PlatformException {
        return inputTransport.readMessage();
    }

    //IoC getters & setters
    public void registerInputTransport(IInputTransport inputTransport) {
        this.inputTransport = inputTransport;
    }
}
