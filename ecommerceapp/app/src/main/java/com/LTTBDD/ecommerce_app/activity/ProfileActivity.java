package com.LTTBDD.ecommerce_app.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.LTTBDD.ecommerce_app.R;
import com.LTTBDD.ecommerce_app.api.ApiService;
import com.LTTBDD.ecommerce_app.common.config.ConnectToMysql;
import com.LTTBDD.ecommerce_app.common.config.dto.ServiceResponse;
import com.LTTBDD.ecommerce_app.common.config.storage.DataLocalManager;
import com.LTTBDD.ecommerce_app.common.utils.DateUtils;
import com.LTTBDD.ecommerce_app.database.AuthDatabase;
import com.LTTBDD.ecommerce_app.database.CartDatabase;
import com.LTTBDD.ecommerce_app.dto.DistrictByProvince;
import com.LTTBDD.ecommerce_app.dto.WardByDistrict;
import com.LTTBDD.ecommerce_app.model.Cart;
import com.LTTBDD.ecommerce_app.model.ItemProduct;
import com.LTTBDD.ecommerce_app.model.Location.District;
import com.LTTBDD.ecommerce_app.model.Location.Province;
import com.LTTBDD.ecommerce_app.model.Location.Ward;
import com.LTTBDD.ecommerce_app.model.User;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Spinner spCountry, spProvince, spDistrict, spWard, spCountryId, spProvinceId, spWardId, spDistrictId;
    Toolbar toolbarProfile;
    NavigationView navigationViewProfile;
    DrawerLayout drawerLayoutProfile;
    FrameLayout framelayoutFavorProfile, framelayoutCartProfile;
    CircularImageView img;
    EditText txtaddress, txtemail, txtphone, txtusername, txtbirthday, profileNameEdit;
    ImageView editName, editAddress, editEmail, editPhone, editBirthday;

    TextView btnUpdateInfo, btnChangePassword, txtname;
    NotificationBadge badge;
    User currUser;
    Cart cart;
    Connection conn;
    CartDatabase cartDatabase;
    AuthDatabase authDatabase;
    public static final String GHN_TOKEN = "2f5f932a-faaa-11ed-a281-3aa62a37e0a5";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.LTTBDD.ecommerce_app.R.layout.activity_profile);
        initView();
        initData();
        initControl();
        actionBar();
    }
    private void initView(){

        editName = findViewById(R.id.editNameBtn);
        editAddress = findViewById(R.id.editAddressBtn);
        editEmail = findViewById(R.id.editEmailBtn);
        editPhone = findViewById(R.id.editPhoneBtn);
        editBirthday = findViewById(R.id.editBirthdayBtn);

        profileNameEdit = findViewById(R.id.profileNameEdit);
        txtusername = findViewById(R.id.profileUsername);
        txtbirthday = findViewById(R.id.profileBirthday);
        btnUpdateInfo = findViewById(R.id.btnUpdateInfo);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        img = findViewById(R.id.profileImg);
        txtname = findViewById(R.id.profileName);
        txtaddress = findViewById(R.id.profileAddress);
        txtphone = findViewById(R.id.profilePhoneNumber);
        txtemail = findViewById(R.id.profileEmail);

        txtusername.setEnabled(false);
        txtaddress.setEnabled(false);
        profileNameEdit.setEnabled(false);
        txtbirthday.setEnabled(false);
        txtemail.setEnabled(false);
        txtphone.setEnabled(false);
        btnUpdateInfo.setEnabled(false);

        toolbarProfile = findViewById(R.id.toolbarProfile);
        navigationViewProfile = findViewById(R.id.navProfile);
        drawerLayoutProfile = findViewById(R.id.drawerLayoutProfile);
        framelayoutFavorProfile = findViewById(R.id.framelayoutFavorProfile);
        framelayoutCartProfile = findViewById(R.id.framelayoutCartProfile);
        badge = findViewById(R.id.badge);
        navigationViewProfile.setNavigationItemSelectedListener(this);
        currUser = DataLocalManager.getCurrUser();


        View hView = navigationViewProfile.inflateHeaderView(R.layout.layout_header_nav);
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
        authDatabase = new AuthDatabase(conn);
        List<ItemProduct> items = cartDatabase.getListItemByCart(cart.getId());
        badge.setText(Integer.toString(items.size()));

        navigationViewProfile.getMenu().findItem(R.id.nav_myprofile).setChecked(true);
    }

    private void getListProvice(Dialog dialog){
        ApiService.apiService.getProvinces(GHN_TOKEN).enqueue(new Callback<ServiceResponse<List<Province>>>() {
            @Override
            public void onResponse(Call<ServiceResponse<List<Province>>> call, Response<ServiceResponse<List<Province>>> response) {
                if(response.body().getCode() == 200 && response != null){
                    List<String> dataArr = new ArrayList<>();
                    List<String> dataIdArr = new ArrayList<>();
                    for(Province i : response.body().getData()){
                        dataArr.add(i.getNameExtension().get(0));
                        dataIdArr.add(Integer.toString(i.getProvinceId()));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(dialog.getContext(),
                            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dataArr);
                    ArrayAdapter adapter1 = new ArrayAdapter(dialog.getContext(),
                            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dataIdArr);
                    spProvince.setAdapter(adapter);
                    spProvinceId.setAdapter(adapter1);
                    spProvince.setSelection(0);
                    spProvinceId.setSelection(0);
                }
                else{
                    Toast.makeText(ProfileActivity.this, "Danh sách Tỉnh/Thành trống", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse<List<Province>>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi trong quá trình tải Tỉnh/Thành", Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private void getListDistrict(Dialog dialog){
        String provinceId = spProvinceId.getSelectedItem().toString();
        DistrictByProvince request = new DistrictByProvince(Integer.parseInt(provinceId));
        ApiService.apiService.getDistricts(GHN_TOKEN, request).enqueue(new Callback<ServiceResponse<List<District>>>() {
            @Override
            public void onResponse(Call<ServiceResponse<List<District>>> call, Response<ServiceResponse<List<District>>> response) {
                ServiceResponse<List<District>> data = response.body();
                if(data != null && data.getCode() == 200){
                    List<String> dataArr = new ArrayList<>();
                    List<String> dataIdArr = new ArrayList<>();
                    for(District i : response.body().getData()){
                        dataArr.add(i.getDistrictName());
                        dataIdArr.add(Integer.toString(i.getDistrictId()));
                    }
                    ArrayAdapter adapter = new ArrayAdapter(dialog.getContext(),
                            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dataArr);
                    ArrayAdapter adapter1 = new ArrayAdapter(dialog.getContext(),
                            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dataIdArr);
                    spDistrict.setAdapter(adapter);
                    spDistrictId.setAdapter(adapter1);
                    spDistrict.setSelection(0);
                    spDistrictId.setSelection(0);
                }
                else{
                    Toast.makeText(ProfileActivity.this, "Danh sách Quận/Huyện trống", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse<List<District>>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi trong quá trình tải Quận/Huyện", Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private void getListWard(Dialog dialog){
        String districtId = spDistrictId.getSelectedItem().toString();
        WardByDistrict request = new WardByDistrict(Integer.parseInt(districtId));

        ApiService.apiService.getWards(GHN_TOKEN, request).enqueue(new Callback<ServiceResponse<List<Ward>>>() {
            @Override
            public void onResponse(Call<ServiceResponse<List<Ward>>> call, Response<ServiceResponse<List<Ward>>> response) {
                ServiceResponse<List<Ward>> data = response.body();
                if (data != null && data.getCode() == 200) {
                    List<String> dataArr = new ArrayList<>();
                    List<String> dataIdArr = new ArrayList<>();
                    for (Ward i : data.getData()) {
                        dataArr.add(i.getWardName());
                        dataIdArr.add(i.getWardCode());
                    }
                    ArrayAdapter adapter = new ArrayAdapter(dialog.getContext(),
                            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dataArr);
                    ArrayAdapter adapter1 = new ArrayAdapter(dialog.getContext(),
                            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dataIdArr);
                    spWard.setAdapter(adapter);
                    spWardId.setAdapter(adapter1);
                    spWard.setSelection(0);
                    spWardId.setSelection(0);
                } else {
                    Toast.makeText(ProfileActivity.this, "Danh sách Phường/Xã trống", Toast.LENGTH_SHORT).show();
                }
            };

            @Override
            public void onFailure(Call<ServiceResponse<List<Ward>>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi trong quá trình tải Phường/Xã", Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private void initControl(){
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileNameEdit.setEnabled(true);
                profileNameEdit.setText("");
                profileNameEdit.requestFocus();
            }
        });

        profileNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnUpdateInfo.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditAddressDialog();
            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtemail.setEnabled(true);
                txtemail.setText("");
                txtemail.requestFocus();
            }
        });
        txtemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnUpdateInfo.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtphone.setEnabled(true);
                txtphone.setText("");
                txtphone.requestFocus();
            }
        });
        txtphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnUpdateInfo.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtbirthday.setEnabled(true);
                txtbirthday.setText("");
                txtbirthday.requestFocus();
            }
        });
        txtbirthday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnUpdateInfo.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        framelayoutCartProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });

        framelayoutFavorProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ProfileActivity.this)
                        .galleryOnly()
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(10);
            }
        });

        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User infoChanged = new User();
                infoChanged.setName(profileNameEdit.getText().toString());

                String email = txtemail.getText().toString();

                String regexEmail = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:" +
                        "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
