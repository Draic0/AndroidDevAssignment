package com.example.androiddevassignment;

import android.support.annotation.NonNull;

public class GetBasesRequest extends Request {

    private MainActivity activity;

    public GetBasesRequest(MainActivity activity, @NonNull ConstVals.Action action){
        super(action);
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        activity.parseDbData(s);
        activity.fillDbData(true);
        activity.serverAddress.setOnFocusChangeListener(null);
        activity.login.requestFocus();
        activity.serverAddress.setOnFocusChangeListener(activity.listener);
        activity = null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        activity = null;
    }
}
