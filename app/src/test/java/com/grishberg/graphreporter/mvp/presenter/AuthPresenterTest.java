package com.grishberg.graphreporter.mvp.presenter;

import com.grishberg.graphreporter.data.beans.common.RestError;
import com.grishberg.graphreporter.data.repository.auth.AuthTokenRepository;
import com.grishberg.graphreporter.data.repository.exceptions.NetworkException;
import com.grishberg.graphreporter.data.repository.exceptions.WrongCredentialsException;
import com.grishberg.graphreporter.data.repository.auth.AuthRepository;
import com.grishberg.graphreporter.mvp.view.AuthView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by grishberg on 12.01.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthPresenterTest {

    public static final String PASSWORD = "123456";
    public static final String LOGIN = "admin";
    public static final String SOME_MESSAGE = "some message";
    @Mock
    AuthTokenRepository authRepository;

    @Mock
    AuthRepository authService;

    @Mock
    AuthView authView;

    AuthPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new AuthPresenter();
        presenter.authService = authService;
        presenter.authRepository = authRepository;
        presenter.attachView(authView);
    }

    @Test
    public void testWhenEmptyLogin() {
        //given
        presenter.auth("", PASSWORD);
        verify(authView, times(1)).showLoginEmptyError();
        verify(authView, times(0)).showPasswordEmptyError();
        verify(authService, times(0)).login(anyString(), anyString());
    }

    @Test
    public void testWhenEmptyPassword() {
        //given
        presenter.auth(LOGIN, "");
        verify(authView, times(0)).showLoginEmptyError();
        verify(authView, times(1)).showPasswordEmptyError();
        verify(authService, times(0)).login(anyString(), anyString());
    }

    @Test
    public void testAuthRequestSuccess() {
        //when
        when(authService.login(anyString(), any(CharSequence.class)))
                .thenReturn(Observable.just(true));
        presenter.auth(LOGIN, PASSWORD);
        //then
        verify(authView, times(0)).showLoginEmptyError();
        verify(authView, times(0)).showPasswordEmptyError();
        verify(authView, times(0)).showFail(anyString());
        verify(authView, times(1)).showProgress();
        verify(authView, times(1)).hideProgress();
        verify(authView, times(1)).showSuccess();
        verify(authService, times(1)).login(LOGIN, PASSWORD);
    }

    @Test
    public void testAuthWrongCredentials() {
        //given
        final RestError error = mock(RestError.class);
        final Observable<Boolean> errorObservable = Observable.error(new WrongCredentialsException(error));
        when(authService.login(anyString(), any(CharSequence.class)))
                .thenReturn(errorObservable);
        //when
        presenter.auth(LOGIN, PASSWORD);
        //then
        verify(authView, times(1)).hideProgress();
        verify(authView, times(0)).showSuccess();
        verify(authView, times(1)).showWrongCredentials();
    }

    @Test
    public void testAuthNetworkError() {
        //given
        NetworkException mockException = mock(NetworkException.class);
        when(mockException.getMessage()).thenReturn(SOME_MESSAGE);
        when(authService.login(anyString(), anyString()))
                .thenReturn(Observable.error(mockException));
        //when
        presenter.auth(LOGIN, PASSWORD);
        //then
        verify(authView, times(1)).hideProgress();
        verify(authView, times(0)).showSuccess();
        verify(authView, times(0)).showWrongCredentials();
        verify(authView, times(1)).showFail(SOME_MESSAGE);
    }
}