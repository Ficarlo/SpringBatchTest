package com.apave.AGATETOBDEQT.Controller;

import com.apave.AGATETOBDEQT.Model.Marquage;
import com.apave.AGATETOBDEQT.Model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


//Vérification des données en base
//.a.A.C.JobCompletionNotificationListener : Found <firstName: JOHN, lastName: DOE> in the database.
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            log.info("marquage result:");
            jdbcTemplate.query("SELECT log_id, person_id from marquage ",
                    (rs, row) -> new Marquage(
                            rs.getInt("log_id"),
                            rs.getInt("person_id")
                    )

            ).forEach(logPerson -> log.info("Found <" + logPerson + "> in the database."));


            //log.info("Person result:");
            //jdbcTemplate.query("SELECT person_id, first_name, last_name FROM people ",
            //        (rs, row) -> new Person(
            //                rs.getInt("person_id"),
            //                rs.getString("first_name"),
            //                rs.getString("last_name")
            //        )
//
            //).forEach(logPerson -> log.info("Found <" + logPerson + "> in the database."));
        }
    }
}
