package burp.execjs;

import burp.BurpExtender;

import javax.script.*;
import java.io.Reader;

public class JsUtil {
    public ScriptEngine engine;
    public String methodName;

    public void setConfig(JsConfig config) throws Exception {
        this.loadJsCode(config.CryptoJsCode);
        this.methodName = config.MethodName;
    }

    private void initEngine() {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        engine = scriptEngineManager.getEngineByName("javascript");
    }

    public void loadJsCode(String jsCode) throws ScriptException {
        initEngine();
        engine.eval(jsCode);
    }

    public String eval(String param) throws ScriptException, NoSuchMethodException {
        Invocable invocable = (Invocable) engine;
        return invocable.invokeFunction(methodName, param).toString();
    }

    @Override
    protected void finalize() throws Throwable {
        this.engine = null;
        super.finalize();
    }
}
