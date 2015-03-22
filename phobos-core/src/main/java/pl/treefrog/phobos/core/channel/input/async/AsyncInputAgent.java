package pl.treefrog.phobos.core.channel.input.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.core.channel.input.InputAgent;
import pl.treefrog.phobos.core.channel.input.async.listener.IMessageListener;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PlatformException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class AsyncInputAgent extends InputAgent {

    private static final Logger log = LoggerFactory.getLogger(AsyncInputAgent.class);

    protected IMessageListener messageListener;

    @Override
    public void init(IProcessingNode nodeConfig) throws PlatformException {
        super.init(nodeConfig);

        log.info("[" + parentProcNode.getNodeName() + "][" + this.hashCode() + "] Initializing async input agent");

        PhobosAssert.assertNotNull("Listener must not be null for operation", messageListener);
        messageListener.init(messageHandler, this);
    }

    @Override
    public void start() throws PlatformException {
        log.info("[" + parentProcNode.getNodeName() + "][" + this.hashCode() + "] starting async input agent");
        super.start();

        if (messageListener != null) {
            messageListener.start();
        }
    }

    @Override
    public void stop() throws PlatformException {
        log.info("[" + parentProcNode.getNodeName() + "][" + this.hashCode() + "] stopping async input agent");
        super.stop();

        if (messageListener != null) {
            messageListener.stop();
        }
    }

    //getters & setters
    public void setMessageListener(IMessageListener messageListener) {
        this.messageListener = messageListener;
    }
}
