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

    InboxRepository(
            final JdbcTemplate jdbcTemplate,
            final ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
        this.messageInsert = new SimpleJdbcInsert(this.jdbcTemplate).
                withTableName("inbox").
                usingGeneratedKeyColumns("id");
    }

    IncomingMessage save(final IncomingMessage msg) {
        try {
            var type = msg.type;
            var createdAt = Timestamp.from(msg.createdAt);
            var payload = this.objectMapper.writeValueAsString(msg.payload);
            var status = msg.status.name();
            var processingStartedAt = msg.getProcessingStartedAt().
                    map(Timestamp::from).
                    orElse(null);

            if (null == msg.id) {
                msg.id = insert(type, createdAt, payload, status, processingStartedAt);
            } else {
                update(msg.id, type, createdAt, payload, status, processingStartedAt);
            }

            return msg;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Integer insert(
            final String type,
            final Timestamp createdAt,
            final String payload,
            final String status,
            final Timestamp processingStartedAt) {
        Map<String, Object> args = new HashMap<>();
        args.put("type", type);
        args.put("created_at", createdAt);
        args.put("payload", payload);
        args.put("status", status);
        Optional.ofNullable(processingStartedAt).
                ifPresent(t -> args.put("processing_started_at", t));

        return (Integer) this.messageInsert.executeAndReturnKey(args);
    }

    private void update(
            final Integer id,
            final String type,
            final Timestamp createdAt,
            final String payload,
            final String status,
            final Timestamp processingStartedAt) {
        this.jdbcTemplate.update(
                "update inbox set type=?, created_at=?, payload=?::jsonb, status=?, processing_started_at=? where id=?;",
                type, createdAt, payload, status, processingStartedAt, id
        );
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
                            IncomingMessage.Status.valueOf(rs.getString("status"))
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
