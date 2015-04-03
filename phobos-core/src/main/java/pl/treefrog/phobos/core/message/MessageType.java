package pl.treefrog.phobos.core.message;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class MessageType {

    public static final MessageType DEFAULT_MESSAGE = new MessageType(0, "DEFAULT", 0, "DEF-GRP");

    private Integer typeIdx = 0;
    private String type = "DEFAULT";

    private Integer groupIdx = 0;
    private String group = "DEF-GRP";

    public MessageType() {
    }

    public MessageType(Integer idx, String type) {
        this.typeIdx = idx;
        this.type = type;
    }

    public MessageType(Integer typeIdx, String type, Integer groupIdx, String group) {
        this.typeIdx = typeIdx;
        this.type = type;
        this.groupIdx = groupIdx;
        this.group = group;
    }

    public int getTypeIdx() {
        return typeIdx;
    }

    public String getType() {
        return type;
    }

    public Integer getGroupIdx() {
        return groupIdx;
    }

    public String getGroup() {
        return group;
    }

    public String toString() {
        return group + "::" + type;
    }

    @Override
    public boolean equals(Object object) {
        MessageType mType = (MessageType) object;
        if (this.typeIdx.equals(mType.typeIdx) &&
                this.type.equals(mType.type) &&
                this.groupIdx.equals(mType.groupIdx) &&
                this.group.equals(mType.group)) {
            return true;
        } else {
            return false;
        }
    }
}
