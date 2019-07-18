package com.AgateToBDEQT.AgateToBDEQT.ControllerAgate;

import javax.sql.DataSource;

import com.AgateToBDEQT.AgateToBDEQT.Model.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    // tag::readerwriterprocessor[]


    @Bean
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {

        Person result = new Person();
        result.setFirstName(rs.getString("student_Firstname"));
        result.setLastName(rs.getString("student_Lastname"));

        return result;
    }
    @Bean
    public FlatFileItemReader<Person> reader() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names(new String[]{"firstName", "lastName"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                    setTargetType(Person.class);
                }})
                .build();
    }

    //@Bean
    //public Jdbc<Person> writer(DataSource dataSource) {
    //        return new JdbcBatchItemWriterBuilder<Person>()
    //                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
    //                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
    //                .dataSource(dataSource)
    //                .build();
    //}

    @Bean
    public AgateItemProcessor processor() {
        return new AgateItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
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
        ResultSet rS = new ResultSet();
        return stepBuilderFactory.get("step1")
                .<Person, Person> chunk(10)
                .reader(mapRow(rS,0))
                .processor(processor())
                .writer(writer)
                .build();
    }
    // end::jobstep[]
}
