package pl.treefrog.phobos.core.channel.input;

import pl.treefrog.phobos.core.channel.IChannel;
import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.exception.PlatformException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public interface IInputChannel extends IChannel {

    Message readMessage() throws PlatformException;

}
