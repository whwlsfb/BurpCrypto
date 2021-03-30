package burp.execjs;

import burp.BurpExtender;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

public class HtmlUnitEngine implements IJsEngine {
    WebClient webClient;
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

    private HtmlPage initEngine() throws IOException {
        webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(true);
        webClient.setAjaxController(new CustomAjaxController());
        return webClient.getPage("about:blank");
    }

    public HtmlPage loadJsCode(String jsCode) {
        try {
            HtmlPage pageRef = initEngine();
            pageRef.executeJavaScript(jsCode);
            return pageRef;
        } catch (Exception ex) {
            this.parent.stderr.write(ex.getMessage());
            return null;
        }
    }

    public String eval(String param) throws Exception {
        HtmlPage pageRef = this.loadJsCode(jsCode);
        ScriptResult result = pageRef.executeJavaScript(this.methodName + "(\"" + jsParamReCode(param) + "\");");
        pageRef.cleanUp();
        return result.getJavaScriptResult().toString();
    }

    public String jsParamReCode(String param) {
        return param.replace("\\", "\\\\").replace("\"", "\\\"");
    }

}
