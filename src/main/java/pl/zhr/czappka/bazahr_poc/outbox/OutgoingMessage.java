package pl.zhr.czappka.bazahr_poc.outbox;

import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

class OutgoingMessage {

    enum Status {
        pending,
        publishing,
        published,
        failed;
    }

    @Id
    Integer id;

    String type;

    Map<String, Object> payload;

    Instant createdAt;

    String targetUrl;

    Instant publishingStartedAt;

    Status status;

    OutgoingMessage(
            final String type,
            final Map<String, Object> payload,
            final Instant createdAt,
            final String targetUrl) {
        this(null, type, payload, createdAt, targetUrl, null, Status.pending);
    }

    OutgoingMessage(
            final Integer id,
            final String type,
            final Map<String, Object> payload,
            final Instant createdAt,
            final String targetUrl,
            final Instant publishingStartedAt,
            final Status status) {
        this.id = id;
        this.type = type;
        this.payload = payload;
        this.createdAt = createdAt;
        this.targetUrl = targetUrl;
        this.publishingStartedAt = publishingStartedAt;
        this.status = status;
    }

    void finish() {
        this.status = Status.published;
        this.publishingStartedAt = null;
    }

    Optional<Instant> getPublishingStartedAt() {
        return Optional.ofNullable(this.publishingStartedAt);
    }

    @Override
    public String toString() {
        return "OutgoingMessage{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", payload=" + payload +
                ", createdAt=" + createdAt +
                ", targetUrl='" + targetUrl + '\'' +
                ", publishingStartedAt=" + publishingStartedAt +
                ", status=" + status +
                '}';
    }
}
