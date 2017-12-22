package resultSetMapper;

import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Timestamp;

/*
This class is mapped with a table called employee in DB. It is used for mapping resultSet to List of object of this
class
 */
@ToString
@Entity
public class Employee {

    @Column(name = "id")
    private int id;
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    @Column(name = "salary")
    private double salary;
    @Column(name = "hireddate")
    private Timestamp hiredDate;
}
