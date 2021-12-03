package burp.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSStaticFunction;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class HttpUtils extends ScriptableObject {
    public static OkHttpClient client = new OkHttpClient();

    @Override
    public String getClassName() {
        return "HttpUtils";
    }

    private static Request.Builder GetDefaultRequest(String url) {
        int fakeFirefoxVersion = Utils.GetRandomNumber(45, 94 + Calendar.getInstance().get(Calendar.YEAR) - 2021);
        Request.Builder requestBuilder = new Request.Builder()
                .url(url);
        requestBuilder.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:" + fakeFirefoxVersion + ".0) Gecko/20100101 Firefox/" + fakeFirefoxVersion + ".0");
        return requestBuilder;
    }

    @JSStaticFunction
    public static String Get(String url, Object headers) {
        Request.Builder requestBuilder = GetDefaultRequest(url);
        if (headers != null) {
            if (headers.getClass() == NativeObject.class) {
                NativeObject nHeaders = (NativeObject) headers;
                for (Object key : nHeaders.getAllIds()) {
                    requestBuilder = requestBuilder.addHeader(key.toString(), nHeaders.get(key).toString());
                }
            }
        }
        try {
            return client.newCall(requestBuilder.get().build()).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "request error.";
        }
    }
}
