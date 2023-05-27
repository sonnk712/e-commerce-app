package com.LTTBDD.ecommerce_app.database;

import android.os.StrictMode;

import com.LTTBDD.ecommerce_app.activity.WishList;
import com.LTTBDD.ecommerce_app.model.Cart;
import com.LTTBDD.ecommerce_app.model.ItemProduct;
import com.LTTBDD.ecommerce_app.model.WishListModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WishListDatabase {
    private Connection conn;

    public WishListDatabase(Connection conn) {
        this.conn = conn;
    }

    public List<WishListModel> getWishListFromUser(int userId) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "Select * from wish_lish where user_id = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            List<WishListModel> response = new ArrayList<>();
            while(rs.next()) {
                WishListModel data = new WishListModel();
                data.setId(rs.getInt(1));
                data.setName(rs.getString(2));
                data.setPrice(rs.getDouble(3));
                data.setDescription(rs.getString(4));
                data.setImg(rs.getString(5));
                data.setUserId(rs.getInt(6));

                response.add(data);
            }
            return response;
        }
        catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }

    public Boolean create(WishListModel item){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "INSERT INTO wish_lish(name, price, description, img, user_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, item.getName());
            stmt.setDouble(2, item.getPrice());
            stmt.setString(3, item.getDescription());
            stmt.setString(4, item.getImg());
            stmt.setInt(5, item.getUserId());

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                return true;
            }
            return false;
        }
        catch (SQLException e){
            System.out.println(e);
            return false;
        }
    }


    public Boolean checkWishlistExist(int wishListId, int userId) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "Select * from wish_lish where id = ? and user_id = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, wishListId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();

            int cnt = 0;
            while(rs.next()) {
                cnt++;
                if(cnt > 0){
                    return true;
                }
            }
            return false;
        }
        catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }
}
