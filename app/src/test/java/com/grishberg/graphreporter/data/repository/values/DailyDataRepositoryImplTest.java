package com.grishberg.graphreporter.data.repository.values;

import android.util.Log;

import com.grishberg.graphreporter.data.rest.RestConst;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * Created by grishberg on 15.03.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class DailyDataRepositoryImplTest {

    @Test
    public void testCancel() throws Exception {

        Observable<Long> observable = Observable
                .range(0, 1000)
                .concatMap(pageIndex -> {
                    long offset = pageIndex.longValue() * RestConst.PAGE_LIMIT;
                    System.out.println("concat map index = " + pageIndex);
                    return Observable.just(offset);
                })
                .takeWhile(response -> true)
                .takeLast(1)
                .subscribeOn(Schedulers.io())
                .doOnUnsubscribe(() -> {
                    System.out.println("doOnUnsubscribe");
                });

        Subscriber<Long> subscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Long aLong) {
                System.out.println("onNext: " + aLong);
            }
        };
        observable.subscribe(subscriber);
        Thread.sleep(20);
        System.out.println("testCancel: unsubscribe");
        subscriber.unsubscribe();
        Thread.sleep(1000);
    }
}