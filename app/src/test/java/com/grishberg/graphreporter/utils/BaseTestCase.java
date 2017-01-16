package com.grishberg.graphreporter.utils;

import com.grishberg.graphreporter.App;
import com.grishberg.graphreporter.data.repository.AuthTokenRepository;
import com.grishberg.graphreporter.data.rest.Api;
import com.grishberg.graphreporter.di.AppComponent;
import com.grishberg.graphreporter.di.DaggerTestAppComponent;
import com.grishberg.graphreporter.di.DiManager;
import com.grishberg.graphreporter.di.TestRestModule;

import org.junit.Before;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

/**
 * Created by grishberg on 15.01.17.
 */
public class BaseTestCase {

    @Mock
    protected Api api;

    @Mock
    protected AuthTokenRepository tokenRepository;

    @Before
    public void setUp() throws Exception {
        final AppComponent component = DaggerTestAppComponent.builder()
                .testRestModule(new TestRestModule(tokenRepository, api))
                .build();
        DiManager.initComponents(component);
    }
}
