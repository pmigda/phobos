package pl.treefrog.phobos.runtime;

import pl.treefrog.phobos.core.channel.BaseChannel;
import pl.treefrog.phobos.core.channel.ChannelSet;
import pl.treefrog.phobos.core.channel.IChannel;
import pl.treefrog.phobos.core.channel.input.InputAgent;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class BaseInputAgentGenStrategy implements InputAgentGenStrategy {


    @Override
    public InputAgent buildInputAgent() {
        InputAgent res = new InputAgent();
        ChannelSet<IChannel> inputChannelSet = new ChannelSet<>();
        res.setChannelSet(inputChannelSet);
        return res;
    }

    @Override
    public BaseChannel buildInputChannel() {
        return new BaseChannel();
    }
}
