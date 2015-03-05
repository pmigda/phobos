package pl.treefrog.phobos.runtime;

import pl.treefrog.phobos.core.channel.BaseChannel;
import pl.treefrog.phobos.core.channel.ChannelSet;
import pl.treefrog.phobos.core.channel.output.IOutputChannel;
import pl.treefrog.phobos.core.channel.output.OutputAgent;
import pl.treefrog.phobos.core.channel.output.OutputChannel;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class BaseOutputAgentGenStrategy implements OutputAgentGenStrategy {

    @Override
    public OutputAgent buildOutputAgent() {
        OutputAgent outputAgent = new OutputAgent();
        ChannelSet<IOutputChannel> outputChannelSet = new ChannelSet<>();
        outputAgent.setChannelSet(outputChannelSet);
        return outputAgent;
    }

    @Override
    public BaseChannel buildOutputChannel() {
        return new OutputChannel();
    }


}
