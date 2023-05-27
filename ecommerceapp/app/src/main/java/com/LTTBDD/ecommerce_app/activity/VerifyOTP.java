package com.LTTBDD.ecommerce_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.LTTBDD.ecommerce_app.R;
import com.LTTBDD.ecommerce_app.common.config.ConnectToMysql;
import com.LTTBDD.ecommerce_app.common.service.MailService;
import com.LTTBDD.ecommerce_app.common.service.OTPService;
import com.LTTBDD.ecommerce_app.common.utils.StringUtils;
import com.LTTBDD.ecommerce_app.database.AuthDatabase;
import com.LTTBDD.ecommerce_app.model.User;
import com.chaos.view.PinView;

import java.sql.Connection;

public class VerifyOTP extends AppCompatActivity {
    PinView pinView;
    Button btnVerify;
    String email;
    Connection conn;
    AuthDatabase auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        initView();
        initControl();
    }

    private void initControl(){
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = pinView.getText().toString();
                OTPService otpService = new OTPService();
                Boolean checkVerify = otpService.verifyOTP(otp);
                if(checkVerify == false){
                    Toast.makeText(VerifyOTP.this, "Mã xác minh đã hết hạn", Toast.LENGTH_SHORT).show();
                }
                else if(checkVerify == null){
                    Toast.makeText(VerifyOTP.this, "Xảy ra lỗi trong quá trình xác minh", Toast.LENGTH_SHORT).show();
                }

                User user = auth.findByEmail(email);
                user.setPassword(StringUtils.generateRandomNumber());
                auth.forgotPassword(user.getPassword(), user.getId());

                String subject = "Cửa hàng LTTBĐ: Thông báo quên mật khẩu thành công!";
                String content = "Mật khẩu mới của bạn là: " + user.getPassword() + "! Sau khi nhận được mật khẩu, " +
                        "hãy thay đổi nó tại hệ thống!";

                MailService mailService = new MailService();
                boolean mailSender = mailService.sendForForgotPassword(email, subject, content);

                if(mailSender == true){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Thông báo đã được gửi qua " + email + " !Vui lòng kiểm tra!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Gửi mail thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void initView(){
        pinView = findViewById(R.id.pinview);
        btnVerify = findViewById(R.id.btnVerify);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        conn = ConnectToMysql.connect();
        auth = new AuthDatabase(conn);
    }
}