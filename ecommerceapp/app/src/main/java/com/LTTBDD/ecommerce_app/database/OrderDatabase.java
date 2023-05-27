package com.LTTBDD.ecommerce_app.database;

import android.os.StrictMode;

import com.LTTBDD.ecommerce_app.dto.HistoryInfo;
import com.LTTBDD.ecommerce_app.dto.RegisterInfo;
import com.LTTBDD.ecommerce_app.model.Order;
import com.LTTBDD.ecommerce_app.model.OrderDetail;
import com.LTTBDD.ecommerce_app.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDatabase {
    private Connection conn;

    public OrderDatabase(Connection conn) {
        this.conn = conn;
    }

    public Order saveOrder(Order order){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "INSERT INTO orders(delivery_address, note, createdDate, status, user_id, total_price, quantity) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            java.util.Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            stmt.setString(1, order.getDeliveryAddress());
            stmt.setString(2, order.getNote());
            stmt.setDate(3, sqlDate);
            stmt.setInt(4, order.getStatus());
            stmt.setInt(5, order.getUserId());
            stmt.setDouble(6, order.getTotalPrice());
            stmt.setInt(7, order.getQuantity());

            int rowsAffected = stmt.executeUpdate();
            Order result = new Order();
            if(rowsAffected > 0){
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        order.setId(id);
                    }
                }
               return order;
            }
            return null;
        }
        catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }

    public List<HistoryInfo> findOrderDetailByOrder(int id){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "SELECT od.id, od.price, od.quantity, np.name, np.brief_description, np.image " +
                    "FROM order_detail od inner join new_product np on od.product_id = np.id " +
                    "WHERE od.order_id = ? order by od.id";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            List<HistoryInfo> response = new ArrayList<>();
            while (rs.next()){
                HistoryInfo data = new HistoryInfo();
                data.setId(rs.getInt(1));
                data.setPrice(rs.getDouble(2));
                data.setQuantity(rs.getInt(3));
                data.setName(rs.getString(4));
                data.setDescription(rs.getString(5));
                data.setImage(rs.getString(6));

                response.add(data);
            }
            return response;
        }
        catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }

    public List<Order> findOrderByUser(int userId){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "SELECT * FROM orders WHERE user_id = ? order by createdDate desc";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            List<Order> response = new ArrayList<>();
            while (rs.next()){
                Order data = new Order();
                data.setId(rs.getInt(1));
                data.setDeliveryAddress(rs.getString(2));
                data.setNote(rs.getString(3));
                data.setCreatedDate(rs.getDate(4));
                data.setStatus(rs.getInt(5));
                data.setUserId(rs.getInt(6));
                data.setTotalPrice(rs.getDouble(7));
                data.setQuantity(rs.getInt(8));

                response.add(data);
            }
            return response;
        }
        catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }

    public OrderDetail saveOrderDetail(OrderDetail orderDetail){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "INSERT INTO order_detail(price, quantity, product_id, order_id) VALUES(?, ?, ?, ?)";
            PreparedStatement stmt = this.conn.prepareStatement(query);

            java.util.Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            stmt.setDouble(1, orderDetail.getPrice());
            stmt.setInt(2, orderDetail.getQuantity());
            stmt.setInt(3, orderDetail.getProductId());
            stmt.setInt(4, orderDetail.getOrderId());

            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected > 0){
                return orderDetail;
            }
            return null;
        }
        catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }

}
