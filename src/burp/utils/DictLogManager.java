package burp.utils;

import burp.BurpExtender;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;

public class DictLogManager {
    private BurpExtender parent;

    public DictLogManager(final BurpExtender newParent) {
        this.parent = newParent;
    }

    public void Log(byte[] enc, byte[] origin) {
        this.parent.store.put(Utils.MD5(enc), origin);
    }


}