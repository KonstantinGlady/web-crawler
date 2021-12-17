package crawler;

import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class WebCrawler implements LinkHandler {

    private Collection<String> visitedLinks = Collections.synchronizedSet(new TreeSet<>());
    private String url;
    private ForkJoinPool forkJoinPool;
    private Params params;

    public WebCrawler(String url) {
        this.url = url;
        this.params = new Params(url);
        forkJoinPool = ForkJoinPool.commonPool();
    }

    @Override
    public int size() {
        return visitedLinks.size();
    }

    @Override
    public boolean visited(String link) {
        return visitedLinks.contains(link);
    }

    @Override
    public void addVisited(String link) {
        visitedLinks.add(link);
    }

    public static void main(String[] args) {
        new WebCrawler("https://lenta.ru").start();

    }

    private void start() {
        forkJoinPool.invoke(new LinkAction(url, this, params));
        System.out.println("links: " + visitedLinks.size());
        visitedLinks.stream()
                .sorted(Comparator.naturalOrder())
                .forEach(System.out::println);

    }
}
