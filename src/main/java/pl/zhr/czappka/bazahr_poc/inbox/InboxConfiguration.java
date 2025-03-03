package pl.zhr.czappka.bazahr_poc.inbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

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

}
