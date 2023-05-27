package com.LTTBDD.ecommerce_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.LTTBDD.ecommerce_app.R;
import com.LTTBDD.ecommerce_app.adapter.MobileAdapter;
import com.LTTBDD.ecommerce_app.adapter.SearchAdapter;
import com.LTTBDD.ecommerce_app.common.config.ConnectToMysql;
import com.LTTBDD.ecommerce_app.common.config.constants.Constants;
import com.LTTBDD.ecommerce_app.common.config.constants.MessageCode;
import com.LTTBDD.ecommerce_app.common.config.dto.ServiceResponse;
import com.LTTBDD.ecommerce_app.common.config.storage.DataLocalManager;
import com.LTTBDD.ecommerce_app.common.service.ItemClickListener;
import com.LTTBDD.ecommerce_app.database.ProductData;
import com.LTTBDD.ecommerce_app.model.NewProduct;
import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements ItemClickListener {
    RecyclerView recycleViewSearch, recycleViewCategory;
    Toolbar toolbarSearch;
    LinearLayoutManager layoutManager;
    List<NewProduct> productDataList;
    NavigationView navMobile;
    MobileAdapter adapter;
    int page = 1;
    String textSearch;
    Connection conn;
    ProductData productData;
    int currBrandApperance = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.LTTBDD.ecommerce_app.R.layout.activity_search);
        initView();
        actionToolBar();
//        filterByBrand(currBrandApperance);
//        if(productDataList.size() >= 4){
//            addEventLoad();
//        }
    }

    private List<String> getList(){
        String[] listArr = getResources().getStringArray(R.array.category_item);
        List<String> list = new ArrayList<>(Arrays.asList(listArr));
        return list;
    }

    private void initView() {
        toolbarSearch = findViewById(R.id.toolbarSearch);
        recycleViewSearch = findViewById(R.id.recycleViewSearch);
        recycleViewCategory = findViewById(R.id.recycleViewCategory);
        navMobile = findViewById(R.id.navMobile);

        LinearLayoutManager layoutManagerCategory = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        SearchAdapter adapterCategory = new SearchAdapter(getList(), getApplicationContext());
        recycleViewCategory.setLayoutManager(layoutManagerCategory);
        recycleViewCategory.setHasFixedSize(true);

        recycleViewCategory.setAdapter(adapterCategory);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycleViewSearch.setLayoutManager(layoutManager);
        recycleViewSearch.setHasFixedSize(true);
        productDataList = new ArrayList<>();

        adapterCategory.setItemClickListener(this);
        conn = ConnectToMysql.connect();
        productData = new ProductData(conn);

        Intent intent = getIntent();
        textSearch = intent.getStringExtra("textSearch");
        if(textSearch == null){
            textSearch = "";
        }

        ServiceResponse<List<NewProduct>> list = new ServiceResponse<>();
        list = productData.getAllByTextSearch(textSearch);
        if(list != null){
            productDataList = list.getData();
            adapter = new MobileAdapter(productDataList, getApplicationContext());
            recycleViewSearch.setAdapter(adapter);
        }
    }

    private void actionToolBar() {
        setSupportActionBar(toolbarSearch);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarSearch.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setTitle("Tìm kiếm sản phẩm");
        toolbarSearch.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(SearchActivity.this, MainActivity.class);
            }
        });
    }

    public static void redirectActivity(Activity currActivity, Class secondActivity) {
        Intent intent = new Intent(currActivity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        currActivity.startActivity(intent);
        currActivity.finish();
    }


    public void filterByBrand(int brandId){
        try {
            ServiceResponse<List<NewProduct>> result = productData.getListByBrand(brandId);
            if (result.getCode() == MessageCode.SUCCESS) {
                productDataList = result.getData();
                adapter = new MobileAdapter(productDataList, getApplicationContext());
                recycleViewSearch.setAdapter(adapter);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClick(View view, int index, boolean isLongClick) {
        if(!isLongClick){
            switch (index){
                case 0: filterByBrand(1); currBrandApperance = 1; break;
                case 1: filterByBrand(2); currBrandApperance = 2; break;
                case 2: filterByBrand(3); currBrandApperance = 3; break;
                case 3: filterByBrand(4); currBrandApperance = 4; break;
                case 4: filterByBrand(5); currBrandApperance = 5; break;
                case 5: filterByBrand(6); currBrandApperance = 6; break;
                case 6: filterByBrand(7); currBrandApperance = 7; break;
                case 7: filterByBrand(8); currBrandApperance = 8; break;
            }
        }
    }
}