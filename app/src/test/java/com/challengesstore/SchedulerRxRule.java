package com.challengesstore;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by felix on 1/8/18
 */

public class SchedulerRxRule implements TestRule {

    @Override
    public Statement apply(final Statement base, Description d) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxJavaPlugins.setIoSchedulerHandler(
                        scheduler -> Schedulers.trampoline());
                RxJavaPlugins.setComputationSchedulerHandler(
                        scheduler -> Schedulers.trampoline());
                RxJavaPlugins.setNewThreadSchedulerHandler(
                        scheduler -> Schedulers.trampoline());
                RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                        scheduler -> Schedulers.trampoline());

                try {
                    base.evaluate();
                } finally {
                    RxJavaPlugins.reset();
                    RxAndroidPlugins.reset();
                }
            }
        };
    }

    // fake response from server success
    public <T> Observable<Response<T>> getSuccessObservable(T response) {
        Response<T> fakeResponse =  Response.success(response);
        return Observable.just(fakeResponse);
    }

    // fake response from server error
    public <T> Observable<Response<T>>  getErrorObservable(int codeError,String textError) {
        Response<T> fakeResponse = Response.error(codeError, ResponseBody.create(null,textError));
        return Observable.just(fakeResponse);
    }
}
