package com.LTTBDD.ecommerce_app.database;

import android.os.AsyncTask;
import android.os.StrictMode;

import com.LTTBDD.ecommerce_app.common.config.ConnectToMysql;
import com.LTTBDD.ecommerce_app.common.config.constants.Constants;
import com.LTTBDD.ecommerce_app.common.config.constants.MessageCode;
import com.LTTBDD.ecommerce_app.common.config.dto.ServiceResponse;
import com.LTTBDD.ecommerce_app.model.Category;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryData{
    private Connection conn;

    public CategoryData(Connection conn) {
        this.conn = conn;
    }

    public ServiceResponse<List<Category>> getList() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            PreparedStatement stmt = this.conn.prepareStatement("Select * from category");
            ResultSet rs = stmt.executeQuery();
            List<Category> response = new ArrayList<>();
            while(rs.next()) {
                Category data = new Category();
                data.setId(Integer.parseInt(rs.getString(1)));
                data.setName(rs.getString(2));
                data.setImage(rs.getString(3));
                response.add(data);
            }
            if(response.size() == 0){
                return new ServiceResponse<>(MessageCode.CATEGORY_IS_EMPTY, MessageCode.CATEGORY_IS_EMPTY_MESSAGE
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
}
