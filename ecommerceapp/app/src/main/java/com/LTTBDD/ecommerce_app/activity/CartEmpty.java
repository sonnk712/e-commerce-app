package com.LTTBDD.ecommerce_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.LTTBDD.ecommerce_app.R;

public class CartEmpty extends AppCompatActivity {
    Button continueShopping;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.LTTBDD.ecommerce_app.R.layout.activity_cart_empty);
        continueShopping = findViewById(R.id.continueShopping);
        continueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartEmpty.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}