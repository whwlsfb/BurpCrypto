package burp.execjs;

import burp.BurpExtender;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JreBuiltInEngine implements IJsEngine {

    public ScriptEngine engine;
    public String methodName;
    public BurpExtender parent;

    public void setConfig(JsConfig config) throws Exception {
        this.loadJsCode(config.CryptoJsCode);
        this.methodName = config.MethodName;
    }

    @Override
    public void setParent(BurpExtender parent) {
        this.parent = parent;
    }

    private void initEngine() {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        engine = scriptEngineManager.getEngineByName("javascript");
    }

    public void loadJsCode(String jsCode) {
        try {
            initEngine();
            engine.eval(jsCode);
        } catch (Exception ex) {
            this.parent.stderr.write(ex.getMessage());
        }
    }

    public String eval(String param) throws ScriptException, NoSuchMethodException {
        Invocable invocable = (Invocable) engine;
        return invocable.invokeFunction(methodName, param).toString();
    }
}
