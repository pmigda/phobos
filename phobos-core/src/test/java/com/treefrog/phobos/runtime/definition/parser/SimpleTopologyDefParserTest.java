package com.treefrog.phobos.runtime.definition.parser;

import com.treefrog.phobos.exception.InvalidInputException;
import com.treefrog.phobos.runtime.definition.ProcNodeDef;
import com.treefrog.phobos.runtime.definition.TopologyDefGraph;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.fail;

public class SimpleTopologyDefParserTest {

    private SimpleTopologyDefParser parser = new SimpleTopologyDefParser();

    @Test
    public void parseValidPattern1Test() throws InvalidInputException {
        TopologyDefGraph defGraph = parser.parse(Arrays.asList(new String[]{"-[X]->(A)-[Y]->(B)"}));
        Assert.assertEquals(2,defGraph.getProcNodeDefs().size());
        Assert.assertEquals(2,defGraph.getEdgeDefs().size());

        Assert.assertTrue(defGraph.getProcNodeDefsMap().containsKey("A"));
        Assert.assertTrue(defGraph.getProcNodeDefsMap().containsKey("B"));

        Assert.assertTrue(defGraph.getEdgeDefsMap().containsKey("X"));
        Assert.assertTrue(defGraph.getEdgeDefsMap().containsKey("Y"));

        ProcNodeDef n1 = defGraph.getProcNodeDef("A");
        Assert.assertNotNull(n1);
        Assert.assertEquals(1,n1.getInputEdgesMap().size());
        Assert.assertTrue(n1.getInputEdgesMap().containsKey("X"));
        Assert.assertEquals(1,n1.getOutputEdgesMap().size());
        Assert.assertTrue(n1.getOutputEdgesMap().containsKey("Y"));

        ProcNodeDef n2 = defGraph.getProcNodeDef("B");
        Assert.assertNotNull(n2);
        Assert.assertEquals(1,n2.getInputEdgesMap().size());
        Assert.assertTrue(n2.getInputEdgesMap().containsKey("Y"));

    }

    @Test
    public void parseValidPattern2Test() throws InvalidInputException {
        TopologyDefGraph defGraph = parser.parse(Arrays.asList(new String[]{"(A)-[Y]->(B)"}));
        Assert.assertEquals(2,defGraph.getProcNodeDefs().size());
        Assert.assertEquals(1,defGraph.getEdgeDefs().size());

        Assert.assertTrue(defGraph.getProcNodeDefsMap().containsKey("A"));
        Assert.assertTrue(defGraph.getProcNodeDefsMap().containsKey("B"));

        Assert.assertTrue(defGraph.getEdgeDefsMap().containsKey("Y"));

        ProcNodeDef n1 = defGraph.getProcNodeDef("A");
        Assert.assertNotNull(n1);
        Assert.assertEquals(0,n1.getInputEdgesMap().size());
        Assert.assertEquals(1,n1.getOutputEdgesMap().size());
        Assert.assertTrue(n1.getOutputEdgesMap().containsKey("Y"));


        ProcNodeDef n2 = defGraph.getProcNodeDef("B");
        Assert.assertNotNull(n2);
        Assert.assertEquals(1,n2.getInputEdgesMap().size());
        Assert.assertTrue(n2.getInputEdgesMap().containsKey("Y"));
    }

    @Test
    public void parseValidPattern3Test() throws InvalidInputException {
        TopologyDefGraph defGraph = parser.parse(Arrays.asList(new String[]{"(A)-[Y]->(A)"}));
        Assert.assertEquals(1,defGraph.getProcNodeDefs().size());
        Assert.assertEquals(1,defGraph.getEdgeDefs().size());

        Assert.assertTrue(defGraph.getProcNodeDefsMap().containsKey("A"));
        Assert.assertTrue(defGraph.getEdgeDefsMap().containsKey("Y"));

        ProcNodeDef n1 = defGraph.getProcNodeDef("A");
        Assert.assertNotNull(n1);
        Assert.assertEquals(1,n1.getInputEdgesMap().size());
        Assert.assertEquals(1,n1.getOutputEdgesMap().size());
        Assert.assertTrue(n1.getInputEdgesMap().containsKey("Y"));
        Assert.assertTrue(n1.getOutputEdgesMap().containsKey("Y"));
    }

    @Test
    public void parseInvalidPatternTest() throws InvalidInputException {
        testInvalidPattern("(A)-[Y]->");
        testInvalidPattern("(A)-->(B)");
        testInvalidPattern("-(A)-[Y]->(B)");
        testInvalidPattern("()-[Y]->()");
        testInvalidPattern("(A)-[Y]->(B)-[X]");
        testInvalidPattern("(A)-[Y]->(B)-[X]->(C)");
    }

    private void testInvalidPattern(String pattern){
        try {
            TopologyDefGraph defGraph = parser.parse(Arrays.asList(new String[]{pattern}));
            fail("Not valid pattern has been matched");
        } catch(InvalidInputException ex){
        }
    }
}
