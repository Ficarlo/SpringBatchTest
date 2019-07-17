package com.apave.AGATETOBDEQT.Service;

import com.apave.AGATETOBDEQT.Controller.JobCompletionNotificationListener;
import com.apave.AGATETOBDEQT.Controller.Processor.PersonItemProcessor;
import com.apave.AGATETOBDEQT.Controller.Mapper.PersonRowMapper;
import com.apave.AGATETOBDEQT.Model.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobRegistry jobRegistry;

    @Autowired
    private DataSource dataSource;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public ItemStreamReader<Person> reader() {
        JdbcCursorItemReader<Person> reader = new JdbcCursorItemReader<Person>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT person_id, first_name, last_name FROM people");
        reader.setRowMapper(new PersonRowMapper());
        return reader;
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO marquage (person_id) VALUES (:personId)")
                .dataSource(dataSource)
                .build();
    }




    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Person> writer) {
        return stepBuilderFactory.get("step1")
                .<Person, Person> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
    // end::jobstep[]

    //@Bean
    //public JdbcBatchItemWriter<Person> writer2(DataSource dataSource) {
    //    return new JdbcBatchItemWriterBuilder<Person>()
    //            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
    //            .sql("UPDATE people set first_name= :firstName, last_name = :lastName where person_id = :personId")
    //            .dataSource(dataSource)
    //            .build();
    //}


    //public CompositeItemWriter<T> compositeItemWriter(){
    //    CompositeItemWriter writer = new CompositeItemWriter();
    //    writer.setDelegates(Arrays.asList(writer(),writer2()));
    //    return writer;
    //}
}