package pl.treefrog.phobos.core.channel.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.core.channel.AbstractChannelAgent;
import pl.treefrog.phobos.core.handler.IMessageHandler;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PlatformException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class InputAgent extends AbstractChannelAgent<InputChannel> implements IInputAgent {

    private static final Logger log = LoggerFactory.getLogger(InputAgent.class);

    protected IMessageHandler messageHandler;

    @Override
    public void init(IProcessingNode nodeConfig) throws PlatformException {
        super.init(nodeConfig);

        log.info("[" + parentProcNode.getNodeName() + "][" + this.hashCode() + "] Initializing input agent");

        messageHandler = nodeConfig.getMessageHandler();
        PhobosAssert.assertNotNull("Message handler must not be null for operation", messageHandler);
    }

}
