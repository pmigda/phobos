package pl.treefrog.phobos.core.message;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class Message<C extends ControlHeader, P extends Payload> {

    protected Integer id;
    protected MessageType type = new MessageType();

    protected C controlHeader;
    protected P payload;

    public Message() {
    }

    ;

    public Message(C controlHeader) {
        this.controlHeader = controlHeader;
    }

    ;

    public Message(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public C getControlHeader() {
        return controlHeader;
    }

    public void setControlHeader(C controlHeader) {
        this.controlHeader = controlHeader;
    }

    public P getPayload() {
        return payload;
    }

    public void setPayload(P payload) {
        this.payload = payload;
    }

    public boolean isOfType(MessageType mType) {
        return this.type.equals(mType);
    }

    public boolean isInGroup(Integer messageGroupIdx) {
        return this.type.getGroupIdx() == messageGroupIdx;
    }

    public boolean isInGroup(String messageGroupName) {
        return this.type.getGroup().equals(messageGroupName);
    }
}
