package com.devrajs.hikaricp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//Create config in code itself rather than reading directly from file itself
public class Example2 {

    public static void main(String[] args) throws SQLException {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:8018/configstore_db");
        /*
        dataSource.user=qa_user
dataSource.password=userqa@321
dataSource.cachePrepStmts=true
dataSource.prepStmtCacheSize=250
dataSource.prepStmtCacheSqlLimit=2048
         */
        hikariConfig.setUsername("<>");
        hikariConfig.setPassword("<>");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        //hikariConfig.setDriverClassName("org.postgresql.ds.PGSimpleDataSource"); You don't have to set driver name, hikari will automatically set the name based on db type
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try
        {
            connection = hikariDataSource.getConnection();
            preparedStatement = connection.prepareStatement("select * from config where id=11");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                String name = resultSet.getString("name");
                System.out.println("Here is configName : " + name);
            }
        }
        finally {
            if(connection != null)
            {
                connection.close();
            }
        }
    }
}