//                if(!email.matches(regexEmail)){
//                    Toast.makeText(ProfileActivity.this, "Sai định dạng email, hãy nhập lại!", Toast.LENGTH_SHORT).show();
//                    txtemail.requestFocus();
//                    return;
//                }

                infoChanged.setEmail(email);
                infoChanged.setPhoneNumber(txtphone.getText().toString());
                String birthDayStr = txtbirthday.getText().toString();
                Date birthDay = DateUtils.convertStringToDate(birthDayStr);
                if(birthDay == null){
                    Toast.makeText(ProfileActivity.this, "Sai định dạng ngày (yyyy-MM-dd), hãy nhập lại!", Toast.LENGTH_SHORT).show();
                    txtbirthday.requestFocus();
                    return;
                }
                infoChanged.setBirthday(birthDay);

                Boolean isUpdatedInfo =  authDatabase.updateInfo(infoChanged, currUser.getId());
                if(isUpdatedInfo){
                    DataLocalManager.logout();
                    currUser.setName(profileNameEdit.getText().toString());
                    currUser.setEmail(txtemail.getText().toString());
                    currUser.setPhoneNumber(txtphone.getText().toString());
                    currUser.setBirthday(DateUtils.convertStringToDate(txtbirthday.getText().toString()));

                    DataLocalManager.setCurrUser(currUser);
                    restartActivity(ProfileActivity.this);
                    Toast.makeText(ProfileActivity.this, "Đã cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ProfileActivity.this, "Lỗi trong quá trình cập nhật thông tin", Toast.LENGTH_SHORT).show();
                }



            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangePasswordDialog();
            }
        });
    }

    private void showEditAddressDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_address);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        spCountryId = dialog.findViewById(R.id.tvCountryId);
        spProvinceId = dialog.findViewById(R.id.tvProvinceId);
        spDistrictId = dialog.findViewById(R.id.tvDistrictId);
        spWardId = dialog.findViewById(R.id.tvWardId);
        spCountryId.setEnabled(false);
        spProvinceId.setEnabled(false);
        spDistrictId.setEnabled(false);
        spWardId.setEnabled(false);

        spCountry = dialog.findViewById(R.id.spCountry);
        spProvince = dialog.findViewById(R.id.spProvince);
        spDistrict = dialog.findViewById(R.id.spDistrict);
        spWard = dialog.findViewById(R.id.spWard);
        EditText detailAddress = dialog.findViewById(R.id.detailAddress);

        List<String> countryStr = new ArrayList<>();
        List<String> countryIdStr = new ArrayList<>();
        countryStr.add("Việt Nam");
        countryIdStr.add("1");
        ArrayAdapter countryAdapter = new ArrayAdapter(dialog.getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, countryStr);
        ArrayAdapter countryIdAdapter = new ArrayAdapter(dialog.getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, countryIdStr);
        spCountry.setAdapter(countryAdapter);
        spCountryId.setAdapter(countryIdAdapter);


        getListProvice(dialog);
        dialog.show();

        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spProvinceId.setSelection(i);
