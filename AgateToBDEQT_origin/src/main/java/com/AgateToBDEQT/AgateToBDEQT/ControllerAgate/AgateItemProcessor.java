package com.AgateToBDEQT.AgateToBDEQT.ControllerAgate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.AgateToBDEQT.AgateToBDEQT.Model.Person;
import org.springframework.jdbc.core.RowMapper;

public class AgateItemProcessor implements RowMapper<Person>{

    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {

        Person result = new Person();
        result.setFirstName(rs.getString("student_Firstname"));
        result.setLastName(rs.getString("student_Lastname"));

        return result;
    }

}

