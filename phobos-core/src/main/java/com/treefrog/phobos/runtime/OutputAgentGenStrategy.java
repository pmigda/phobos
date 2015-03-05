package com.treefrog.phobos.runtime;

import com.treefrog.phobos.core.channel.BaseChannel;
import com.treefrog.phobos.core.channel.output.OutputAgent;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */

/**
 * Strategy used to programmatically generate skeleton of output components for processing node.
 * Transports implementations are left to be delivered for later phase.
 */
public interface OutputAgentGenStrategy {

    OutputAgent buildOutputAgent();

    BaseChannel buildOutputChannel();
}
