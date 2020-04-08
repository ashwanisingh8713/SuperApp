package com.netoperation.net;


import android.content.Context;

public abstract class RequestCallbackContext<T> implements RequestCallback<T> {

    private Context context;

    public Context getContext() { return context ;}

    public void setContext(Context context) {
        this.context = context;
    }

}
