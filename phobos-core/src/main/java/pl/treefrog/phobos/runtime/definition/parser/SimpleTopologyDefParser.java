package pl.treefrog.phobos.runtime.definition.parser;

import org.apache.commons.lang.StringUtils;
import pl.treefrog.phobos.exception.PhobosInvalidInputException;
import pl.treefrog.phobos.runtime.definition.EdgeDef;
import pl.treefrog.phobos.runtime.definition.ProcNodeDef;
import pl.treefrog.phobos.runtime.definition.TopologyDefGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class SimpleTopologyDefParser implements ITopologyDefParser {

    private Pattern pattern = Pattern.compile("^(-\\[([\\pL\\pN]+)\\]->)?\\(([\\pL\\pN]+)\\)-\\[([\\pL\\pN]+)\\]->\\(([\\pL\\pN]+)\\)");

    public SimpleTopologyDefParser() {
    }

    public SimpleTopologyDefParser(String patternStr) {
        this.pattern = Pattern.compile(patternStr);
    }

    @Override
    public TopologyDefGraph parse(File input) throws IOException, PhobosInvalidInputException {
        BufferedReader inputReader = new BufferedReader(new FileReader(input));
        List<String> defInputList = new LinkedList<>();
        for (String inputLine; (inputLine = inputReader.readLine()) != null; ) {
            defInputList.add(inputLine);
        }
        inputReader.close();

        return parse(defInputList);
    }

    @Override
    public TopologyDefGraph parse(List<String> input) throws PhobosInvalidInputException {
        TopologyDefGraph defGraph = new TopologyDefGraph();

        for (String defLine : input) {
            Matcher matcher = pattern.matcher(defLine);

            if (matcher.matches()) {

                //System.out.println(matcher.group(1));
                //System.out.println(matcher.group(2));
                //System.out.println(matcher.group(3));
                //System.out.println(matcher.group(4));
                //System.out.println(matcher.group(5));

                buildDefGraph(matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5), defGraph);

            } else {
                throw new PhobosInvalidInputException("Input line doesn't match required pattern");
            }
        }

        return defGraph;
    }

    private void buildDefGraph(String C0, String N1, String C1, String N2, TopologyDefGraph defGraph) {

        ProcNodeDef n1 = getOrCreateNode(N1, defGraph);
        ProcNodeDef n2 = getOrCreateNode(N2, defGraph);

        EdgeDef c1 = getOrCreateEdge(C1, defGraph);
        addOutputEdge(c1, n1);
        addInputEdge(c1, n2);

        if (StringUtils.isNotEmpty(C0)) {
            EdgeDef c0 = getOrCreateEdge(C0, defGraph);
            addInputEdge(c0, n1);
        }

    }

    private ProcNodeDef getOrCreateNode(String nodeId, TopologyDefGraph defGraph) {
        ProcNodeDef node = defGraph.getProcNodeDef(nodeId);
        if (node == null) {
            node = new ProcNodeDef(nodeId);
            defGraph.addProcNodeDef(node);
        }
        return node;
    }

    private EdgeDef getOrCreateEdge(String edgeId, TopologyDefGraph defGraph) {
        EdgeDef edge = defGraph.getEdgeDef(edgeId);
        if (edge == null) {
            edge = new EdgeDef(edgeId);
            defGraph.addEdgeDef(edge);
        }
        return edge;
    }

    private void addInputEdge(EdgeDef edge, ProcNodeDef node) {
        if (!node.getInputEdgesMap().containsKey(edge.getEdgeId())) {
            node.getInputEdgesMap().put(edge.getEdgeId(), edge);
        }
    }

    private void addOutputEdge(EdgeDef edge, ProcNodeDef node) {
        if (!node.getOutputEdgesMap().containsKey(edge.getEdgeId())) {
            node.getOutputEdgesMap().put(edge.getEdgeId(), edge);
        }
    }
}
