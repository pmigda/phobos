package pl.treefrog.phobos.runtime.definition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class TopologyDefGraph {

    private Map<String, ProcNodeDef> procNodeDefs = new HashMap<>();
    private Map<String, EdgeDef> edgeDefs = new HashMap<>();

    public void addProcNodeDef(ProcNodeDef node) {
        procNodeDefs.put(node.getNodeId(), node);
    }

    public ProcNodeDef getProcNodeDef(String nodeId) {
        return procNodeDefs.get(nodeId);
    }

    public void addEdgeDef(EdgeDef edge) {
        edgeDefs.put(edge.getEdgeId(), edge);
    }

    public EdgeDef getEdgeDef(String edgeId) {
        return edgeDefs.get(edgeId);
    }

    public Map<String, ProcNodeDef> getProcNodeDefsMap() {
        return procNodeDefs;
    }

    public Map<String, EdgeDef> getEdgeDefsMap() {
        return edgeDefs;
    }

    public Collection<ProcNodeDef> getProcNodeDefs() {
        return procNodeDefs.values();
    }

    public Collection<EdgeDef> getEdgeDefs() {
        return edgeDefs.values();
    }

}
