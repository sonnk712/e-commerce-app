package com.LTTBDD.ecommerce_app.database;

import android.os.StrictMode;

import com.LTTBDD.ecommerce_app.common.config.constants.MessageCode;
import com.LTTBDD.ecommerce_app.common.config.dto.ServiceResponse;
import com.LTTBDD.ecommerce_app.model.NewProduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductData {
    private Connection conn;

    public ProductData(Connection conn) {
        this.conn = conn;
    }


    public ServiceResponse<List<NewProduct>> getAllByTextSearch(String textSearch) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String query = "SELECT * from new_product where name like concat('%', ?, '%') ORDER BY id";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, textSearch);
            ResultSet rs = stmt.executeQuery();
            List<NewProduct> response = new ArrayList<>();
            while(rs.next()) {
                NewProduct data = new NewProduct();
                data.setId(Integer.parseInt(rs.getString(1)));
                data.setName(rs.getString(2));
                data.setPrice(Double.parseDouble(rs.getString(3)));
                data.setImage(rs.getString(4));
                data.setDescription(rs.getString(5));
                data.setCategoryId(rs.getInt(6));
                data.setBriefDescription(rs.getString(7));
                data.setBrandId(rs.getInt(8));
                data.setBrandName(rs.getString(9));
                response.add(data);
            }
            if(response.size() == 0){
                return new ServiceResponse<>(MessageCode.PRODUCT_IS_EMPTY, MessageCode.PRODUCT_IS_EMPTY_MESSAGE
                        , null);
            }

            return new ServiceResponse<>(MessageCode.SUCCESS, MessageCode.SUCCESS_MESSAGE, response);
        }
        catch (SQLException e){
            System.out.println(e);
            return new ServiceResponse<>(MessageCode.FAILED, MessageCode.FAILED_MESSAGE
                    , null);
        }
    }

    public ServiceResponse<List<NewProduct>> getListByTextSearch(String textSearch, int page, int total) {
        int pos = (page - 1) * total;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String query = "SELECT * from new_product where name like concat('%', ?, '%') ORDER BY id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, textSearch);
            stmt.setInt(2, pos);
            stmt.setInt(3, total);

            ResultSet rs = stmt.executeQuery();

            List<NewProduct> response = new ArrayList<>();
            while(rs.next()) {
                NewProduct data = new NewProduct();
                data.setId(Integer.parseInt(rs.getString(1)));
                data.setName(rs.getString(2));
                data.setPrice(Double.parseDouble(rs.getString(3)));
                data.setImage(rs.getString(4));
                data.setDescription(rs.getString(5));
                data.setCategoryId(rs.getInt(6));
                data.setBriefDescription(rs.getString(7));
                data.setBrandId(rs.getInt(8));
                data.setBrandName(rs.getString(9));
                response.add(data);
            }
            if(response.size() == 0){
                return new ServiceResponse<>(MessageCode.PRODUCT_IS_EMPTY, MessageCode.PRODUCT_IS_EMPTY_MESSAGE
                        , null);
            }

            return new ServiceResponse<>(MessageCode.SUCCESS, MessageCode.SUCCESS_MESSAGE, response);
        }
        catch (SQLException e){
            System.out.println(e);
            return new ServiceResponse<>(MessageCode.FAILED, MessageCode.FAILED_MESSAGE
                    , null);
        }
    }

    public ServiceResponse<List<NewProduct>> getList(int categoryId, int page, int total) {
        int pos = (page - 1) * total;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String query = "SELECT * from new_product where category_id = ? ORDER BY id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, categoryId);
            stmt.setInt(2, pos);
            stmt.setInt(3, total);

            ResultSet rs = stmt.executeQuery();

            List<NewProduct> response = new ArrayList<>();
            while(rs.next()) {
                NewProduct data = new NewProduct();
                data.setId(Integer.parseInt(rs.getString(1)));
                data.setName(rs.getString(2));
                data.setPrice(Double.parseDouble(rs.getString(3)));
                data.setImage(rs.getString(4));
                data.setDescription(rs.getString(5));
                data.setCategoryId(rs.getInt(6));
                data.setBriefDescription(rs.getString(7));
                data.setBrandId(rs.getInt(8));
                data.setBrandName(rs.getString(9));
                response.add(data);
            }
            if(response.size() == 0){
                return new ServiceResponse<>(MessageCode.PRODUCT_IS_EMPTY, MessageCode.PRODUCT_IS_EMPTY_MESSAGE
                        , null);
            }

            return new ServiceResponse<>(MessageCode.SUCCESS, MessageCode.SUCCESS_MESSAGE, response);
        }
        catch (SQLException e){
            System.out.println(e);
            return new ServiceResponse<>(MessageCode.FAILED, MessageCode.FAILED_MESSAGE
                    , null);
        }
    }

    public ServiceResponse<List<NewProduct>> getListByBrand(int brandId) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String query = "SELECT * from new_product where brand_id = ? ORDER BY id";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, brandId);
            ResultSet rs = stmt.executeQuery();

            List<NewProduct> response = new ArrayList<>();
            while(rs.next()) {
                NewProduct data = new NewProduct();
                data.setId(Integer.parseInt(rs.getString(1)));
                data.setName(rs.getString(2));
                data.setPrice(Double.parseDouble(rs.getString(3)));
                data.setImage(rs.getString(4));
                data.setDescription(rs.getString(5));
                data.setCategoryId(rs.getInt(6));
                data.setBriefDescription(rs.getString(7));
                data.setBrandId(rs.getInt(8));
                data.setBrandName(rs.getString(9));
                response.add(data);
            }
            if(response.size() == 0){
                return new ServiceResponse<>(MessageCode.PRODUCT_IS_EMPTY, MessageCode.PRODUCT_IS_EMPTY_MESSAGE
                        , null);
            }

            return new ServiceResponse<>(MessageCode.SUCCESS, MessageCode.SUCCESS_MESSAGE, response);
        }
        catch (SQLException e){
            System.out.println(e);
            return new ServiceResponse<>(MessageCode.FAILED, MessageCode.FAILED_MESSAGE
                    , null);
        }
    }

    public NewProduct getProductById(int id){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "SELECT * from new_product where id = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            NewProduct data = new NewProduct();
            while(rs.next()) {
                data.setId(Integer.parseInt(rs.getString(1)));
                data.setName(rs.getString(2));
                data.setPrice(Double.parseDouble(rs.getString(3)));
                data.setImage(rs.getString(4));
                data.setDescription(rs.getString(5));
                data.setCategoryId(rs.getInt(6));
                data.setBriefDescription(rs.getString(7));
                data.setBrandId(rs.getInt(8));
                data.setBrandName(rs.getString(9));
            }
            return data;

        }catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }

}
