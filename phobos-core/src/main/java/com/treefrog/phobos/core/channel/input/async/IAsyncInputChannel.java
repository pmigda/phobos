package com.treefrog.phobos.core.channel.input.async;

import com.treefrog.phobos.core.channel.IChannel;
import com.treefrog.phobos.core.msg.Message;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public interface IAsyncInputChannel extends IChannel {

    Message readMessage();

}
