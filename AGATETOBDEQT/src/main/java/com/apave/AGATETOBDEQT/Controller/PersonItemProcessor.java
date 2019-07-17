package com.apave.AGATETOBDEQT.Controller;

import com.apave.AGATETOBDEQT.Model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

    //Convertion des données
    // c.a.A.Controller.PersonItemProcessor     : Converting (firstName: John, lastName: Doe) into (firstName: JOHN, lastName: DOE)
    @Override
    public Person process(final Person person) throws Exception {
        final String firstName = person.getFirstName().toUpperCase();
        final String lastName = person.getLastName().toUpperCase();

        final Person transformedPerson = new Person(person.getPersonId(),firstName, lastName);

        log.info("Converting (" + person + ") into (" + transformedPerson + ")");

        if(person.getPersonId() == 1){
            log.info("Ne pas retourner Samuel");
           return null;
        }
        else{
            return transformedPerson;
        }

    }

}