package com.devrajs.apachedb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    private Integer id;
    private Integer employeeId;
    private String address;
}
