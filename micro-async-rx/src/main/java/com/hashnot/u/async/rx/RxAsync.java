package com.hashnot.u.async.rx;

import com.hashnot.u.async.Async;
import rx.Observable;
import rx.Observer;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

/**
 * @author Rafał Krupiński
 */
public class RxAsync {
    public static <T> Observable<T> call(Callable<T> call, Executor executor) {
        Subject<T, T> subject = ReplaySubject.create();
        Async.call(call, executor, (r, t) -> handleResult(r, t, subject));
        return subject;
    }

    protected static <T> void handleResult(T result, Throwable throwable, Observer<T> observer) {
        if (throwable == null) {
            observer.onNext(result);
            observer.onCompleted();
        } else {
            observer.onError(throwable);
        }
    }
}
