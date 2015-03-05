package pl.treefrog.phobos.runtime;

import pl.treefrog.phobos.core.channel.BaseChannel;
import pl.treefrog.phobos.core.channel.ChannelSet;
import pl.treefrog.phobos.core.channel.input.InputAgent;
import pl.treefrog.phobos.core.channel.input.async.AsyncInputAgent;
import pl.treefrog.phobos.core.channel.input.async.AsyncInputChannel;
import pl.treefrog.phobos.core.channel.input.async.IAsyncInputChannel;
import pl.treefrog.phobos.listener.RoundRobinListener;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class AsyncInputAgentGenStrategy implements InputAgentGenStrategy {

    @Override
    public InputAgent buildInputAgent() {
        AsyncInputAgent res = new AsyncInputAgent();
        ChannelSet<IAsyncInputChannel> inputChannelSet = new ChannelSet<>();
        res.setChannelSet(inputChannelSet);
        res.setListener(new RoundRobinListener());
        return res;
    }

    @Override
    public BaseChannel buildInputChannel() {
        return new AsyncInputChannel();
    }

}
