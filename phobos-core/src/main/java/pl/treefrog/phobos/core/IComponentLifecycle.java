package pl.treefrog.phobos.core;

import pl.treefrog.phobos.exception.PlatformException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public interface IComponentLifecycle {

    void init(IProcessingNode nodeConfig) throws PlatformException;

    void start() throws PlatformException;

    void stop() throws PlatformException;

}
