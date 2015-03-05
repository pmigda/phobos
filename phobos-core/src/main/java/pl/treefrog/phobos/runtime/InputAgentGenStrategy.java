package pl.treefrog.phobos.runtime;

import pl.treefrog.phobos.core.channel.BaseChannel;
import pl.treefrog.phobos.core.channel.input.InputAgent;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */

/**
 * Strategy used to programmatically generate skeleton of input components for processing node.
 * Transports implementations are left to be delivered for later phase.
 */
public interface InputAgentGenStrategy {

    InputAgent buildInputAgent();

    BaseChannel buildInputChannel();
}
