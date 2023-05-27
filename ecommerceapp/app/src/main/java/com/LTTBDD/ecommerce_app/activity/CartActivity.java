package com.LTTBDD.ecommerce_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.LTTBDD.ecommerce_app.R;
import com.LTTBDD.ecommerce_app.adapter.CartAdapter;
import com.LTTBDD.ecommerce_app.api.ApiService;
import com.LTTBDD.ecommerce_app.common.config.ConnectToMysql;
import com.LTTBDD.ecommerce_app.common.config.dto.ServiceResponse;
import com.LTTBDD.ecommerce_app.common.config.storage.DataLocalManager;
import com.LTTBDD.ecommerce_app.database.CartDatabase;
import com.LTTBDD.ecommerce_app.dto.ServiceResponse2;
import com.LTTBDD.ecommerce_app.model.Cart;
import com.LTTBDD.ecommerce_app.model.EvenBus.ChangeTotalPriceEvent;
import com.LTTBDD.ecommerce_app.model.ItemProduct;
import com.LTTBDD.ecommerce_app.model.Location.Ward;
import com.LTTBDD.ecommerce_app.model.Shipment.CostRequest;
import com.LTTBDD.ecommerce_app.model.Shipment.CostResponse;
import com.LTTBDD.ecommerce_app.model.Shipment.ShippingService;
import com.LTTBDD.ecommerce_app.model.Shipment.ShippingServiceRequest;
import com.LTTBDD.ecommerce_app.model.User;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    TextView emptyCart, totalPrice, price, shippingCost, taxCost, deliveryAddress, paymentMethod;
    ImageView selectPaymentMethod;
    Toolbar toolbarCart;
    RecyclerView recyclerViewCart;
    Button btnCheckout;
    CartAdapter adapter;
    List<ItemProduct> items;
    User currUser;
    Cart cart;
    Connection conn;
    CartDatabase cartDatabase;
    Spinner spShippingService;
    public static final String GHN_TOKEN = "2f5f932a-faaa-11ed-a281-3aa62a37e0a5";
    public static final int SHOP_ID = 3715493;
    public static final int FROM_DISTRICT = 1542;//Hà đông
    public static final String VNPAY_METHOD = "Thanh toán bằng ví điện thử VNPAY";
    public static final String COD_METHOD = "Thanh toán bằng COD";

    public static final String PAYPAL_METHOD = "Thanh toán bằng PayPal";
    public String methodSelected;
    public List<String> listServiceStr;
    List<ShippingService> listService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_v2);
        initView();
        initControl();
