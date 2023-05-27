package com.LTTBDD.ecommerce_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.LTTBDD.ecommerce_app.R;
import com.LTTBDD.ecommerce_app.adapter.CategoryAdapter;
import com.LTTBDD.ecommerce_app.adapter.NewProductAdapter;
import com.LTTBDD.ecommerce_app.common.config.ConnectToMysql;
import com.LTTBDD.ecommerce_app.common.config.constants.Constants;
import com.LTTBDD.ecommerce_app.common.config.dto.ServiceResponse;
import com.LTTBDD.ecommerce_app.common.config.storage.DataLocalManager;
import com.LTTBDD.ecommerce_app.database.CartDatabase;
import com.LTTBDD.ecommerce_app.database.CategoryData;
import com.LTTBDD.ecommerce_app.database.NewProductData;
import com.LTTBDD.ecommerce_app.model.Cart;
import com.LTTBDD.ecommerce_app.model.Category;
import com.LTTBDD.ecommerce_app.model.ItemProduct;
import com.LTTBDD.ecommerce_app.model.NewProduct;
import com.LTTBDD.ecommerce_app.model.User;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
, SearchView.OnQueryTextListener{
    SearchView searchViewHome;
    ImageView currImage;
    TextView currName, currEmail;
    Toolbar toolbarMain;
    ViewFlipper viewFlipperMain;
    RecyclerView recycleViewMain;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    FrameLayout frameLayoutMain, framelayoutProfile;
    CategoryAdapter categoryAdapter;
    List<Category> categoryList;

    NotificationBadge badge;
    User currUser;
    Cart cart;
    Connection conn;
    CartDatabase cartDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        actionBar();

        if(isConnected(this)){
            Toast.makeText(getApplicationContext(), currUser.getUsername(), Toast.LENGTH_SHORT).show();
            actionFlipper();
            Connection conn = ConnectToMysql.connect();
//            getCategory(conn);
            getNewProductList(conn);
        }
        else{
            Toast.makeText(getApplicationContext(), "Không có internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void getNewProductList(Connection conn){
        try {
            NewProductData newProductData = new NewProductData(conn);

            ServiceResponse<List<NewProduct>> newProductList = newProductData.getList();
            if(newProductList.getCode() == Constants.TRUE){
                NewProductAdapter newProductAdapter = new NewProductAdapter(newProductList.getData(), getApplicationContext());
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
                recycleViewMain.setLayoutManager(layoutManager);
                recycleViewMain.setHasFixedSize(true);
                recycleViewMain.setAdapter(newProductAdapter);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void actionFlipper(){
        List<String> advertises = new ArrayList<>();
        advertises.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        advertises.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        advertises.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        for(int i = 0; i < advertises.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(advertises.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipperMain.addView(imageView);
        }
        viewFlipperMain.setFlipInterval(3000);
        viewFlipperMain.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipperMain.setInAnimation(slide_in);
        viewFlipperMain.setOutAnimation(slide_out);
    }
    private void actionBar(){
        setSupportActionBar(toolbarMain);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        toolbarMain.setTitleTextColor(getResources().getColor(R.color.white));
        toolbarMain.setNavigationIcon(R.drawable.baseline_menu_24);
        toolbarMain.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbarMain,
                R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }
    private void initView(){
        searchViewHome = findViewById(R.id.searchViewHome);
        navigationView = findViewById(R.id.navigationView);
//        currImage = findViewById(R.id.currImage);
//        currName = findViewById(R.id.currName);
//        currEmail = findViewById(R.id.currEmail);
        toolbarMain = findViewById(R.id.toolbarMain);
        viewFlipperMain = findViewById(R.id.viewFlipperMain);
        recycleViewMain = findViewById(R.id.recycleViewMain);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawerLayout);
        frameLayoutMain = findViewById(R.id.framelayoutMain);
        frameLayoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });
        framelayoutProfile = findViewById(R.id.framelayoutProfile);

        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList, getApplicationContext());
        badge = findViewById(R.id.badge);

//        currUser = new User(1, "sonnk712@gmail.com", "123456", "sonefast712@gmail.com",
//                "Nguyễn Khắc Sơn", "Hạ Mỗ, Đan Phượng, Hà Nội", 1, new Date(), null, "0358785476", 1);
        currUser = DataLocalManager.getCurrUser();

        View hView = navigationView.inflateHeaderView(R.layout.layout_header_nav);
        ImageView currImg = (ImageView)hView.findViewById(R.id.currImage);
        TextView currName = (TextView)hView.findViewById(R.id.currName);
        TextView currEmail = (TextView)hView.findViewById(R.id.currEmail);

        if(TextUtils.isEmpty(currUser.getImg())){
            currImg.setImageResource(R.drawable.user_icon_2);
        }
        else{
            Uri uri = Uri.parse(currUser.getImg());
            Glide.with(getApplicationContext()).load(uri).into(currImg);
        }

        currName.setText(currUser.getName());
        currEmail.setText(currUser.getEmail());

        conn = ConnectToMysql.connect();;
        cartDatabase = new CartDatabase(conn);
        cart = cartDatabase.getCartFromUser(currUser.getUsername());
        List<ItemProduct> items = cartDatabase.getListItemByCart(cart.getId());
        badge.setText(Integer.toString(items.size()));

        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

        EditText searchEditText = searchViewHome.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorPrimary));
        searchEditText.setHint("Tìm kiếm sản phẩm");
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        searchEditText.setTextSize(14);
        searchViewHome.setOnQueryTextListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<ItemProduct> items = cartDatabase.getListItemByCart(cart.getId());
        badge.setText(Integer.toString(items.size()));
    }

    private boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())){
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_home){
            item.setChecked(true);
            recreate();
        }else if(id == R.id.nav_mobile){
            item.setChecked(true);
            redirectActivity(MainActivity.this, MobileActivity.class);
        }
        else if(id == R.id.nav_laptop){
            item.setChecked(true);
            redirectActivity(MainActivity.this, LaptopActivity.class);
        }
        else if(id == R.id.nav_history){
            item.setChecked(true);
            redirectActivity(MainActivity.this, HistoryActivity.class);
        }
        else if(id == R.id.nav_signout){
            item.setChecked(true);
            DataLocalManager.logout();
            Intent logout = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(logout);
        }
        else if(id == R.id.nav_search){
            item.setChecked(true);
            redirectActivity(MainActivity.this, SearchActivity.class);
        }
        else if(id == R.id.nav_contact_us){
            item.setChecked(true);
            redirectActivity(MainActivity.this, ContactInforActivity.class);
        }
        else if(id == R.id.nav_about_us){
            item.setChecked(true);
            redirectActivity(MainActivity.this, AboutUsActivity.class);
        }
        else if(id == R.id.nav_myprofile){
            item.setChecked(true);
            redirectActivity(MainActivity.this, ProfileActivity.class);
        }
        else if(id == R.id.nav_wish_list){
            item.setChecked(true);
            redirectActivity(MainActivity.this, WishList.class);
        }
        closeDrawer(drawerLayout);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
    public static void redirectActivity(Activity currActivity, Class secondActivity){
        Intent intent = new Intent(currActivity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        currActivity.startActivity(intent);
        currActivity.finish();
    }

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("textSearch", query);
        startActivity(intent);
        finish();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}