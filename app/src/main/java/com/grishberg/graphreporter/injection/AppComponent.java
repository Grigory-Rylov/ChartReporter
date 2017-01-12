package com.grishberg.graphreporter.injection;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by grishberg on 12.01.17.
 */
@Singleton
@Component(modules = {RestModule.class,
        AppModule.class,
        ProfileModule.class,
        //DbModule.class
})

public interface AppComponent {
}
