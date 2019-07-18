package com.apave.AGATETOBDEQT.Processor;

import com.apave.AGATETOBDEQT.wsSoap.WebServiceClient;
import com.apave.AGATETOBDEQT.Model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import static com.apave.AGATETOBDEQT.wsSoap.WsConstantes.NAME_PACKAGE;
import static com.apave.AGATETOBDEQT.wsSoap.WsConstantes.URL_WS;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

    //Convertion des données
    // c.a.A.Controller.PersonItemProcessor     : Converting (firstName: John, lastName: Doe) into (firstName: JOHN, lastName: DOE)
    @Override
    public Person process(final Person person) throws Exception {
        final String firstName = person.getFirstName().toUpperCase();
        final String lastName = person.getLastName().toUpperCase();
        final int personId = person.getPersonId();

        final Person transformedPerson = new Person(person.getPersonId(),firstName, lastName);

        log.info("Converting (" + person + ") into (" + transformedPerson + ")");

        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // package must match the package in the <generatePackage> specified in pom.xml
        marshaller.setContextPath(NAME_PACKAGE);

        WebServiceClient wsSoap = new WebServiceClient();
        wsSoap.setDefaultUri(URL_WS);//url sans ?wsdl a la fin
        wsSoap.setMarshaller(marshaller);
        wsSoap.setUnmarshaller(marshaller);

        int ret = wsSoap.somme(person.getPersonId(), person.getPersonId());

        log.info("Retour wsSoap:"+ret);
        if( ret <3){
            //https://stackoverflow.com/questions/32605181/how-to-terminate-step-from-a-spring-batch-job
           return null;
        }
        else{
            return transformedPerson;
        }

    }

}