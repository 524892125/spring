package com.example;

import javax.xml.crypto.Data;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class UpdateData {

    private static final String URL = "jdbc:mysql://rm-bp1340o08f808ymdg.mysql.rds.aliyuncs.com:3306/icp_filing";
    private static final String USER = "dev_jinghong";
    private static final String PASSWORD = "AqP+=0-a&d0izsXGvP37lKkknu&KFL";

    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // 1. 注册JDBC驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 打开连接
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            String selectQuery = "SELECT * FROM if_article";
            preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String time = resultSet.getString("publish_time");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.parse(time, formatter);
                System.out.println(localDateTime);


//                LocalDateTime newTime = localDateTime.plusDays(113);

//                String sql = "UPDATE if_article SET publish_time = ? WHERE id = ?";
//                preparedStatement = connection.prepareStatement(sql);
//                preparedStatement.setString(1, formatter.format(newTime));
//
//                long id = resultSet.getLong("id");
//                System.out.println(id);
//                preparedStatement.setLong(2, id);
//                int rowsAffected = preparedStatement.executeUpdate();
//                if (rowsAffected > 0) {
//                    System.out.println("Row " + row + " updated successfully.");
//                } else {
//                    System.out.println("Failed to update row " + row);
//                }
            }



            // 3. 执行查询或更新
//            String sql = "SELECT your_table_name SET column1 = ? WHERE column2 = ?";
//            preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setString(1, "new_value");
//            preparedStatement.setInt(2, 1);
//
//            int rowsAffected = preparedStatement.executeUpdate();
//            System.out.println("Rows affected: " + rowsAffected);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 4. 清理环境
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
