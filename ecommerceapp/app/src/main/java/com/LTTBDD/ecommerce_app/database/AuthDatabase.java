package com.LTTBDD.ecommerce_app.database;

import android.os.StrictMode;

import com.LTTBDD.ecommerce_app.dto.RegisterInfo;
import com.LTTBDD.ecommerce_app.model.Cart;
import com.LTTBDD.ecommerce_app.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AuthDatabase {
    private Connection conn;

    public AuthDatabase(Connection conn) {
        this.conn = conn;
    }

    public Boolean checkCorrectPassword(String currPassword, int id){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "SELECT password FROM users where id = ? and password = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.setString(2, currPassword);
            ResultSet rs = stmt.executeQuery();
            String result = null;
            while (rs.next()){
                result = rs.getString(1);
                if(result != null){
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

    public Boolean updateAddress(User infoChanged, int id){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "Update users set address = ?, province_id = ?, district_id = ?, ward_id = ?" +
                    " where id = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);

            stmt.setString(1, infoChanged.getAddress());
            stmt.setInt(2, infoChanged.getProvinceId());
            stmt.setInt(3, infoChanged.getDistrictId());
            stmt.setString(4, infoChanged.getWardId());
            stmt.setInt(5, id);
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

    public Boolean updatePassword(String newPassword, int id){
        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "Update users set password = ? where id = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);

            stmt.setString(1, newPassword);
            stmt.setInt(2, id);

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

    public Boolean updateInfo(User infoChanged, int userId){
        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "Update users set name = ?, gmail = ?, phone_number = ?, birthday = ?  where id = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);

            if(infoChanged.getBirthday() != null){
                java.sql.Date sqlDate = new java.sql.Date(infoChanged.getBirthday().getTime());
                stmt.setDate(4, sqlDate);
            }
            else{
                stmt.setDate(4, null);
            }
            stmt.setString(1, infoChanged.getName());
            stmt.setString(2, infoChanged.getGmail());
            stmt.setString(3, infoChanged.getPhoneNumber());

            stmt.setInt(5, userId);
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

    public Boolean updateAvatar(String uri, int userId){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "Update users set img = ? where id = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, uri);
            stmt.setInt(2, userId);
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

    public Boolean forgotPassword(String newPassword, int userId){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "Update users set password = ? where id = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
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

    public User register(RegisterInfo request){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "INSERT INTO users(username, password, gmail, name, address, gender, birthday, img, " +
                    "phone_number, cart_id)" +
                    " values(?, ?, ?, null, null, null, null, null, null, ?)";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, request.getUsername());
            stmt.setString(2, request.getPassword());
            stmt.setString(3, request.getGmail());
            stmt.setInt(4, request.getCartId());
            int rowsAffected = stmt.executeUpdate();
            User user = new User();
            if(rowsAffected > 0){
                user.setUsername(request.getUsername());
                user.setPassword(request.getPassword());
                user.setEmail(request.getGmail());
                user.setCartId(request.getCartId());
            }
            return user;
        }
        catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }

    public User login(String username, String password){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "SELECT * FROM users where username = ? and password = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            User user = new User();
            while (rs.next()){
                user.setId(rs.getInt(1));
                user.setUsername(rs.getString(2));
                user.setPassword(rs.getString(3));
                user.setGmail(rs.getString(4));
                user.setName(rs.getString(5));
                user.setAddress(rs.getString(6));
                user.setGender(rs.getInt(7));
                user.setBirthday(rs.getDate(8));
                user.setImg(rs.getString(9));
                user.setPhoneNumber(rs.getString(10));
                user.setCartId(rs.getInt(11));
                user.setProvinceId(rs.getInt(12));
                user.setDistrictId(rs.getInt(13));
                user.setWardId(rs.getString(14));
                return user;
            }
            return null;
        }
        catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }

    public Boolean checkExistedUsername(String username){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "SELECT COUNT(*) FROM users WHERE username = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                int count = rs.getInt(1);
                return count == 1;
            }
            return false;
        }
        catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }

    public Boolean checkExistedEmail(String email){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "SELECT COUNT(*) FROM users WHERE gmail = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                int count = rs.getInt(1);
                return count == 1;
            }
            return false;
        }
        catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }

    public User findByEmail(String email){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String query = "SELECT * FROM users WHERE gmail = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            User user = new User();
            while (rs.next()){
                user.setId(rs.getInt(1));
                user.setUsername(rs.getString(2));
                user.setPassword(rs.getString(3));
                user.setGmail(rs.getString(4));
                user.setName(rs.getString(5));
                user.setAddress(rs.getString(6));
                user.setGender(rs.getInt(7));
                user.setBirthday(rs.getDate(8));
                user.setImg(rs.getString(9));
                user.setPhoneNumber(rs.getString(10));
                user.setCartId(rs.getInt(11));
                user.setProvinceId(rs.getInt(12));
                user.setDistrictId(rs.getInt(13));
                user.setWardId(rs.getString(14));
            }
            return user;
        }
        catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }

}
