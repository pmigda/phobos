package com.treefrog.phobos.core.api;

import com.treefrog.phobos.core.channel.output.IOutputAgent;
import com.treefrog.phobos.core.msg.Message;

import java.util.List;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */

/**
 * Platform plug point for message processing logic
 */
public interface IExecutor {

    void processMessage(Message message, IOutputAgent outputAgent);

    List<String> getRequiredChannelsIds();

}
