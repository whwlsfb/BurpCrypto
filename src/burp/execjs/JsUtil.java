package burp.execjs;

import burp.BurpExtender;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.io.Reader;

public class JsUtil {
    Context engine;
    Scriptable scope;
    public String methodName;
    public BurpExtender parent;
    String jsCode = "";

    public void setConfig(JsConfig config) throws Exception {
        jsCode = config.CryptoJsCode;
        this.methodName = config.MethodName;
    }

    private void initEngine() {
        engine = Context.enter();
        scope = engine.initStandardObjects();
    }

    public void loadJsCode(String jsCode) {
        try {
            initEngine();
            engine.evaluateString(scope, jsCode, JsUtil.class.getSimpleName(), 1, null);
        } catch (Exception ex) {
            this.parent.stderr.write(ex.getMessage());
        }
    }

    public Object callFunction(String functionName, Object[] functionParams) throws Exception {
        Function function = (Function) scope.get(functionName, scope);
        if (function == null) {
            throw new Exception("function " + functionName + " not found.");
        }
        return function.call(engine, scope, scope, functionParams);
    }

    public String eval(String param) throws Exception {
        this.loadJsCode(jsCode);
        return callFunction(methodName, new Object[]{param}).toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
