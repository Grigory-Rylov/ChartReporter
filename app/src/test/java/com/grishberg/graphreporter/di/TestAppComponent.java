package com.grishberg.graphreporter.di;

import com.grishberg.graphreporter.data.repository.BaseRestRepository;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by grishberg on 15.01.17.
 */
@Singleton
@Component(modules = {TestRestModule.class})
public interface TestAppComponent extends AppComponent {
    void inject(BaseRestRepository baseRestRepository);
}

