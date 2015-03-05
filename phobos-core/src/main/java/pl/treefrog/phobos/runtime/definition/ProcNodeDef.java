package pl.treefrog.phobos.runtime.definition;

import java.util.HashMap;
import java.util.Map;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class ProcNodeDef {

    private String nodeId;
    private Map<String, EdgeDef> inputEdgesMap = new HashMap<>();
    private Map<String, EdgeDef> outputEdgesMap = new HashMap<>();

    public ProcNodeDef(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public Map<String, EdgeDef> getInputEdgesMap() {
        return inputEdgesMap;
    }

    public void setInputEdgesMap(Map<String, EdgeDef> inputEdgesMap) {
        this.inputEdgesMap = inputEdgesMap;
    }

    public Map<String, EdgeDef> getOutputEdgesMap() {
        return outputEdgesMap;
    }

    public void setOutputEdgesMap(Map<String, EdgeDef> outputEdgesMap) {
        this.outputEdgesMap = outputEdgesMap;
    }
}
