package pl.treefrog.phobos.core.channel.input.async;

import pl.treefrog.phobos.core.IProcessingNode;
import pl.treefrog.phobos.core.channel.input.InputAgent;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PlatformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class AsyncInputAgent extends InputAgent<IAsyncInputChannel> {

    private static final Logger log = LoggerFactory.getLogger(AsyncInputAgent.class);

    protected IListener listener;

    @Override
    public void init(IProcessingNode nodeConfig) throws PlatformException {
        super.init(nodeConfig);

        log.info("["+parentProcNode.getNodeName()+"]["+this.hashCode()+"] Initializing async input agent");

        PhobosAssert.assertNotNull("Listener must not be null for operation", listener);
        listener.init(processor, channelSet);
    }

    @Override
    public void start() {
        log.info("["+parentProcNode.getNodeName()+"]["+this.hashCode()+"] starting async input agent");
        super.start();

        if (listener != null) {
            listener.start();
        }
    }

    @Override
    public void stop() {
        log.info("["+parentProcNode.getNodeName()+"]["+this.hashCode()+"] stopping async input agent");
        super.stop();

        if (listener != null) {
            listener.stop();
        }
    }

    //getters & setters
    public void setListener(IListener listener) {
        this.listener = listener;
    }
}
