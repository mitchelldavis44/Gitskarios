package com.alorma.github.explore.datasource;

import com.alorma.github.explore.parser.Item;
import com.alorma.github.presenter.CacheWrapper;
import com.alorma.github.sdk.core.datasource.CacheDataSource;
import com.alorma.github.sdk.core.datasource.SdkItem;
import java.util.List;
import rx.Observable;

public class ExploreCacheDataSource implements CacheDataSource<String, List<Item>> {

  private static final String CATEGORIES = "CATEGORIES";

  @Override
  public void saveData(SdkItem<String> request, SdkItem<List<Item>> data) {
    CacheWrapper.exploreCache().set(CATEGORIES, data.getK());
  }

  @Override
  public Observable<SdkItem<List<Item>>> getData(SdkItem<String> request) {
    return Observable.just(getCategories()).map(SdkItem::new);
  }

  private List<Item> getCategories() {
    return CacheWrapper.exploreCache().get(CATEGORIES);
  }
}
