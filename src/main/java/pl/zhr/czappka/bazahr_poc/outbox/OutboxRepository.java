package pl.zhr.czappka.bazahr_poc.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Map;

@Component
class OutboxRepository {

    final JdbcTemplate jdbcTemplate;
    final ObjectMapper objectMapper;

    final SimpleJdbcInsert messageInsert;

    OutboxRepository(
            final ObjectMapper objectMapper,
            final JdbcTemplate jdbcTemplate) {
        this.objectMapper = objectMapper;
        this.jdbcTemplate = jdbcTemplate;

        this.messageInsert = new SimpleJdbcInsert(this.jdbcTemplate).
                withTableName("outbox").
                usingGeneratedKeyColumns("id");
    }

    OutgoingMessage save(final OutgoingMessage msg) {
        try {
            var payload = this.objectMapper.writeValueAsString(msg.payload);
            msg.id = (Integer) this.messageInsert.executeAndReturnKey(Map.of(
                    "type", msg.type,
                    "payload", payload,
                    "created_at", Timestamp.from(msg.createdAt),
                    "target_url", msg.targetUrl,
                    "status", msg.status.name()));
            return msg;
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
