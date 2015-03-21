package pl.treefrog.phobos.core.message;

import java.util.HashMap;
import java.util.Map;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class ControlMessage extends Message {

    private Map<String, Object> controlHeader = new HashMap<>();

    public ControlMessage(Integer id) {
        super(id);
        type = MessageType.CONTROL.getIdx();
    }

    public Map<String, Object> getControlHeader() {
        return controlHeader;
    }

    public void setControlHeader(Map<String, Object> controlHeader) {
        this.controlHeader = controlHeader;
    }
}
