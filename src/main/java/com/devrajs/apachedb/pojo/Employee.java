package com.devrajs.apachedb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//Give no args constructor
public class Employee {
    private Integer id;
    private String firstName;
    private String lastName;
    private Double salary;
    private Date hiredDate;
    private List<Email> emailList;
}