//                Toast.makeText(ProfileActivity.this, provinceId, Toast.LENGTH_SHORT).show();
                getListDistrict(dialog);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spDistrictId.setSelection(i);
                String districtId = spDistrictId.getSelectedItem().toString();
//                Toast.makeText(ProfileActivity.this, provinceId, Toast.LENGTH_SHORT).show();
                getListWard(dialog);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                String province = spProvince.getSelectedItem().toString();
                String district = spDistrict.getSelectedItem().toString();
                String ward = spWard.getSelectedItem().toString();

                String provinceId = spProvinceId.getSelectedItem().toString();
                String districtId = spDistrictId.getSelectedItem().toString();
                String wardId = spWardId.getSelectedItem().toString();

                String detailAddressStr = detailAddress.getText().toString() + ", " + ward + ", " +
                        district + ", " + province;
                User infoUpdated = new User();
                infoUpdated.setAddress(detailAddressStr);
                infoUpdated.setProvinceId(Integer.parseInt(provinceId));
                infoUpdated.setDistrictId(Integer.parseInt(districtId));
                infoUpdated.setWardId(wardId);


                Boolean isUpdatedAddress = authDatabase.updateAddress(infoUpdated, currUser.getId());
                if(isUpdatedAddress){
                    Toast.makeText(getApplicationContext(), "Cập nhật địa chỉ giao hàng thành công", Toast.LENGTH_SHORT).show();
                    DataLocalManager.logout();
                    currUser.setAddress(detailAddressStr);
                    currUser.setProvinceId(Integer.parseInt(provinceId));
                    currUser.setDistrictId(Integer.parseInt(districtId));
                    currUser.setWardId(wardId);

                    DataLocalManager.setCurrUser(currUser);
                    dialog.dismiss();
                    txtaddress.setText(detailAddressStr);
                    restartActivity(ProfileActivity.this);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Lỗi trong quá trình cập nhật đơn hàng", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

    }

    private void showChangePasswordDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_password);

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

        TextView cancle = dialog.findViewById(R.id.btnCancle);
        TextView accept = dialog.findViewById(R.id.btnUpdatePassword);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "Test hủy", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText currPassword = dialog.findViewById(R.id.currentPassword);
                EditText newPassword = dialog.findViewById(R.id.newPassword);

                String currPasswordTxt = currPassword.getText().toString();
                String newPasswordTxt = newPassword.getText().toString();

                if(TextUtils.isEmpty(currPasswordTxt)){
                    Toast.makeText(ProfileActivity.this, "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(newPasswordTxt)){
                    Toast.makeText(ProfileActivity.this, "Mật khẩu mới không được để trống", Toast.LENGTH_SHORT).show();
                    return;
                }

                String regexPassword = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
                if(!newPasswordTxt.matches(regexPassword)){
                    Toast.makeText(ProfileActivity.this, "Mật khẩu mới không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                Boolean isCorrectPassword = authDatabase.checkCorrectPassword(currPasswordTxt, currUser.getId());
                if(isCorrectPassword != null && isCorrectPassword != false){
                    dialog.dismiss();
                    Boolean isUpdatedPassword = authDatabase.updatePassword(newPasswordTxt, currUser.getId());
                    if(isUpdatedPassword == null || !isUpdatedPassword){
                        Toast.makeText(ProfileActivity.this, "Lỗi trong quá trình đổi mật khẩu", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        currUser.setPassword(newPasswordTxt);
                        DataLocalManager.logout();
                        DataLocalManager.setCurrUser(currUser);
                        Toast.makeText(ProfileActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        restartActivity(ProfileActivity.this);
                    }
                }
                else{
                    Toast.makeText(ProfileActivity.this, "Mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

    private void actionBar(){
        setSupportActionBar(toolbarProfile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thông tin cá nhân");
        toolbarProfile.setTitleTextColor(getResources().getColor(R.color.white));
        toolbarProfile.setNavigationIcon(R.drawable.baseline_menu_24);
        toolbarProfile.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayoutProfile.openDrawer(GravityCompat.START);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayoutProfile, toolbarProfile,
                R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayoutProfile.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            Uri uri = data.getData();
            Glide.with(getApplicationContext()).load(uri).into(img);
            authDatabase.updateAvatar(uri.toString(), currUser.getId());
            currUser.setImg(uri.toString());
            DataLocalManager.logout();
            DataLocalManager.setCurrUser(currUser);
            recreate();
        }
        else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData(){
        if(currUser.getImg() == null || currUser.getImg().equals("")){
            img.setImageResource(R.drawable.user_icon_2);
        }
        else{
            Glide.with(getApplicationContext()).load(currUser.getImg()).into(img);
        }
        profileNameEdit.setText(currUser.getName());
        txtname.setText(currUser.getName());
        txtaddress.setText(currUser.getAddress());
        txtemail.setText(currUser.getEmail());
        txtphone.setText(currUser.getPhoneNumber());
        txtusername.setText(currUser.getUsername());
        if(currUser.getBirthday() == null){
            txtbirthday.setText("");
        }
        else{
            txtbirthday.setText(DateUtils.dateToStringStartWithDDMMYYY(currUser.getBirthday()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<ItemProduct> items = cartDatabase.getListItemByCart(cart.getId());
        badge.setText(Integer.toString(items.size()));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_home){
            item.setChecked(true);
            redirectActivity(ProfileActivity.this, MainActivity.class);
        }else if(id == R.id.nav_mobile){
            item.setChecked(true);
            redirectActivity(ProfileActivity.this, MobileActivity.class);
        }
        else if(id == R.id.nav_laptop){
            item.setChecked(true);
            redirectActivity(ProfileActivity.this, LaptopActivity.class);
        }
        else if(id == R.id.nav_history){
            item.setChecked(true);
            redirectActivity(ProfileActivity.this, HistoryActivity.class);
        }
        else if(id == R.id.nav_signout){
            item.setChecked(true);
            DataLocalManager.logout();
            Intent logout = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(logout);
        }
        else if(id == R.id.nav_search){
            item.setChecked(true);
            redirectActivity(ProfileActivity.this, SearchActivity.class);
        }
        else if(id == R.id.nav_contact_us){
            item.setChecked(true);
            redirectActivity(ProfileActivity.this, ContactInforActivity.class);
        }
        else if(id == R.id.nav_about_us){
            item.setChecked(true);
            redirectActivity(ProfileActivity.this, AboutUsActivity.class);
        }
        else if(id == R.id.nav_myprofile){
            item.setChecked(true);
            restartActivity(ProfileActivity.this);
        }
        else if(id == R.id.nav_wish_list){
            item.setChecked(true);
            redirectActivity(ProfileActivity.this, WishList.class);
        }
        closeDrawer(drawerLayoutProfile);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayoutProfile.isDrawerOpen(GravityCompat.START)){
            drawerLayoutProfile.closeDrawer(GravityCompat.START);
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
        closeDrawer(drawerLayoutProfile);
    }

    public static void restartActivity(Activity activity) {
        if (Build.VERSION.SDK_INT >= 11) {
            activity.recreate();
        } else {
            activity.finish();
            activity.startActivity(activity.getIntent());
        }
    }

}