package com.LTTBDD.ecommerce_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.LTTBDD.ecommerce_app.R;
import com.LTTBDD.ecommerce_app.common.config.ConnectToMysql;
import com.LTTBDD.ecommerce_app.database.AuthDatabase;
import com.LTTBDD.ecommerce_app.database.CartDatabase;
import com.LTTBDD.ecommerce_app.dto.RegisterInfo;
import com.LTTBDD.ecommerce_app.model.User;

import java.sql.Connection;

public class RegisterActivity extends AppCompatActivity {
    TextView txtUsername, txtGmail, txtPassword, txtConfirmPassword, txtSignIn;
    Button signUp;
    ImageView backRegister;
    Connection conn;
    AuthDatabase auth;

    CartDatabase cartData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.LTTBDD.ecommerce_app.R.layout.activity_register);
        initView();
        initControl();
    }

    private void initControl(){
        backRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check = validateInput();
                if(check == null){
                    Integer insertCart = cartData.creatCart();
                    if(insertCart != null || insertCart != 0){
                        RegisterInfo request = new RegisterInfo();
                        request.setUsername(txtUsername.getText().toString());
                        request.setPassword(txtPassword.getText().toString());
                        request.setRePassword(txtConfirmPassword.getText().toString());
                        request.setCartId(insertCart);

                        request.setGmail(txtGmail.getText().toString());
                        User user = auth.register(request);
                        if(user != null){
                            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                            login.putExtra("username", user.getUsername());
                            login.putExtra("password", user.getPassword());
                            startActivity(login);
                            Toast.makeText(getApplicationContext(), "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            finishAffinity();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Đăng ký tài khoản không thành công!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), check, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String validateInput(){
        String gmail = txtGmail.getText().toString();
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        String confirmPassword = txtConfirmPassword.getText().toString();

        if(TextUtils.isEmpty(username)){
            return "Tài khoản không được để trống!";
        }
        if(TextUtils.isEmpty(gmail)){
            return "Email không được để trống!";
        }
        if(TextUtils.isEmpty(password)){
            return "Mật khẩu không được để trống!";
        }
        if(TextUtils.isEmpty(confirmPassword)){
            return "Mật khẩu xác nhận không được để trống!";
        }
        String regexPassword = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        if(!password.matches(regexPassword)){
            return "Mật khẩu không hợp lệ";
        }

        if(!confirmPassword.matches(regexPassword)){
            return "Mật khẩu xác nhận không hợp lệ";
        }

        if(!password.equals(confirmPassword)){
            return "Mật khẩu xác nhận không khớp!";
        }
        String regexEmail = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
        if(!gmail.matches(regexEmail)){
            return "Email không hợp lệ1";
        }

        if(auth.checkExistedUsername(username)){
            return "Tài khoản đã tồn tại. Vui lòng nhập lại!";
        }
        if(auth.checkExistedEmail(gmail)){
            return "Email đã tồn tại. Vui lòng nhập lại!";
        }
        return null;
    }

    private void initView(){
        txtUsername = findViewById(R.id.username);
        txtGmail = findViewById(R.id.email);
        txtPassword = findViewById(R.id.password);
        txtConfirmPassword = findViewById(R.id.confirmPassword);
        txtSignIn = findViewById(R.id.txtSignIn);
        signUp = findViewById(R.id.signUp);
        backRegister = findViewById(R.id.backRegister);
        conn = ConnectToMysql.connect();
        auth = new AuthDatabase(conn);
        cartData = new CartDatabase(conn);
    }
}