package com.apave.AGATETOBDEQT.Controller.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.apave.AGATETOBDEQT.Model.Person;
import org.springframework.jdbc.core.RowMapper;

public class PersonRowMapper implements RowMapper<Person> {

    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person person = new Person();
        person.setPersonId(rs.getInt("person_id"));
        person.setFirstName(rs.getString("first_name"));
        person.setLastName(rs.getString("last_name"));

        return person;
    }

}
