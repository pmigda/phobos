package pl.treefrog.phobos.runtime.definition.parser;

import pl.treefrog.phobos.exception.InvalidInputException;
import pl.treefrog.phobos.runtime.definition.TopologyDefGraph;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public interface ITopologyDefParser {

    TopologyDefGraph parse(File input) throws IOException, InvalidInputException;

    TopologyDefGraph parse(List<String> input) throws InvalidInputException;

}
