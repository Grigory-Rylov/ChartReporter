package com.grishberg.graphreporter.di.modules;

import com.grishberg.graphreporter.data.repository.ValuesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by grishberg on 12.01.17.
 */
@Module
public class StorageModule {
    private final ValuesRepository valuesRepository;

    public StorageModule(final ValuesRepository valuesRepository) {
        this.valuesRepository = valuesRepository;
    }

    @Provides
    @Singleton
    ValuesRepository provideContext() {
        return valuesRepository;
    }
}
