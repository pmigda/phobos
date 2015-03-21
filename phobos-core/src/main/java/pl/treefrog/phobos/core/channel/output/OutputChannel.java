package pl.treefrog.phobos.core.channel.output;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.treefrog.phobos.core.channel.BaseChannel;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.exception.PlatformException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class OutputChannel extends BaseChannel implements IOutputChannel {

    private static final Logger log = LoggerFactory.getLogger(OutputChannel.class);

    @Override
    public void sendMessage(Message msg) throws PlatformException {
        transport.sendMessage(msg);
    }

}
