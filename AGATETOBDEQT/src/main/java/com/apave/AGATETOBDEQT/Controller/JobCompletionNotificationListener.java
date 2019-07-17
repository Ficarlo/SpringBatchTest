package com.apave.AGATETOBDEQT.Controller;

import com.apave.AGATETOBDEQT.Model.Log;
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

            jdbcTemplate.query("SELECT log_id, person_id from log ",
                    (rs, row) -> new Log(
                            rs.getInt("log_id"),
                            rs.getInt("person_id")
                    )

            ).forEach(logPerson -> log.info("Found <" + logPerson + "> in the database."));
        }
    }
}
