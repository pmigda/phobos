package pl.treefrog.phobos.core.message;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public enum MessageType {

    DEFAULT(0),
    CONTROL(1),
    PAYLOAD(2);

    Integer idx;

    MessageType(Integer idx) {
        this.idx = idx;
    }

    public int getIdx() {
        return idx;
    }
}
