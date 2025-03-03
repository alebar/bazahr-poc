package pl.zhr.czappka.bazahr_poc.inbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.Timestamp;
import java.util.Map;

class InboxRepository {

    final JdbcTemplate jdbcTemplate;
    final ObjectMapper objectMapper;

    final SimpleJdbcInsert messageInsert;

    InboxRepository(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
        this.messageInsert = new SimpleJdbcInsert(this.jdbcTemplate).
                withTableName("inbox").
                usingGeneratedKeyColumns("id");
    }

    IncomingMessage save(final IncomingMessage msg) throws JsonProcessingException {
        final var payload = this.objectMapper.writeValueAsString(msg.payload);
        var id = this.messageInsert.executeAndReturnKey(Map.of(
                "created_at", Timestamp.from(msg.createdAt),
                "payload", payload,
                "status", msg.status.name()
        ));
        msg.id = (Integer) id;
        return msg;
    }
}
