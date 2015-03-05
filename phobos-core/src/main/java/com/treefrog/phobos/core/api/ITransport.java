package com.treefrog.phobos.core.api;

import com.treefrog.phobos.core.IProcessingNode;
import com.treefrog.phobos.core.msg.Message;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */

/**
 * Platform plug point for messages xchange transport
 */
public interface ITransport {

    void init(IProcessingNode nodeConfig);

    void start(String channelId);

    void stop();

    void sendMessage(Message msg);

    Message readMessage();

}
