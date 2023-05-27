package com.LTTBDD.ecommerce_app.api;

import com.LTTBDD.ecommerce_app.common.config.dto.ServiceResponse;
import com.LTTBDD.ecommerce_app.dto.DistrictByProvince;
import com.LTTBDD.ecommerce_app.dto.ServiceResponse2;
import com.LTTBDD.ecommerce_app.dto.WardByDistrict;
import com.LTTBDD.ecommerce_app.model.Location.District;
import com.LTTBDD.ecommerce_app.model.Location.Province;
import com.LTTBDD.ecommerce_app.model.Location.Ward;
import com.LTTBDD.ecommerce_app.model.Shipment.CostRequest;
import com.LTTBDD.ecommerce_app.model.Shipment.CostResponse;
import com.LTTBDD.ecommerce_app.model.Shipment.ShippingService;
import com.LTTBDD.ecommerce_app.model.Shipment.ShippingServiceRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    public static final String GHN_URL = "https://online-gateway.ghn.vn/";
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl(GHN_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);
    @GET("shiip/public-api/master-data/province")
    Call<ServiceResponse<List<Province>>> getProvinces(@Header("token") String token);

    @POST("shiip/public-api/master-data/district")
    Call<ServiceResponse<List<District>>> getDistricts(@Header("token") String token,
                                                       @Body DistrictByProvince request);

    @POST("shiip/public-api/master-data/ward")
    Call<ServiceResponse<List<Ward>>> getWards(@Header("token") String token,
                                               @Body WardByDistrict request);

    @POST("shiip/public-api/v2/shipping-order/available-services")
    Call<ServiceResponse2<List<ShippingService>>> getShippingService(@Header("token") String token,
                                                                     @Body String request);
    @POST("shiip/public-api/v2/shipping-order/fee")
    Call<ServiceResponse<CostResponse>> getShippingCost(@Header("token") String token,
                                                        @Body CostRequest request);
}
