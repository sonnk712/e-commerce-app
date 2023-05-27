package com.LTTBDD.ecommerce_app.database;

import android.os.StrictMode;

import com.LTTBDD.ecommerce_app.common.config.constants.MessageCode;
import com.LTTBDD.ecommerce_app.common.config.dto.ServiceResponse;
import com.LTTBDD.ecommerce_app.model.Category;
import com.LTTBDD.ecommerce_app.model.NewProduct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewProductData {
    private Connection conn;

    public NewProductData(Connection conn) {
        this.conn = conn;
    }

    public ServiceResponse<List<NewProduct>> getList() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            PreparedStatement stmt = this.conn.prepareStatement("Select * from new_product order by id desc");
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

}
