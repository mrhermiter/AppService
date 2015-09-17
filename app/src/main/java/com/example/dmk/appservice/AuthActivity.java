package com.example.dmk.appservice;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AuthActivity extends Activity {

    private Button btnLogin;
    private Button btnCancel;
    private EditText etLogin;
    private EditText etPass;
    public final static String CLOGIN = "cLogin";
    public final static String CPASS = "cPass";
    private SharedPreferences spAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnCancel = (Button) findViewById(R.id.btnCancelLogin);
        etLogin = (EditText) findViewById(R.id.etLogin);
        etPass = (EditText) findViewById(R.id.etPass);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = etLogin.getText().toString();
                String pass = etPass.getText().toString();
                if(login.isEmpty() || pass.isEmpty()){
                    Toast t =  Toast.makeText(getApplicationContext(),
                            R.string.login_error,
                            Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                    return;
                }
                Intent answerIntent = new Intent();
                answerIntent.putExtra(CLOGIN, login);
                answerIntent.putExtra(CPASS, pass);
                setResult(RESULT_OK, answerIntent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
