package pl.zhr.czappka.bazahr_poc.inbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
class InboxRepository {

    private final static TypeReference<HashMap<String,Object>> MAP_REF = new TypeReference<>() {};

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
                "type", msg.type,
                "created_at", Timestamp.from(msg.createdAt),
                "payload", payload,
                "status", msg.status.name()
        ));
        msg.id = (Integer) id;
        return msg;
    }

    private final static String sql = """
            update inbox
            set status = 'processing', processing_started_at = now()
            where id = (
                select id from inbox
                where
                    (status = 'pending')
                    or (
                        status = 'processing'
                        and processing_started_at is not null
                        and processing_started_at < now() - interval '3 minutes')
                order by created_at
                limit 1
                for update skip locked
            )
            returning *;
            """;

    Optional<IncomingMessage> findOldestPendingMessageSettingProcessing() {
        final PreparedStatementCallback<Optional<IncomingMessage>> callback = ps -> {
            ps.execute();
            var rs = ps.getResultSet();
            if (rs.next()) {
                try {
                    var payload = this.objectMapper.readValue(
                            rs.getString("payload"),
                            MAP_REF
                    );
                    var im = new IncomingMessage(
                            rs.getInt("id"),
                            rs.getString("type"),
                            rs.getTimestamp("created_at").toInstant(),
                            payload,
                            IncomingMessageStatus.valueOf(rs.getString("status"))
                    );
                    return Optional.of(im);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            } else {
                return Optional.empty();
            }
        };
        return this.jdbcTemplate.execute(
                con -> con.prepareStatement(sql),
                callback
        );
    }
}
