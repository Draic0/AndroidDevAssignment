package com.example.androiddevassignment;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public EditText serverAddress, login, password;
    public View.OnFocusChangeListener listener;
    private Spinner databases;
    private Button enter;
    private ArrayList<Database> dbcontents;
    private int defIdx = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serverAddress = findViewById(R.id.server_address);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        databases = findViewById(R.id.databases);
        enter = findViewById(R.id.enter);
        listener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    ConstVals.serverAddress = serverAddress.getText().toString();
                    fetchDataBases();
                }
            }
        };
        serverAddress.setOnFocusChangeListener(listener);
        serverAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_GO){
                    ConstVals.serverAddress = serverAddress.getText().toString();
                    fetchDataBases();
                }
                return true;
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAction();
            }
        });
        fillDbData(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        serverAddress.requestFocus();
    }

    private void fetchDataBases(){
        Request request1 = new Request(ConstVals.Action.RequestBases);
        request1.execute();
        Request request2 = new GetBasesRequest(this, ConstVals.Action.GetBases);
        request2.execute();
    }

    public void parseDbData(String s){
        if(s==null){
            dbcontents = null;
            defIdx = -1;
            return;
        }
        dbcontents = new ArrayList<>();
        try{
            JSONArray arr = new JSONArray(s);
            if(arr.length()==0){
                dbcontents = null;
                defIdx = -1;
                return;
            }
            JSONObject data = arr.getJSONObject(0);
            data = new JSONObject(data.getString("Message"));
            arr = data.getJSONArray("Databases");
            for(int i = 0;i<arr.length();i++){
                data = arr.getJSONObject(i);
                Database db = new Database(data.getString("DatabaseId"),data.getString("DisplayName"));
                if(data.getBoolean("IsDefault")){
                    defIdx = i;
                }
                dbcontents.add(db);
            }
        }catch(JSONException exc){
            Log.e(TAG,Log.getStackTraceString(exc));
            dbcontents = null;
            defIdx = -1;
        }
    }

    public void fillDbData(boolean showWarning){
        if(dbcontents==null){
            String[] content = getResources().getStringArray(R.array.db_default);
            databases.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_dropdown_item,content));
            login.setText("", TextView.BufferType.EDITABLE);
            login.setEnabled(false);
            password.setText("", TextView.BufferType.EDITABLE);
            password.setEnabled(false);
            enter.setEnabled(false);
            if(showWarning) {
                AlertDialog.Builder ab = new AlertDialog.Builder(this, R.style.AlertDialog);
                ab.setMessage(R.string.connection_error).setPositiveButton("OK", null)
                        .create().show();
            }
            return;
        }
        ArrayAdapter<Database> ad = new ArrayAdapter<Database>(this,
                android.R.layout.simple_spinner_dropdown_item,dbcontents);
        databases.setAdapter(ad);
        if(defIdx>=0){
            databases.setSelection(defIdx);
        }
        login.setEnabled(true);
        password.setEnabled(true);
        enter.setEnabled(true);
    }

    private void loginAction(){
        Request request1 = new Request(ConstVals.Action.RequestToken);
        request1.execute(((Database)databases.getSelectedItem()).getId(),
                login.getText().toString(),password.getText().toString());
        Request request2 = new GetTokenRequest(this,ConstVals.Action.GetToken);
        request2.execute();
    }
}
