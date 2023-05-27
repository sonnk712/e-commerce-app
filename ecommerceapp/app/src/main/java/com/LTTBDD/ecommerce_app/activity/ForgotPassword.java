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
import com.LTTBDD.ecommerce_app.common.service.MailService;
import com.LTTBDD.ecommerce_app.common.service.OTPService;
import com.LTTBDD.ecommerce_app.database.AuthDatabase;

import java.sql.Connection;

public class ForgotPassword extends AppCompatActivity {
    ImageView btnBack;
    TextView txtEmailForgotPassword;
    Button btnForgotPassword;
    Connection conn;
    AuthDatabase auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.LTTBDD.ecommerce_app.R.layout.activity_forgot_password);
        initView();
        initControl();
    }

    private void initView(){
        btnBack = findViewById(R.id.back);
        txtEmailForgotPassword = findViewById(R.id.txtEmailForgotPassword);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        conn = ConnectToMysql.connect();
        auth = new AuthDatabase(conn);
    }
    private void initControl(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmailForgotPassword.getText().toString();;
                String validateEmail = validateEmail(email);
                if(validateEmail == null){
                    OTPService otpService = new OTPService();
                    String otp = otpService.generate();
                    if(otp != null){
                        sendMail(email, otp);
                    }
                }
                else{
                    Toast.makeText(ForgotPassword.this, validateEmail, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private String validateEmail(String gmail){
        if(TextUtils.isEmpty(gmail)){
            return "Email không được để trống!";
        }

        String regexEmail = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
        if(!gmail.matches(regexEmail)){
            return "Email không hợp lệ!";
        }

        if(!auth.checkExistedEmail(gmail)){
            return "Địa chỉ email này không tồn tại trong dữ liệu cửa hàng!";
        }
        return null;
    }

    private void sendMail(String email, String otp){
        MailService mailService = new MailService();
        String subject = "Cửa hàng LTTBĐ: Thử gửi xác minh quên mật khẩu!";
        String content = "Để thực hiện thay đổi mật khẩu mới, hãy sử dụng mã " + otp + " để xác minh!";
        boolean mailSender = mailService.sendForForgotPassword(email, subject, content);
        if(mailSender == true){
            Intent intent = new Intent(this, VerifyOTP.class);
            intent.putExtra("email", email);
            startActivity(intent);
            Toast.makeText(this, "Tin nhẵn đã được gửi qua " + email + " !Vui lòng kiểm tra!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Gửi mail thất bại", Toast.LENGTH_SHORT).show();
        }

    }
}