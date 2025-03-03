package pl.zhr.czappka.bazahr_poc.inbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Configuration
class InboxConfiguration {

    final JdbcTemplate jdbcTemplate;

    final ObjectMapper objectMapper;

    InboxConfiguration(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @Bean
    InboxRepository inboxRepository() {
        return new InboxRepository(jdbcTemplate, objectMapper);
    }

    @Bean
    JobDetail inboxProcessingJob() {
        return JobBuilder.newJob().
                ofType(InboxProcessor.class).
                storeDurably().
                withIdentity("Inbox_Processing_Job").
                withDescription("Processing incoming messages").
                build();
    }

    @Bean
    Trigger inboxProcesssingTrigger(final JobDetail inboxProcessingJob) {
        return TriggerBuilder.newTrigger().
                forJob(inboxProcessingJob).
                withIdentity("Inbox_Processing_Trigger").
                withDescription("Trigger for incoming messages processing").
                withSchedule(
                        simpleSchedule().
                                repeatForever().
                                withIntervalInSeconds(2)).
                build();
    }

}
