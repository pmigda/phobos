package pl.treefrog.phobos.core.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.IComponentLifecycle;
import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.core.api.ITransport;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PlatformException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class BaseChannel implements IChannel, IComponentLifecycle {

    private static final Logger log = LoggerFactory.getLogger(BaseChannel.class);

    protected String channelId;
    protected IProcessingNode parentProcNode;
    protected ITransport transport;

    @Override
    public void init(IProcessingNode nodeConfig) throws PlatformException {
        parentProcNode = nodeConfig;
        PhobosAssert.assertNotNull("Parent processing node must not be null", parentProcNode);

        log.info("[" + parentProcNode.getNodeName() + "][" + channelId + "] Initializing channel");

        PhobosAssert.assertNotNull("transport must not be null for operation", transport);
        transport.init(nodeConfig.getMessageHandler());
    }

    @Override
    public void start() throws PlatformException {
        log.info("[" + parentProcNode.getNodeName() + "][" + channelId + "] starting channel");
        transport.start(channelId);
    }

    @Override
    public void stop() throws PlatformException {
        log.info("[" + parentProcNode.getNodeName() + "][" + channelId + "] stopping channel");
        transport.stop();
    }

    //getters & setters
    @Override
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public ITransport getTransport() {
        return transport;
    }

    public void setTransport(ITransport transport) {
        this.transport = transport;
    }

}
