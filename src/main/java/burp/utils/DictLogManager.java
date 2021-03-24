package burp.utils;

import burp.BurpExtender;

import java.io.UnsupportedEncodingException;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;

public class DictLogManager {
    private BurpExtender parent;

    public DictLogManager(final BurpExtender newParent) {
        this.parent = newParent;
    }

    public void Log(byte[] enc, byte[] origin) {
        this.parent.store.put(Utils.MD5(enc), origin);
    }

    public String Search(String key) {
        try {
            byte[] value = this.parent.store.get(Utils.MD5(key.getBytes("UTF-8")));
            if (value != null && value.length > 0) {
                return new String(value);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}