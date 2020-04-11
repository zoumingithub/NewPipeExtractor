package org.schabi.newpipe.extractor.services.cosmos;

import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.channel.ChannelExtractor;
import org.schabi.newpipe.extractor.comments.CommentsExtractor;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.kiosk.KioskExtractor;
import org.schabi.newpipe.extractor.kiosk.KioskList;
import org.schabi.newpipe.extractor.linkhandler.*;
import org.schabi.newpipe.extractor.playlist.PlaylistExtractor;
import org.schabi.newpipe.extractor.search.SearchExtractor;
import org.schabi.newpipe.extractor.services.cosmos.extractors.CosmosHomePageExtractor;
import org.schabi.newpipe.extractor.services.youtube.YoutubeService;
import org.schabi.newpipe.extractor.services.youtube.extractors.*;
import org.schabi.newpipe.extractor.services.youtube.linkHandler.*;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.subscription.SubscriptionExtractor;
import org.schabi.newpipe.extractor.suggestion.SuggestionExtractor;

import java.util.List;

import static java.util.Arrays.asList;
import static org.schabi.newpipe.extractor.StreamingService.ServiceInfo.MediaCapability.*;
import static org.schabi.newpipe.extractor.StreamingService.ServiceInfo.MediaCapability.COMMENTS;

public class CosmosService extends StreamingService {
    /**
     * Creates a new Streaming service.
     * If you Implement one do not set id within your implementation of this extractor, instead
     * set the id when you put the extractor into
     * <a href="https://teamnewpipe.github.io/NewPipeExtractor/javadoc/org/schabi/newpipe/extractor/ServiceList.html">ServiceList</a>.
     * All other parameters can be set directly from the overriding constructor.
     *
     * @param id           the number of the service to identify him within the NewPipe frontend
     * @param name         the name of the service
     * @param capabilities the type of media this service can handle
     */

    public CosmosService(int id, String name, List<ServiceInfo.MediaCapability> capabilities) {
        super(id, name, capabilities);
    }

    public CosmosService(int id) {
        super(id, "Cosmos", asList(AUDIO, VIDEO, LIVE, COMMENTS));
    }

    @Override
    public String getBaseUrl() {
        return "http://localhost:8080/";
    }

    /**
     * Must return a new instance of an implementation of LinkHandlerFactory for streams.
     *
     * @return an instance of a LinkHandlerFactory for streams
     */
    @Override
    public LinkHandlerFactory getStreamLHFactory() {
        return YoutubeStreamLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getChannelLHFactory() {
        return YoutubeChannelLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getPlaylistLHFactory() {
        return YoutubePlaylistLinkHandlerFactory.getInstance();
    }

    @Override
    public SearchQueryHandlerFactory getSearchQHFactory() {
        return YoutubeSearchQueryHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getCommentsLHFactory() {
        return null;
    }

    @Override
    public StreamExtractor getStreamExtractor(LinkHandler linkHandler) {
        return new YoutubeStreamExtractor(this, linkHandler);
    }

    @Override
    public ChannelExtractor getChannelExtractor(ListLinkHandler linkHandler) {
        return new YoutubeChannelExtractor(this, linkHandler);
    }

    @Override
    public PlaylistExtractor getPlaylistExtractor(ListLinkHandler linkHandler) {
        return new YoutubePlaylistExtractor(this, linkHandler);
    }

    @Override
    public SearchExtractor getSearchExtractor(SearchQueryHandler query) {
        return new YoutubeSearchExtractor(this, query);
    }

    /**
     * Must create a new instance of a SuggestionExtractor implementation.
     *
     * @return a new SuggestionExtractor instance
     */
    @Override
    public SuggestionExtractor getSuggestionExtractor() {
        return null;
    }

    /**
     * Outdated or obsolete. null can be returned.
     *
     * @return just null
     */
    @Override
    public SubscriptionExtractor getSubscriptionExtractor() {
        return null;
    }

    /**
     * Must create a new instance of a KioskList implementation.
     *
     * @return a new KioskList instance
     * @throws ExtractionException
     */
    @Override
    public KioskList getKioskList() throws ExtractionException {
        KioskList list = new KioskList(this);

        // add kiosks here e.g.:
        try {
            list.addKioskEntry(new KioskList.KioskExtractorFactory() {
                @Override
                public KioskExtractor createNewKiosk(StreamingService streamingService,
                                                     String url,
                                                     String id)
                        throws ExtractionException {
                    return new CosmosHomePageExtractor(CosmosService.this,
                            new YoutubeTrendingLinkHandlerFactory().fromUrl(url), id);
                }
            }, new YoutubeTrendingLinkHandlerFactory(), "Trending");
            list.setDefaultKiosk("Trending");
        } catch (Exception e) {
            throw new ExtractionException(e);
        }

        return list;
    }

    @Override
    public CommentsExtractor getCommentsExtractor(ListLinkHandler linkHandler) throws ExtractionException {
        return new YoutubeCommentsExtractor(this, linkHandler);
    }
}
