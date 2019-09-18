package com.example.androiddevassignment;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.androiddevassignment.model.BusinessLogics;
import com.example.androiddevassignment.view.ContentView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public ContentView view;
    public BusinessLogics model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = new ContentView(getWindow().getDecorView());
        model = new BusinessLogics();
        view.setServerAddressOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    ConstVals.serverAddress = view.getServerAddress();
                    model.fetchDataBases(MainActivity.this);
                }
            }
        });
        view.setServerAddressOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_GO){
                    ConstVals.serverAddress = view.getServerAddress();
                    model.fetchDataBases(MainActivity.this);
                }
                return true;
            }
        });
        view.setEnterListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = view.getSelectedDatabase().getId();
                String login = view.getLogin();
                String password = view.getPassword();
                model.loginAction(MainActivity.this,id,login,password);
            }
        });

        restoreInstanceState(savedInstanceState);
        view.fillDbData(this,model.getDatabases(),false);
    }

    private void restoreInstanceState(Bundle savedInstanceState){
        if(savedInstanceState==null){
            return;
        }
        view.restoreInstanceState(savedInstanceState);
        model.restoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        view.saveInstanceState(outState,outPersistentState);
        model.saveInstanceState(outState,outPersistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.requestInitialFocus();
    }
}
