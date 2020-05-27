package burp.execjs;

import burp.BurpExtender;

import javax.script.*;
import java.io.Reader;

public class JSEngine {
    public ScriptEngine engine;
    public BurpExtender parent;

    public JSEngine(BurpExtender parent) {
        this.parent = parent;
    }

    private void initEngine() {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        engine = scriptEngineManager.getEngineByName("javascript");
    }

    public void loadJsCode(String jsCode) throws ScriptException {
        initEngine();
        engine.eval(jsCode);
    }

    public void loadJsCode(Reader reader) throws ScriptException {
        initEngine();
        engine.eval(reader);
    }

    public String eval(String methodName, Object[] params) throws ScriptException, NoSuchMethodException {
        Invocable invocable = (Invocable) engine;
        return invocable.invokeFunction(methodName, params).toString();
    }

    @Override
    protected void finalize() throws Throwable {
        this.engine = null;
        super.finalize();
    }
}
