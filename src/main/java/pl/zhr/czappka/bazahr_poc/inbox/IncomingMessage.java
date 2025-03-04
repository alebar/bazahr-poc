package pl.zhr.czappka.bazahr_poc.inbox;

import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

class IncomingMessage {

    @Id
    Integer id;

    String type;

    Instant createdAt;

    Map<String, Object> payload;

    IncomingMessageStatus status;

    Instant processingStartedAt;

    IncomingMessage(String type, Instant createdAt, Map<String, Object> payload, IncomingMessageStatus status) {
        this.type = type;
        this.createdAt = createdAt;
        this.payload = payload;
        this.status = status;
    }

    IncomingMessage(Integer id, String type, Instant createdAt, Map<String, Object> payload, IncomingMessageStatus status) {
        this.id = id;
        this.type = type;
        this.createdAt = createdAt;
        this.payload = payload;
        this.status = status;
    }

    void fail() {
        clearProcessing();
        this.status = IncomingMessageStatus.failed;
    }

    void finish() {
        clearProcessing();
        this.status = IncomingMessageStatus.finished;
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
