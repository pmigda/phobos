package com.treefrog.phobos.core;

import com.treefrog.phobos.exception.PlatformException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public interface ILifecycle {

    void init(IProcessingNode nodeConfig) throws PlatformException;

    void start();

    void stop();

}
