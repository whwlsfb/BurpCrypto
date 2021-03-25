package burp.execjs;

import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

public class CustomAjaxController extends AjaxController {

    private static final Log LOG = LogFactory.getLog(CustomAjaxController.class);

    private transient WeakReference<Thread> originatedThread_;

    /**
     * Creates an instance.
     */
    public CustomAjaxController() {
        init();
    }

    /**
     * Initializes this instance.
     */
    private void init() {
        originatedThread_ = new WeakReference<>(Thread.currentThread());
    }

    /**
     * Resynchronizes calls performed from the thread where this instance has been created.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public boolean processSynchron(final HtmlPage page, final WebRequest settings, final boolean async) {
        URL reqURL = settings.getUrl();
        settings.removeAdditionalHeader("Origin");
        return true;
    }

    /**
     * Indicates if the currently executing thread is the one in which this instance has been created.
     *
     * @return {@code true} if it's the same thread
     */
    boolean isInOriginalThread() {
        return Thread.currentThread() == originatedThread_.get();
    }

    /**
     * Custom deserialization logic.
     *
     * @param stream the stream from which to read the object
     * @throws IOException            if an IO error occurs
     * @throws ClassNotFoundException if a class cannot be found
     */
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        init();
    }
}
