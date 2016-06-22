package com.alorma.github.explore.datasource;

import com.alorma.github.explore.parser.Item;
import com.alorma.github.sdk.core.datasource.SdkItem;
import com.alorma.github.sdk.core.repository.GenericRepository;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ExplorePresenter {

  private GenericRepository<String, List<Item>> repository;

  public ExplorePresenter(GenericRepository<String, List<Item>> repository) {

    this.repository = repository;
  }

  public void load() {
    repository.execute(null)
        .map(SdkItem::getK)
        .flatMap(Observable::from)
        .map(Item::getTitle)
        .toList()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onItems, this::onError);
  }

  private void onItems(List<String> titles) {

  }

  private void onError(Throwable throwable) {

  }
}
