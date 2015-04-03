package pl.treefrog.phobos.core.state.context;

import pl.treefrog.phobos.core.state.manager.IStateManager;
import pl.treefrog.phobos.exception.PhobosAssert;
import pl.treefrog.phobos.exception.PhobosException;

import java.util.HashMap;
import java.util.Map;

/**
 * author  : Piotr Migda (piotr.migda@treefrog.pl)
 * company : www.treefrog.pl
 * created : 2015-03-03
 * license : See the "LICENSE.txt" file for the full terms of the license governing this code.
 */
public abstract class AbstractContext implements IContext {

    protected String type;
    protected String oid;
    protected IStateManager parentStateManager;
    protected Map<String, Object> valueStore = new HashMap<>();
    protected Boolean disposed = false;

    public AbstractContext(String type, String oid, IStateManager parentStateManager) {
        this.type = type;
        this.oid = oid;
        this.parentStateManager = parentStateManager;
    }

    @Override
    public Object get(String key) {
        return valueStore.get(key);
    }

    @Override
    public void put(String key, Object value) {
        valueStore.put(key, value);
    }

    @Override
    public void remove(String key) {
        valueStore.remove(key);
    }

    public abstract void beforeMessageProcessing() throws PhobosException;

    public abstract void afterMessageProcessing() throws PhobosException;

    public void dispose() throws PhobosException {
        PhobosAssert.assertNotNull("There's not parent state manager provided within context", parentStateManager);
        parentStateManager.dispose(this);
        disposed = true;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Boolean getDisposed() {
        return disposed;
    }

}
