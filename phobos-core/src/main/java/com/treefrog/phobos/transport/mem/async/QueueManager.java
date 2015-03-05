package com.treefrog.phobos.transport.mem.async;

import com.treefrog.phobos.core.msg.Message;

import java.util.AbstractQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class QueueManager {

    private Map<String, AbstractQueue<Message>> queueList = new HashMap<>();

    public void createQueue(String topicId, int size) {
        queueList.put(topicId, new ArrayBlockingQueue<Message>(size));
    }

    public AbstractQueue<Message> getQueue(String topicId) {
        return queueList.get(topicId);
    }
}
