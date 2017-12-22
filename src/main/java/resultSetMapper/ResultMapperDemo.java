package resultSetMapper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ResultMapperDemo {

    public static void main(String[] args) throws SQLException, IllegalAccessException, NoSuchFieldException,
            InstantiationException {
        ResultMapperDemo resultMapperDemo = new ResultMapperDemo();
        ResultSet resultSet = resultMapperDemo.getResultSet();
        ResultSetMapper<Employee> resultSetMapper = new ResultSetMapper<>();
        List<Employee> employees = resultSetMapper.getObjectsFromResultSet(resultSet, Employee.class);
        for(Employee employee : employees){
            System.out.println(employee.toString());
        }
    }

    ResultSet getResultSet() throws SQLException {
        String configFile = "src/main/resources/db.properties";
        HikariConfig hikariConfig = new HikariConfig(configFile);
        hikariConfig.setDriverClassName("org.postgresql.ds.PGSimpleDataSource");
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        connection = hikariDataSource.getConnection();
        preparedStatement = connection.prepareStatement("select * from employee;");
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
}
