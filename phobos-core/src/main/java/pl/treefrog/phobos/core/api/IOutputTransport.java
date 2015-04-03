package pl.treefrog.phobos.core.api;

import pl.treefrog.phobos.core.message.Message;
import pl.treefrog.phobos.exception.PhobosException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public interface IOutputTransport {

    void init() throws PhobosException;

    void start(String channelId) throws PhobosException;

    void stop() throws PhobosException;

    void sendMessage(Message msg) throws PhobosException;

}
