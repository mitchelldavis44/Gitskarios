package com.alorma.github.presenter;

import com.fewlaps.quitnowcache.QNCache;
import com.fewlaps.quitnowcache.QNCacheBuilder;
import java.util.concurrent.TimeUnit;

public class CacheWrapper {

  private static QNCache qnCacheRepos;
  private static QNCache qnCacheExplore;

  public static QNCache reposCache() {
    if (qnCacheRepos == null) {
      qnCacheRepos = new QNCacheBuilder().setDefaultKeepaliveInMillis(TimeUnit.MINUTES.toMillis(10)).createQNCache();
    }
    return qnCacheRepos;
  }

  public static QNCache exploreCache() {
    if (qnCacheExplore == null) {
      qnCacheExplore = new QNCacheBuilder().setDefaultKeepaliveInMillis(TimeUnit.MINUTES.toMillis(10)).createQNCache();
    }
    return qnCacheExplore;
  }
}
