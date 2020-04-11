package org.schabi.newpipe.extractor.services.cosmos.extractors;

import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.kiosk.KioskExtractor;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandler;
import org.schabi.newpipe.extractor.services.youtube.linkHandler.YoutubeStreamLinkHandlerFactory;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.extractor.stream.StreamType;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.*;

public class CosmosHomePageExtractor  extends KioskExtractor<StreamInfoItem> {
    public CosmosHomePageExtractor(StreamingService streamingService, ListLinkHandler linkHandler, String kioskId) {
        super(streamingService, linkHandler, kioskId);
    }

    /**
     * Fetch the current page.
     *
     * @param downloader the download to use
     * @throws IOException         if the page can not be loaded
     * @throws ExtractionException if the pages content is not understood
     */
    @Override
    public void onFetchPage(@Nonnull Downloader downloader) throws IOException, ExtractionException {

    }

    /**
     * Id should be the name of the kiosk, tho Id is used for identifing it in the frontend,
     * so id should be kept in english.
     * In order to get the name of the kiosk in the desired language we have to
     * crawl if from the website.
     *
     * @return the tranlsated version of id
     * @throws ParsingException
     */
    @Nonnull
    @Override
    public String getName() throws ParsingException {
        return "Home";
    }

    /**
     * A {@link InfoItemsPage InfoItemsPage} corresponding to the initial page where the items are from the initial request and
     * the nextPageUrl relative to it.
     *
     * @return a {@link InfoItemsPage} corresponding to the initial page
     */
    @Nonnull
    @Override
    public InfoItemsPage<StreamInfoItem> getInitialPage() throws IOException, ExtractionException {
        /*
         public InfoItemsPage(List<T> itemsList, String nextPageUrl, List<Throwable> errors) {
            this.itemsList = itemsList;
            this.nextPageUrl = nextPageUrl;
            this.errors = errors;
        }
         */
        List<StreamInfoItem> itemsList = new ArrayList<StreamInfoItem>();
        List<Throwable> errors = new ArrayList<Throwable>();

        String[] videoIds = {"yYE_e5PuqnY","YzHqFyKwr5A","0YPiSxGuPUk"};

        for(String videoId:videoIds){
            String url = YoutubeStreamLinkHandlerFactory.getInstance().getUrl(videoId);
            StreamInfoItem item = new StreamInfoItem(getServiceId(), url,"Title", StreamType.VIDEO_STREAM);
            itemsList.add(item);

        }

        return new InfoItemsPage<StreamInfoItem>(itemsList,"",errors);
    }

    /**
     * Returns an url that can be used to get the next page relative to the initial one.
     * <p>Usually, these links will only work in the implementation itself.</p>
     *
     * @return an url pointing to the next page relative to the initial page
     * @see #getPage(String)
     */
    @Override
    public String getNextPageUrl() throws IOException, ExtractionException {
        return "";
    }

    /**
     * Get a list of items corresponding to the specific requested page.
     *
     * @param pageUrl any page url got from the exclusive implementation of the list extractor
     * @return a {@link InfoItemsPage} corresponding to the requested page
     * @see #getNextPageUrl()
     * @see InfoItemsPage#getNextPageUrl()
     */
    @Override
    public InfoItemsPage<StreamInfoItem> getPage(String pageUrl) throws IOException, ExtractionException {
        return getInitialPage();
    }
}
