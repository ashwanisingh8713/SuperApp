package com.netoperation.net;


public interface RequestCallback<T> {

    void onNext(T t);
    void onError(Throwable t, String str);
    void onComplete(String str);
}
