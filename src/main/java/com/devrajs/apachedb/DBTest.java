package com.devrajs.apachedb;

import com.devrajs.apachedb.pojo.Employee;
import com.devrajs.apachedb.pojo.EmployeeHandler;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.AsyncQueryRunner;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DBTest {

    private Connection connection;

    @BeforeTest
    public void setUp() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.ds.PGSimpleDataSource");
        String connStr = "jdbc:postgresql://" + "db-qa1002.ads.corp.inmobi.com" + ":" + 8018 + "/" + "configstore_db";
        connection = DriverManager.getConnection(connStr, "qa_user", "userqa@321");
    }

    @AfterClass
    public void tearDown() {
        DbUtils.closeQuietly(connection);
    }

    @Test
    public void testDB1() throws SQLException {
        MapListHandler mapListHandler = new MapListHandler();
        QueryRunner queryRunner = new QueryRunner();

        List<Map<String, Object>> list = queryRunner.query(connection, "SELECT * FROM employee", mapListHandler);
        for(Map<String, Object> map : list)
        {
            for(Map.Entry<String, Object> entry : map.entrySet())
            {
                System.out.print(entry.toString());
                System.out.print(" , ");
            }
            System.out.println();
        }
    }

    @Test
    public void beanListHandler() throws SQLException {
        BeanListHandler<Employee> employeeBeanListHandler = new BeanListHandler<>(Employee.class);
        QueryRunner queryRunner = new QueryRunner();
        List<Employee> list = queryRunner.query(connection, "SELECT * FROM employee", employeeBeanListHandler);
        for(Employee employee : list)
        {
            System.out.println(employee.toString());
        }
    }

    //For query which will return single value, we can use scalar handler
    @Test
    public void scalarHandler() throws SQLException {
        ScalarHandler<Long> scalarHandler = new ScalarHandler<>();
        QueryRunner queryRunner = new QueryRunner();
        long value = queryRunner.query(connection, "SELECT COUNT(*) FROM employee", scalarHandler);
        System.out.println(value);
    }

    @Test
    public void customHandlerDemo() throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        EmployeeHandler employeeHandler = new EmployeeHandler(connection);
        List<Employee> employeeList = queryRunner.query(connection, "SELECT * FROM employee", employeeHandler);
        for(Employee employee : employeeList)
        {
            System.out.println(employee.toString());
        }
    }

    /*
    There are two ways to insert values:
    One is using update() method
    Other is insert() method

     */
    @Test
    public void insertQueryUsingUpdate() throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        String query_with_placeholder = "INSERT INTO employee (id, firstname,lastname,salary, hireddate) VALUES (?, ?, ?, ?, ?)";
        int countInserted = queryRunner.update(connection, query_with_placeholder, 3, "Holo", "Naina", 6777.0, new Date(new java.util.Date().getTime()));
        System.out.println(countInserted);
    }

    @Test
    public void insertQueryUsingInsert() throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        ScalarHandler<Integer> scalarHandler = new ScalarHandler<>();
        String query_with_placeholder = "INSERT INTO employee (id, firstname,lastname,salary, hireddate) VALUES (?, ?, ?, ?, ?)";
        int countInserted = queryRunner.insert(connection, query_with_placeholder, scalarHandler, 6, "Bhaji", "Sampna", 67277.0, new Date(new java.util.Date().getTime()));
        System.out.println(countInserted);
    }

    @Test
    public void updateRecord() throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        String query = "UPDATE employee set salary = salary * 1.1 where salary > ?";
        int updatedrows = queryRunner.update(connection, query, 7000.0);
        System.out.println(updatedrows);
    }

    @Test
    public void deleteRecord() throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        String query = "DELETE FROM employee where firstname=?";//You don't have to put '' for string...prepared statement will
        //do that...prepared statement you beauty!!
        int deletedNumber = queryRunner.update(connection, query, "Lddaila");
        System.out.println(deletedNumber);
    }

    /*
    DBUtils provide support of asynchronous query, it will return instance of Future
     */

    @Test
    public void testAsynQuery() throws SQLException, InterruptedException, ExecutionException, TimeoutException {
        AsyncQueryRunner asyncQueryRunner = new AsyncQueryRunner(Executors.newCachedThreadPool());
        EmployeeHandler employeeHandler = new EmployeeHandler(connection);
        String query = "SELECT * FROM employee";
        Future<List<Employee>> future = asyncQueryRunner.query(connection, query, employeeHandler);
        List<Employee> employeeList = future.get(10, TimeUnit.SECONDS);//wait for max 10 seconds
        for(Employee employee : employeeList)
        {
            System.out.println(employee.toString());
        }
    }

    @Test
    public void queryRunnerWithDataSource() throws SQLException {
        MapListHandler mapListHandler = new MapListHandler();
        QueryRunner queryRunner = new QueryRunner(new HikariDataSource());
        List<Map<String, Object>> list = queryRunner.query("SELECT * FROM employee", mapListHandler);
    }

    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = LocalDateTime.of(LocalDate.now(ZoneId.of("UTC")), LocalTime.MIDNIGHT);
        System.out.println(localDateTime.toEpochSecond(ZoneOffset.UTC));
        System.out.println(localDateTime.minusDays(2).toEpochSecond(ZoneOffset.UTC));
    }
}
