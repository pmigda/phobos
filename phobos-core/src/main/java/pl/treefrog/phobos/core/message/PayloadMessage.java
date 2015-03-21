package pl.treefrog.phobos.core.message;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class PayloadMessage<P> extends ControlMessage {

    private P payload;

    public PayloadMessage(Integer id) {
        super(id);
        type = MessageType.PAYLOAD.getIdx();
    }

    public P getPayload() {
        return payload;
    }

    public void setPayload(P payload) {
        this.payload = payload;
    }
}
