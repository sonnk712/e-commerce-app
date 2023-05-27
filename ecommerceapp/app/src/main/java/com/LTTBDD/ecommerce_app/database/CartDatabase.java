package com.LTTBDD.ecommerce_app.database;

import android.os.StrictMode;

import com.LTTBDD.ecommerce_app.common.config.constants.MessageCode;
import com.LTTBDD.ecommerce_app.common.config.dto.ServiceResponse;
import com.LTTBDD.ecommerce_app.model.Cart;
import com.LTTBDD.ecommerce_app.model.Category;
import com.LTTBDD.ecommerce_app.model.ItemProduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartDatabase {
    private Connection conn;

    public CartDatabase(Connection conn) {
        this.conn = conn;
    }

    public Boolean updateCartInfo(Cart cart){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "Update cart set total_price = ?, total_quantity = ? where id = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setDouble(1, cart.getTotalPrice());
            stmt.setInt(2, cart.getTotalQuantity());
            stmt.setInt(3, cart.getId());
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected > 0){
                return true;
            }
            return false;
        }
        catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }

    public Boolean removeItem(int id){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "DELETE from product_item where id = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("A user was deleted successfully!");
            }
            return true;
        }
        catch (SQLException e){
            System.out.println(e);
            return false;
        }
    }


    public Boolean updateItemInfo(ItemProduct item){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "Update product_item set quantity = ?, total_price = ?, price = ? where id = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, item.getQuantity());
            stmt.setDouble(2, item.getTotalPrice());
            stmt.setDouble(3, item.getPrice());
            stmt.setInt(4, item.getId());
            stmt.executeUpdate();

            return true;
        }
        catch (SQLException e){
            System.out.println(e);
            return false;
        }
    }

    public Boolean create(ItemProduct item, int cartId){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "INSERT INTO product_item(quantity, total_price, price, cart_id, product_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, item.getQuantity());
            stmt.setDouble(2, item.getTotalPrice());
            stmt.setDouble(3, item.getPrice());
            stmt.setInt(4, cartId);
            stmt.setInt(5, item.getProductId());
            stmt.executeUpdate();

            return true;
        }
        catch (SQLException e){
            System.out.println(e);
            return false;
        }
    }


    public ItemProduct checkProductExisted(int productId, int cartId) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "Select * from product_item where product_id = ? and cart_id = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, productId);
            stmt.setInt(2, cartId);
            ResultSet rs = stmt.executeQuery();

            ItemProduct item = new ItemProduct();
            int count = 0;
            while(rs.next()) {
                item.setId(rs.getInt(1));
                item.setQuantity(rs.getInt(2));
                item.setTotalPrice(rs.getDouble(3));
                item.setCartId(rs.getInt(4));
                item.setProductId(rs.getInt(5));
                count += 1;
            }
            if(count <= 0){
                return null;
            }

            return item;
        }
        catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }


    public List<ItemProduct> getListItemByCart(int cartId) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "Select * from product_item where cart_id = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, cartId);
            ResultSet rs = stmt.executeQuery();

            List<ItemProduct> response = new ArrayList<>();

            while(rs.next()) {
                ItemProduct data = new ItemProduct();
                data.setId(rs.getInt(1));
                data.setQuantity(rs.getInt(2));
                data.setTotalPrice(rs.getDouble(3));
                data.setPrice(rs.getDouble(4));
                data.setCartId(rs.getInt(5));
                data.setProductId(rs.getInt(6));

                response.add(data);
            }

            return response;
        }
        catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }

    public Cart getCartFromUser(String username) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "Select c.* from users u inner join cart c on u.cart_id = c.id where u.username = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            Cart cart = new Cart();
            while(rs.next()) {
                cart.setId(Integer.parseInt(rs.getString(1)));
                cart.setCreatedDate(rs.getDate(2));
                cart.setTotalPrice(rs.getDouble(3));
                cart.setTotalQuantity(rs.getInt(4));
            }
            return cart;
        }
        catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }

    public Integer creatCart() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            java.util.Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            String query = "INSERT into cart(created_date, total_price, total_quantity) values(?, 0, 0)";
            PreparedStatement stmt = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setDate(1, sqlDate);
            int rowsAffected = stmt.executeUpdate();

            if(rowsAffected > 0){
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int cartId = generatedKeys.getInt(1);
                        return cartId;
                    }
                }
            }
            return 0;
        }
        catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }

    public Boolean removeAllItem(int cartId) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String query = "DELETE from product_item where cart_id = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, cartId);
            int rowsAffected = stmt.executeUpdate();

            if(rowsAffected > 0){
                return true;
            }
            return false;
        }
        catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }

    public Boolean emptyCart(int cartId) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "Update cart set total_price = 0, total_quantity = 0 where id = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, cartId);
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected > 0){
                return true;
            }
            return false;
        }
        catch (SQLException e){
            System.out.println(e);
            return false;
        }
    }

}