//        getShippingService();
        getShippingService1();
        getShippingCost();

        if(currUser.getAddress() == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
            builder.setTitle("Thông báo cập nhật thông tin");
            builder.setMessage("Để thực hiện tính toán chi phí giao hàng, bạn cần cập nhật thông tin cá nhân, bạn có muốn cập nhật ngay bây giờ?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    btnCheckout.setEnabled(false);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    private void initView(){
        paymentMethod = findViewById(R.id.textView22);
//        emptyCart = findViewById(R.id.txtEmptyCart);
        selectPaymentMethod = findViewById(R.id.imageView7);
        deliveryAddress = findViewById(R.id.textView20);
        taxCost = findViewById(R.id.textView13);
        shippingCost = findViewById(R.id.textView11);
        price = findViewById(R.id.textView8);
        totalPrice = findViewById(R.id.textView15);
        toolbarCart = findViewById(R.id.toolBarCart);
        recyclerViewCart = findViewById(R.id.recycleViewCart1);
        btnCheckout = findViewById(R.id.button);
        spShippingService = findViewById(R.id.spShippingService);
        methodSelected = COD_METHOD;
    }

    private void getShippingService(){
        ShippingServiceRequest request = new ShippingServiceRequest(3715493, 1542, 2264);
        Gson gson = new Gson();
        String json = gson.toJson(request);
        ApiService.apiService.getShippingService(GHN_TOKEN, json).enqueue(new Callback<ServiceResponse2<List<ShippingService>>>() {
            @Override
            public void onResponse(Call<ServiceResponse2<List<ShippingService>>> call, Response<ServiceResponse2<List<ShippingService>>> response) {
                ServiceResponse2<List<ShippingService>> data = response.body();
                if(data != null && data.getCode() == 200){
                    List<String> dataArr = new ArrayList<>();
                    for(ShippingService i : data.getData()){
                        dataArr.add(i.getShortName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dataArr);
                    spShippingService.setAdapter(adapter);
                }
                else{
                    Toast.makeText(CartActivity.this, "Danh sách dịch vụ hiện đang trống", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ServiceResponse2<List<ShippingService>>> call, Throwable t) {

            }
        });
    }

    private void getShippingCost(){
        String serviceSelected = spShippingService.getSelectedItem().toString();
        int serviceIdSelected = 0;
        for(ShippingService i : listService){
            if(i.getShortName().equals(serviceSelected)){
                serviceIdSelected = i.getServiceId();
            }
        }
        if(serviceIdSelected == 0){
            Toast.makeText(this, "Dịch vụ hiện đang không hoạt động", Toast.LENGTH_SHORT).show();
        }

        CostRequest request = new CostRequest(SHOP_ID, serviceIdSelected, 500000, null,
                FROM_DISTRICT, currUser.getDistrictId(), currUser.getWardId(), 15, 15, 1000, 15);
        ApiService.apiService.getShippingCost(GHN_TOKEN, request).enqueue(new Callback<ServiceResponse<CostResponse>>() {
            @Override
            public void onResponse(Call<ServiceResponse<CostResponse>> call, Response<ServiceResponse<CostResponse>> response) {
                ServiceResponse<CostResponse> data = response.body();
                if(data != null && data.getCode() == 200){
                    DecimalFormat df = new DecimalFormat("###,###,###đ");
                    shippingCost.setText(df.format(data.getData().getTotal()));
                    Double priceDbl = decimalToDouble(price.getText().toString());
                    Double taxCostDbl = priceDbl / 100 * 2;
                    Double totalPriceDbl = data.getData().getTotal() + priceDbl + taxCostDbl;
                    taxCost.setText(df.format(taxCostDbl));
                    totalPrice.setText(df.format(totalPriceDbl));
                }
                else{
                    Toast.makeText(getApplicationContext(), "Lỗi trong quá trình tính phí", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse<CostResponse>> call, Throwable t) {

            }
        });
    }

    private Double decimalToDouble(String cost){
        try {
            DecimalFormat df = new DecimalFormat("###,###,###đ");
            Number parsedNumber = df.parse(cost);
            double amount = parsedNumber.doubleValue();
            return amount;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initControl(){
        setSupportActionBar(toolbarCart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarCart.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerViewCart.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewCart.setLayoutManager(layoutManager);
        paymentMethod.setText(COD_METHOD);
        currUser = DataLocalManager.getCurrUser();

        conn = ConnectToMysql.connect();;
        cartDatabase = new CartDatabase(conn);
        cart = cartDatabase.getCartFromUser(currUser.getUsername());
        items = cartDatabase.getListItemByCart(cart.getId());

        selectPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditAddressDialog();
            }
        });

        if(items.size() == 0){
            Intent intent = new Intent(CartActivity.this, CartEmpty.class);
            startActivity(intent);
            finish();
        }
        else{
            adapter = new CartAdapter(items, getApplicationContext());
            recyclerViewCart.setAdapter(adapter);
            btnCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if(currUser != null){
                       Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                       intent.putExtra("totalPrice", cart.getTotalPrice());
                       intent.putExtra("paymentMethod", methodSelected);
                       startActivity(intent);
                       finish();
                   }
               }
            });
        }

        DecimalFormat df = new DecimalFormat("###,###,###đ");
        price.setText(df.format(cart.getTotalPrice()));
        deliveryAddress.setText(currUser.getAddress());
        spShippingService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getShippingCost();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void showEditAddressDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_payment_selection);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.show();

        RadioButton codPay = dialog.findViewById(R.id.radioButton1);
        RadioButton vnPay = dialog.findViewById(R.id.radioButton2);
        RadioButton payPal = dialog.findViewById(R.id.radioButton3);

        codPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codPay.setChecked(true);
                vnPay.setChecked(false);
                payPal.setChecked(false);

            }
        });
        vnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codPay.setChecked(false);
                vnPay.setChecked(true);
                payPal.setChecked(false);
            }
        });
        payPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codPay.setChecked(false);
                vnPay.setChecked(false);
                payPal.setChecked(true);
            }
        });
        TextView cancle = dialog.findViewById(R.id.btnCancle);
        TextView accept = dialog.findViewById(R.id.btnUpdatePassword);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(codPay.isChecked() && !vnPay.isChecked() && !payPal.isChecked()) {
                    paymentMethod.setText(COD_METHOD);
                    methodSelected = COD_METHOD;
                }
                else if(!codPay.isChecked() && vnPay.isChecked() && !payPal.isChecked()){
                    paymentMethod.setText(VNPAY_METHOD);
                    methodSelected = VNPAY_METHOD;
                }
                else if(payPal.isChecked() && !vnPay.isChecked() && !codPay.isChecked()){
                    paymentMethod.setText(PAYPAL_METHOD);
                    methodSelected = PAYPAL_METHOD;
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void ChangeTotalPriceEvent(ChangeTotalPriceEvent event){
        if(event != null){
            cart = cartDatabase.getCartFromUser(currUser.getUsername());
            DecimalFormat df = new DecimalFormat("###,###,###đ");
            price.setText(df.format(cart.getTotalPrice()));
//
//            double taxDbl = cart.getTotalPrice() * 100 / 10;
//            taxCost.setText(df.format(taxDbl));
            getShippingCost();
            items = cartDatabase.getListItemByCart(cart.getId());

            adapter = new CartAdapter(items, getApplicationContext());
            recyclerViewCart.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
    }

    private void getShippingService1(){
        listService = new ArrayList<>();
        ShippingService service1 = new ShippingService(53321, "Chuyển phát thương mại điện tử", 2);
        ShippingService service2 = new ShippingService(100039, "Chuyển phát truyền thống", 5);
        listService.add(service1);
        listService.add(service2);

        listServiceStr = new ArrayList<>();
        listServiceStr.add(service1.getShortName());
        listServiceStr.add(service2.getShortName());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listServiceStr);
        spShippingService.setAdapter(adapter);
    }

}