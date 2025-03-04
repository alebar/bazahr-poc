package pl.zhr.czappka.bazahr_poc.outbox;

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
class OutboxRepository {

    private final static TypeReference<HashMap<String,Object>> MAP_REF = new TypeReference<>() {};

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
            var type = msg.type;
            var payload = this.objectMapper.writeValueAsString(msg.payload);
            var createdAt = Timestamp.from(msg.createdAt);
            var targetUrl = msg.targetUrl;
            var status = msg.status.name();

            if (null == msg.id) {
                msg.id = insert(type, payload, createdAt, targetUrl, status);
            } else {
                update(
                        msg.id, type, payload, createdAt, targetUrl, status,
                        msg.getPublishingStartedAt().map(Timestamp::from).orElse(null)
                );
            }
            return msg;
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Integer insert(
            final String type,
            final String payload,
            final Timestamp createdAt,
            final String targetUrl,
            final String status) {
        return (Integer) this.messageInsert.executeAndReturnKey(Map.of(
                "type", type,
                "payload", payload,
                "created_at", createdAt,
                "target_url", targetUrl,
                "status", status));
    }

    private void update(
            final Integer id,
            final String type,
            final String payload,
            final Timestamp createdAt,
            final String targetUrl,
            final String status,
            final Timestamp publishingStartedAt) {
        this.jdbcTemplate.update(
                "update outbox set type=?, payload=?::jsonb, created_at=?, target_url=?, status=?, publishing_started_at=? where id=?;",
                type, payload, createdAt, targetUrl, status, publishingStartedAt, id
        );
    }

    private final static String sql = """
            update outbox
            set status = 'publishing', publishing_started_at = now()
            where id = (
                select id from outbox
                where
                    (status = 'pending')
                    or (
                        status = 'publishing'
                        and publishing_started_at is not null
                        and publishing_started_at < now() - interval '3 minutes')
                order by created_at
                limit 1
                for update skip locked
            )
            returning *;
            """;

    Optional<OutgoingMessage> findOldestPendingMessageSettingProcessing() {
        final PreparedStatementCallback<Optional<OutgoingMessage>> callback = ps -> {
            ps.execute();
            var rs = ps.getResultSet();
            if (rs.next()) {
                try {
                    var payload = this.objectMapper.readValue(
                            rs.getString("payload"),
                            MAP_REF
                    );
                    var om = new OutgoingMessage(
                            rs.getInt("id"),
                            rs.getString("type"),
                            payload,
                            rs.getTimestamp("created_at").toInstant(),
                            rs.getString("target_url"),
                            rs.getTimestamp("publishing_started_at").toInstant(),
                            OutgoingMessage.Status.valueOf(rs.getString("status"))
                    );
                    return Optional.of(om);
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
