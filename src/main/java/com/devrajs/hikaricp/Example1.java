package com.devrajs.hikaricp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Basic code snippet of hikari cp where we will create simple connection from db pool
// Example can be seen from here : https://github.com/brettwooldridge/HikariCP#initialization
public class Example1 {

    public static void main(String[] args) throws SQLException {
        String configFile = "src/main/resources/db.properties";
        HikariConfig hikariConfig = new HikariConfig(configFile);
        hikariConfig.setDriverClassName("org.postgresql.ds.PGSimpleDataSource");
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = hikariDataSource.getConnection();
            preparedStatement = connection.prepareStatement("select * from config where id=11;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                String name = resultSet.getString("name");
                System.out.println("result: " + name);
            }
        }finally {
            if(connection != null)
                connection.close();
        }
    }

}
