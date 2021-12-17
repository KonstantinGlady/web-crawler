package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class LinkAction extends RecursiveAction {

    private static final String LINK_ELEMENT_SELECTOR = "a[href]";
    private static final String ABSOLUTE_HREF_ATTRIBUTE = "abs:href";

    private String url;
    private LinkHandler cr;
    private Params params;

    public LinkAction(String url, LinkHandler cr, Params params) {
        this.url = url;
        this.cr = cr;
        this.params = params;
    }

    @Override
    protected void compute() {
        if (cr.size() <= params.getMaxScopeSize()) {

            try {
                List<RecursiveAction> actions = new ArrayList<>();
                CertificateHandler.trustEveryone();
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select(LINK_ELEMENT_SELECTOR);
                List<String> links = extractAttributeValues(elements, ABSOLUTE_HREF_ATTRIBUTE);

                for (String link : links) {
                    if (isInternalLink(link) && !cr.visited(link)) {
                        actions.add(new LinkAction(link, cr, params));
                        cr.addVisited(link);
                    }
                }

                invokeAll(actions);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isInternalLink(String url) {
        return hasSameHost(url, params.getInternalScope());
    }

    private boolean hasSameHost(String url, String internalScope) {
        String urlHost = Utils.getHost(url);
        return internalScope.equals(urlHost);
    }

    private List<String> extractAttributeValues(Elements elements, String attrName) {
        List<String> attrs = new ArrayList<>(elements.size());
        for (Element e : elements) {
            attrs.add(e.attr(attrName));
        }
        return attrs;
    }
}
