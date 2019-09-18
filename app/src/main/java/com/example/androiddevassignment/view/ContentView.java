package com.example.androiddevassignment.view;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.androiddevassignment.Database;
import com.example.androiddevassignment.R;

import java.util.ArrayList;

public class ContentView {

    private EditText serverAddress, login, password;
    private Spinner databases;
    private Button enter;
    private int idx;

    public View.OnFocusChangeListener listener;

    public ContentView(View rootView){
        serverAddress = rootView.findViewById(R.id.server_address);
        login = rootView.findViewById(R.id.login);
        password = rootView.findViewById(R.id.password);
        databases = rootView.findViewById(R.id.databases);
        enter = rootView.findViewById(R.id.enter);
    }

    public void requestInitialFocus(){
        serverAddress.requestFocus();
    }
    public void requestLoginFocus(){
        serverAddress.setOnFocusChangeListener(null);
        login.requestFocus();
        serverAddress.setOnFocusChangeListener(listener);
    }

    public void fillDbData(Context context, ArrayList<Database> data, boolean showWarning){
        if(data==null){
            fillDefaultDbData(context,showWarning);
            return;
        }
        fillDbData(context,data);
    }

    private void fillDefaultDbData(Context context, boolean showWarning){
        String[] content = context.getResources().getStringArray(R.array.db_default);
        databases.setAdapter(new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item,content));
        login.setText("", TextView.BufferType.EDITABLE);
        login.setEnabled(false);
        password.setText("", TextView.BufferType.EDITABLE);
        password.setEnabled(false);
        enter.setEnabled(false);
        if(showWarning) {
            AlertDialog.Builder ab = new AlertDialog.Builder(context, R.style.AlertDialog);
            ab.setMessage(R.string.connection_error).setPositiveButton("OK", null)
                    .create().show();
        }
    }

    private void fillDbData(Context context, ArrayList<Database> data){
        ArrayAdapter<Database> ad = new ArrayAdapter<Database>(context,
                android.R.layout.simple_spinner_dropdown_item,data);
        databases.setAdapter(ad);
        if(idx>=0){
            databases.setSelection(idx);
        }
        login.setEnabled(true);
        password.setEnabled(true);
        enter.setEnabled(true);
    }

    public void setIdx(int idx){
        this.idx = idx;
    }

    public void setEnterListener(View.OnClickListener listener){
        enter.setOnClickListener(listener);
    }
    public void setServerAddressOnFocusChangeListener(View.OnFocusChangeListener listener){
        this.listener = listener;
        serverAddress.setOnFocusChangeListener(listener);
    }
    public void setServerAddressOnEditorActionListener(TextView.OnEditorActionListener listener){
        serverAddress.setOnEditorActionListener(listener);
    }

    public String getServerAddress(){
        return serverAddress.getText().toString();
    }
    public Database getSelectedDatabase(){
        return ((Database)databases.getSelectedItem());
    }
    public String getLogin(){
        return login.getText().toString();
    }
    public String getPassword(){
        return password.getText().toString();
    }

    public void restoreInstanceState(Bundle savedInstanceState){
        if(savedInstanceState==null){
            return;
        }
        serverAddress.setText(savedInstanceState.getString("serverAddress"));
        idx = savedInstanceState.getInt("idx");
        login.setText(savedInstanceState.getString("login"));
        password.setText(savedInstanceState.getString("password"));
    }

    public void saveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString("serverAddress",serverAddress.getText().toString());
        outState.putLong("idx",databases.getSelectedItemId());
        outState.putString("login",login.getText().toString());
        outState.putString("password",password.getText().toString());
    }
}
