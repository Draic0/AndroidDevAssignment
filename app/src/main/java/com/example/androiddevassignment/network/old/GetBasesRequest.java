package com.example.androiddevassignment.network.old;

import android.support.annotation.NonNull;

import com.example.androiddevassignment.ConstVals;
import com.example.androiddevassignment.MainActivity;

public class GetBasesRequest extends OldRequest {

    private MainActivity activity;

    public GetBasesRequest(MainActivity activity, @NonNull ConstVals.Action action){
        super(action);
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        int idx = activity.model.parseDbData(s);
        activity.view.setIdx(idx);
        activity.view.fillDbData(activity,activity.model.getDatabases(),true);
        activity.view.requestLoginFocus();
        activity = null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        activity = null;
    }
}
