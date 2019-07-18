package com.apave.AGATETOBDEQT.Configuration;

import com.apave.AGATETOBDEQT.Configuration.JobCompletionNotificationListener;
import com.apave.AGATETOBDEQT.Processor.PersonItemProcessor;
import com.apave.AGATETOBDEQT.Mapper.PersonRowMapper;
import com.apave.AGATETOBDEQT.Model.Person;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {

    @Autowired
    Environment environment;

    @Autowired
    private JobRegistry jobRegistry;

    @Autowired
    private DataSource dataSource;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    private PlatformTransactionManager transactionManager;



    //@Bean(destroyMethod = "close")
    //DataSource dataSource(Environment env) {
    //    HikariConfig dataSourceConfig = new HikariConfig();
//
    //    dataSourceConfig.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
    //    dataSourceConfig.setJdbcUrl(environment.getProperty("spring.datasource.url"));
    //    dataSourceConfig.setUsername(environment.getProperty("spring.datasource.username"));
    //    dataSourceConfig.setPassword(environment.getProperty("spring.datasource.password"));
//
    //    return new HikariDataSource(dataSourceConfig);
    //}



    @Override
    //https://stackoverflow.com/questions/25077549/spring-batch-without-persisting-metadata-to-database
    public void setDataSource(DataSource dataSource) {
        this.dataSource = null;
        this.transactionManager = new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public ItemStreamReader<Person> reader() {
        JdbcCursorItemReader<Person> reader = new JdbcCursorItemReader<Person>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT person_id, first_name, last_name FROM PR1BD000.people");
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
                .sql("INSERT INTO PR1BD000.marquage (log_id, person_id) VALUES (:personId, :personId)")
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