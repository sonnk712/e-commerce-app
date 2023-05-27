package com.LTTBDD.ecommerce_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.LTTBDD.ecommerce_app.R;
import com.LTTBDD.ecommerce_app.common.config.ConnectToMysql;
import com.LTTBDD.ecommerce_app.common.config.storage.DataLocalManager;
import com.LTTBDD.ecommerce_app.common.config.storage.StorageUser;
import com.LTTBDD.ecommerce_app.database.AuthDatabase;
import com.LTTBDD.ecommerce_app.model.User;
import com.google.gson.Gson;

import java.sql.Connection;

public class LoginActivity extends AppCompatActivity {
    TextView txtSignup, txtUsername, txtPassword, txtForgotPassword;
    Button btnLogin;
    Connection conn;
    AuthDatabase auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.LTTBDD.ecommerce_app.R.layout.activity_login);
        initView();
        initControl();
    }
    private void initControl(){
        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(register);
            }
        });
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotPassword = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(forgotPassword);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User currUser = auth.login(txtUsername.getText().toString(), txtPassword.getText().toString());
                if(currUser != null){
                    DataLocalManager.setCurrUser(currUser);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    finishAffinity();


                    //get CurrentUser
//                    User user = gson.fromJson(currUserStr, User.class);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void initView(){
        txtSignup = findViewById(R.id.txtSignUp);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        txtUsername = findViewById(R.id.usernameLogin);
        txtPassword = findViewById(R.id.passwordLogin);
        btnLogin = findViewById(R.id.btnLogin);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        txtUsername.setText(username);
        txtPassword.setText(password);
        conn = ConnectToMysql.connect();
        auth = new AuthDatabase(conn);
    }
}