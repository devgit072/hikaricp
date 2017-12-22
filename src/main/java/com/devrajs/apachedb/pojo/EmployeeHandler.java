package com.devrajs.apachedb.pojo;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

//This is custom handler
public class EmployeeHandler extends BeanListHandler<Employee> {
    private Connection connection;

    public EmployeeHandler(Connection connection) {
        super(Employee.class);
        this.connection = connection;
    }

    @Override
    public List<Employee> handle(ResultSet resultSet) throws SQLException {
        List<Employee> list = super.handle(resultSet);
        QueryRunner queryRunner = new QueryRunner();
        BeanListHandler<Email> handler = new BeanListHandler<>(Email.class);
        String query = "SELECT * FROM email WHERE employeeid = ?";

        for(Employee employee : list){
            List<Email> emails = queryRunner.query(connection, query, handler, employee.getId());
            employee.setEmailList(emails);
        }
        return list;
    }
}
