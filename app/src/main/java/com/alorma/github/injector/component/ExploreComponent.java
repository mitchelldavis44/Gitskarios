package com.alorma.github.injector.component;

import com.alorma.github.explore.datasource.ExplorePresenter;
import com.alorma.github.explore.fragment.ExploreFragment;
import com.alorma.github.injector.PerActivity;
import com.alorma.github.injector.module.ExploreModule;
import dagger.Component;

@PerActivity @Component(modules = ExploreModule.class) public interface ExploreComponent {

  ExplorePresenter getExplorePresenter();

  void inject(ExploreFragment exploreFragment);
}
