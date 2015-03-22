package pl.treefrog.phobos.transport.mem.async;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public abstract class AbstractQueueTransport {

    protected String queueName;
    protected QueueManager queManager;

    public void setQueManager(QueueManager queManager) {
        this.queManager = queManager;
    }
}
