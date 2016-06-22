package com.alorma.github.explore.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.alorma.github.explore.datasource.ExplorePresenter;
import com.alorma.github.injector.component.DaggerExploreComponent;
import com.alorma.github.injector.component.ExploreComponent;
import com.alorma.github.injector.module.ExploreModule;
import com.alorma.github.ui.fragment.base.BaseFragment;
import javax.inject.Inject;

public class ExploreFragment extends BaseFragment {

  @Inject ExplorePresenter explorePresenter;

  public static ExploreFragment newInstance() {
    return new ExploreFragment();
  }

  @Override
  protected int getLightTheme() {
    return super.getLightTheme();
  }

  @Override
  protected int getDarkTheme() {
    return super.getDarkTheme();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ExploreComponent exploreComponent = DaggerExploreComponent.builder().exploreModule(new ExploreModule()).build();
    exploreComponent.inject(this);
  }

  @Override
  public void onStart() {
    super.onStart();

    explorePresenter.load();
  }
}
