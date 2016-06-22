package com.alorma.github.injector.module;

import com.alorma.github.explore.datasource.ExploreCacheDataSource;
import com.alorma.github.explore.datasource.ExploreDataSource;
import com.alorma.github.explore.datasource.ExplorePresenter;
import com.alorma.github.explore.parser.Item;
import com.alorma.github.injector.PerActivity;
import com.alorma.github.sdk.core.datasource.CacheDataSource;
import com.alorma.github.sdk.core.datasource.CloudDataSource;
import com.alorma.github.sdk.core.repository.GenericRepository;
import dagger.Module;
import dagger.Provides;
import java.util.List;

@Module public class ExploreModule {

  @Provides
  @PerActivity
  CacheDataSource<String, List<Item>> providesCache() {
    return new ExploreCacheDataSource();
  }

  @Provides
  @PerActivity
  CloudDataSource<String, List<Item>> providesDataSource() {
    return new ExploreDataSource(null);
  }

  @Provides
  @PerActivity
  GenericRepository<String, List<Item>> providesRepository(CacheDataSource<String, List<Item>> cache,
      CloudDataSource<String, List<Item>> api) {
    return new GenericRepository<>(cache, api);
  }

  @Provides
  @PerActivity
  ExplorePresenter providesExplorePresenter(GenericRepository<String, List<Item>> exploreDataSource) {
    return new ExplorePresenter(exploreDataSource);
  }
}
