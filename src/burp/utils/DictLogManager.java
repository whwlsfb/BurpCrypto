package burp.utils;

import burp.BurpExtender;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;

public class DictLogManager {
    private BurpExtender parent;

    public DictLogManager(final BurpExtender newParent) {
        this.parent = newParent;
    }

    public void Log(EncryptionType type, byte[] enc, byte[] origin) {
        String typeStr = "";
        switch (type) {
            case AES:
                typeStr = "aes";
                break;
            case RSA:
                typeStr = "rsa";
                break;
            case EXECJS:
                typeStr = "execjs";
                break;
        }
        this.parent.store.put(Utils.byteMerger(bytes(typeStr + "_"), Utils.MD5(enc)), origin);
    }


}