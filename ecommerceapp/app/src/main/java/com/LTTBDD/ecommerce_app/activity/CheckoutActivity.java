package com.LTTBDD.ecommerce_app.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.LTTBDD.ecommerce_app.R;
import com.LTTBDD.ecommerce_app.common.config.ConnectToMysql;
import com.LTTBDD.ecommerce_app.common.config.constants.Constants;
import com.LTTBDD.ecommerce_app.common.config.storage.DataLocalManager;
import com.LTTBDD.ecommerce_app.common.service.MailService;
import com.LTTBDD.ecommerce_app.database.CartDatabase;
import com.LTTBDD.ecommerce_app.database.OrderDatabase;
import com.LTTBDD.ecommerce_app.model.Cart;
import com.LTTBDD.ecommerce_app.model.CreateOrder;
import com.LTTBDD.ecommerce_app.model.ItemProduct;
import com.LTTBDD.ecommerce_app.model.Order;
import com.LTTBDD.ecommerce_app.model.OrderDetail;
import com.LTTBDD.ecommerce_app.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmActivity;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CheckoutActivity extends AppCompatActivity {
    Toolbar toolbarCheckout;
    TextView txtTotalPriceCheckout, txtPhoneNumberCheckout, txtEmailCheckout, txtPaymentMethodCheckout;
    TextInputEditText addressInput, noteInput;

    AppCompatButton btnCheckoutPayment;
    User currUser;
    Connection conn;
    OrderDatabase orderDatabase;
    CartDatabase cartDatabase;
    String paymenMethod;
    public static final String VNPAY_METHOD = "Thanh toán bằng ví điện thử VNPAY";
    public static final String COD_METHOD = "Thanh toán bằng COD";
    public static final String PAYPAL_METHOD = "Thanh toán bằng PayPal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.LTTBDD.ecommerce_app.R.layout.activity_checkout);

        initView();
        initControl();
    }
    private void initView(){
        txtPaymentMethodCheckout = findViewById(R.id.txtPaymentMethodCheckout);
        toolbarCheckout = findViewById(R.id.toolBarCheckout);
        txtTotalPriceCheckout = findViewById(R.id.txtTotalPriceCheckout);
        txtPhoneNumberCheckout = findViewById(R.id.txtPhoneNumberCheckout);
        txtEmailCheckout = findViewById(R.id.txtEmailCheckout);
        addressInput = findViewById(R.id.addressInput);
        noteInput = findViewById(R.id.noteInput);
        btnCheckoutPayment = findViewById(R.id.btnCheckoutPayment);

        currUser = DataLocalManager.getCurrUser();
        conn = ConnectToMysql.connect();
        orderDatabase = new OrderDatabase(conn);
        cartDatabase = new CartDatabase(conn);

    }

    private void initControl(){

        setSupportActionBar(toolbarCheckout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarCheckout.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        Double totalPrice = intent.getDoubleExtra("totalPrice", 0);
        paymenMethod = intent.getStringExtra("paymentMethod");
        DecimalFormat df = new DecimalFormat("###,###,###đ");
        txtPaymentMethodCheckout.setText(paymenMethod);
        txtTotalPriceCheckout.setText(df.format(totalPrice));
        addressInput.setText(currUser.getAddress());
        addressInput.setEnabled(false);
        txtEmailCheckout.setText(currUser.getEmail());
        txtPhoneNumberCheckout.setText(currUser.getPhoneNumber());

        btnCheckoutPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);
                builder.setTitle("Thông báo!");
                builder.setMessage("Bạn có chắc chắn muốn đặt hàng?");
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int pos) {
                        String address = addressInput.getText().toString();
                        String note = noteInput.getText().toString();
                        if(TextUtils.isEmpty(address)){
                            Toast.makeText(CheckoutActivity.this, "Bạn chưa nhập địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
                            addressInput.requestFocus();
                            return;
                        }
                        List<ItemProduct> items = cartDatabase.getListItemByCart(currUser.getCartId());

                        Order order = new Order();
                        order.setTotalPrice(totalPrice);
                        order.setDeliveryAddress(address);
                        order.setNote(note);
                        order.setCreatedDate(new Date());
                        order.setStatus(Constants.BEING_DELIVERED);
                        order.setUserId(currUser.getId());
                        order.setQuantity(items.size());

                        Order result = orderDatabase.saveOrder(order);
                        if(result != null){
                            if(items != null){
                                for (ItemProduct i : items){
                                    OrderDetail orderDetail = new OrderDetail();
                                    orderDetail.setQuantity(i.getQuantity());
                                    orderDetail.setPrice(i.getTotalPrice());
                                    orderDetail.setProductId(i.getProductId());
                                    orderDetail.setOrderId(result.getId());

                                    OrderDetail response = orderDatabase.saveOrderDetail(orderDetail);
                                    if(response == null){
                                        Toast.makeText(CheckoutActivity.this, "Lỗi trong quá trình lưu chi tiết đơn hàng", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }
                            else{
                                Toast.makeText(CheckoutActivity.this, "Không tìm thấy chi tiết đơn hàng", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Boolean emptyItems = cartDatabase.removeAllItem(currUser.getCartId());
                            Boolean emptyCartCheck=  cartDatabase.emptyCart(currUser.getCartId());
                            if(!emptyCartCheck || !emptyItems){
                                Toast.makeText(CheckoutActivity.this, "Lỗi trong quá trình lưu đơn hàng", Toast.LENGTH_SHORT).show();
                                return;
                            }


                            MailService maiSender = new MailService();
                            String subject = "Cửa hàng LTTBDĐ: Thông báo đã đặt thành công đơn hàng!";
                            String content = "Xin chào " + currUser.getEmail() + ", rất cảm ơn vì đã mua hàng của chúng tôi! " +
                                    "Chúng tôi sẽ gửi đơn hàng ngay khi có thể! " +
                                    "Bạn có thể theo dõi trạng thái đơn hàng trong ứng dụng." +
                                    "Trân trọng!";
                            maiSender.sendForForgotPassword(currUser.getEmail(), subject, content);

                            if(paymenMethod.equals(VNPAY_METHOD)){
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                ZaloPaySDK.init(2554, Environment.SANDBOX);
                                requestZalo();
                            }
                            else if(paymenMethod.equals(PAYPAL_METHOD)){
                                onBuyPressed(result);
                                Intent intent = new Intent(CheckoutActivity.this, PayPalService.class);
                                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                                startService(intent);
                            }
                            else if(paymenMethod.equals(COD_METHOD)){
                                Intent intent = new Intent(CheckoutActivity.this, OrderConfirm.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                        else{
                            Toast.makeText(CheckoutActivity.this, "Lỗi trong quá trình lưu đơn", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    private void requestZalo(){
        CreateOrder orderApi = new CreateOrder();
        try {
            JSONObject data = orderApi.createOrder("100000");
            String code = data.getString("return_code");

            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                Log.d("test", token);
                ZaloPaySDK.getInstance().payOrder(CheckoutActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        Intent confirm = new Intent(getApplicationContext(), OrderConfirm.class);
                        startActivity(confirm);
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {

                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    public void onBuyPressed(Order order){
        double priceUSA = order.getTotalPrice() / 23000;
        PayPalPayment payment = new PayPalPayment(new BigDecimal(priceUSA), "USD", "Mã đơn hàng: " + order.getId(), PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Activity.RESULT_OK){
            PaymentConfirmActivity confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if(confirm != null){
                try {
                    Log.i("paymentExample", confirm.toString());
                    Toast.makeText(confirm, "Thanh toán thành côngl!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CheckoutActivity.this, OrderConfirm.class);
                    startActivity(intent);
                    finish();
                }
                catch (Exception e){
                    Toast.makeText(confirm, "Thanh toán với ví Paypal thất bại", Toast.LENGTH_SHORT).show();
                    Log.e("paymentExample", "error: " + e);
                }
            }
        }
        else if(requestCode == Activity.RESULT_CANCELED){
            Log.e("paymentExample", "Cancelled: " );
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId("AVEXmaDt_D_cYkO0E3gCNvJOYWVq5uOFDwoAIsz8upP4N3qx-TOlpITXfR_j5EKuSAIZwL6SndlRA8L7");
}