package burp.execjs;

import burp.BurpExtender;

public interface IJsEngine {
    void setConfig(JsConfig config) throws Exception;
    void setParent(BurpExtender parent);
    String eval(String param) throws Exception;
}