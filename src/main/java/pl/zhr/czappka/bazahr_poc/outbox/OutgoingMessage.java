package pl.zhr.czappka.bazahr_poc.outbox;

import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.Map;

class OutgoingMessage {

    enum Status {
        pending,
        publishing,
        published,
        failed
    }

    @Id
    Integer id;

    String type;

    Map<String, Object> payload;

    Instant createdAt;

    String targetUrl;

    Instant publishingStartedAt;

    Status status;


    OutgoingMessage(String type, Map<String, Object> payload, Instant createdAt, String targetUrl) {
        this.type = type;
        this.payload = payload;
        this.createdAt = createdAt;
        this.targetUrl = targetUrl;
        this.status = Status.pending;
    }
}
