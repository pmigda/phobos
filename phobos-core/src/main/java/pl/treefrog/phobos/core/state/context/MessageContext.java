package pl.treefrog.phobos.core.state.context;

import pl.treefrog.phobos.core.state.manager.IStateManager;
import pl.treefrog.phobos.exception.PhobosException;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public class MessageContext extends AbstractContext implements IMessageContext {

    public static final String MSG_CTX = "msg";

    private SecurityContext securityContext;
    private String batchId;

    public MessageContext(String objectId, IStateManager parentStateManager) {
        super(MSG_CTX, objectId, parentStateManager);
    }

    @Override
    public void beforeMessageProcessing() {
        //NOP
    }

    @Override
    public void afterMessageProcessing() throws PhobosException {
        dispose();
    }

    @Override
    public SecurityContext getSecurityContext() {
        return securityContext;
    }


    @Override
    public String getBatchId() {
        return batchId;
    }

    //IoC getters & setters
    public void setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
}
