package burp.execjs.engine;

import burp.BurpExtender;

import burp.execjs.IJsEngine;
import burp.execjs.JsConfig;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

public class RhinoEngine implements IJsEngine {
    Context engine;
    public String methodName;
    public BurpExtender parent;
    String jsCode = "";

    public void setConfig(JsConfig config) throws Exception {
        jsCode = config.CryptoJsCode;
        this.methodName = config.MethodName;
    }

    @Override
    public void setParent(BurpExtender parent) {
        this.parent = parent;
    }

    private Scriptable initEngine() {
        engine = Context.enter();
        return engine.initStandardObjects();
    }

    public Scriptable loadJsCode(String jsCode) {
        try {
            Scriptable scope = initEngine();
            engine.evaluateString(scope, jsCode, RhinoEngine.class.getSimpleName(), 1, null);
            return scope;
        } catch (Exception ex) {
            this.parent.stderr.write(ex.getMessage());
            return null;
        }
    }

    public Object callFunction(Scriptable scope, String functionName, Object[] functionParams) throws Exception {
        Function function = (Function) scope.get(functionName, scope);
        if (function == null) {
            throw new Exception("function " + functionName + " not found.");
        }
        return function.call(engine, scope, scope, functionParams);
    }

    public String eval(String param) throws Exception {
        Scriptable scope = this.loadJsCode(jsCode);
        String result = callFunction(scope, methodName, new Object[]{param}).toString();
        Context.exit();
        return result;
    }

}
