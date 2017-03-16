package com.grishberg.graphreporter.mvp.presenter;

import com.grishberg.graphreporter.data.beans.AuthContainer;
import com.grishberg.graphreporter.data.repository.auth.AuthTokenRepository;
import com.grishberg.graphreporter.mvp.view.SplashScreenView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Created by grishberg on 15.01.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class SplashScreenPresenterTest {

    @Mock
    AuthTokenRepository repository;

    @Mock
    SplashScreenView view;

    SplashScreenPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new SplashScreenPresenter();
        presenter.repository = repository;
        presenter.attachView(view);
    }

    @Test
    public void testFirstLogin() {
        // given
        when(repository.getAuthInfo()).thenReturn(null);
        // when
        presenter.checkAuth();
        // then
        verify(view, times(1)).showLoginScreen();
        verify(view, times(0)).showMainScreen();
    }

    @Test
    public void testAlreadyLogined() {
        // given
        final AuthContainer authContainer = mock(AuthContainer.class);
        when(authContainer.getRefreshToken()).thenReturn("123456");
        when(repository.getAuthInfo()).thenReturn(authContainer);
        // when
        presenter.checkAuth();
        // then
        verify(view, times(0)).showLoginScreen();
        verify(view, times(1)).showMainScreen();
    }
}