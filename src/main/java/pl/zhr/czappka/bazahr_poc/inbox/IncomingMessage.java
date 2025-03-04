package pl.zhr.czappka.bazahr_poc.inbox;

import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

class IncomingMessage {

    enum Status {
        pending,
        processing,
        failed,
        finished
    }
    
    @Id
    Integer id;

    String type;

    Instant createdAt;

    Map<String, Object> payload;

    Status status;

    Instant processingStartedAt;

    IncomingMessage(
            final String type,
            final Instant createdAt,
            final Map<String, Object> payload,
            final Status status) {
        this.type = type;
        this.createdAt = createdAt;
        this.payload = payload;
        this.status = status;
    }

    IncomingMessage(
            final Integer id,
            final String type,
            final Instant createdAt,
            final Map<String, Object> payload,
            final Status status) {
        this.id = id;
        this.type = type;
        this.createdAt = createdAt;
        this.payload = payload;
        this.status = status;
    }

    void fail() {
        clearProcessing();
        this.status = Status.failed;
    }

    void finish() {
        clearProcessing();
        this.status = Status.finished;
    }

    private void clearProcessing() {
        this.processingStartedAt = null;
    }

    Optional<Instant> getProcessingStartedAt() {
        return Optional.ofNullable(this.processingStartedAt);
    }

    @Override
    public String toString() {
        return "IncomingMessage{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", createdAt=" + createdAt +
                ", payload=" + payload +
                ", status=" + status +
                '}';
    }
}
