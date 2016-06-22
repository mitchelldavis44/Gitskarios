package com.alorma.github.explore.datasource;

import android.support.annotation.NonNull;
import com.alorma.github.explore.parser.Feed;
import com.alorma.github.explore.parser.FeedException;
import com.alorma.github.explore.parser.FeedParser;
import com.alorma.github.explore.parser.FeedParserFactory;
import com.alorma.github.explore.parser.Item;
import com.alorma.github.sdk.core.datasource.CloudDataSource;
import com.alorma.github.sdk.core.datasource.RestWrapper;
import com.alorma.github.sdk.core.datasource.SdkItem;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import rx.Observable;

public class ExploreDataSource extends CloudDataSource<String, List<Item>> {

  public static final String URL = "https://github.com/showcases.atom";

  public ExploreDataSource(RestWrapper restWrapper) {
    super(restWrapper);
  }

  @Override
  protected Observable<SdkItem<List<Item>>> execute(SdkItem<String> request, RestWrapper service) {
    return Observable.defer(this::getFeedObservable).map(Feed::getItemList).map(SdkItem::new);
  }

  private Observable<Feed> getFeedObservable() {
    OkHttpClient client = new OkHttpClient();
    Request request1 = new Request.Builder().url(URL).build();
    try {
      Response response = client.newCall(request1).execute();
      if (response.isSuccessful()) {
        return parseFeed(response);
      }
    } catch (IOException e) {
      return Observable.error(e);
    } catch (FeedException e) {
      return Observable.error(e);
    }
    return Observable.empty();
  }

  @NonNull
  private Observable<Feed> parseFeed(Response response) throws IOException, FeedException {
    InputStream inputStream = response.body().byteStream();
    FeedParser parser = FeedParserFactory.newParser();
    Feed feed = parser.parse(inputStream);
    return Observable.just(feed);
  }
}
