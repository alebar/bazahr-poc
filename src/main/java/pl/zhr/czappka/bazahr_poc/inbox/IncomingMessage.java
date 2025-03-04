package pl.zhr.czappka.bazahr_poc.inbox;

import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.Map;

class IncomingMessage {

    @Id
    Integer id;

    Instant createdAt;

    Map<String, Object> payload;

    IncomingMessageStatus status;

    IncomingMessage(Instant createdAt, Map<String, Object> payload, IncomingMessageStatus status) {
        this.createdAt = createdAt;
        this.payload = payload;
        this.status = status;
    }

    IncomingMessage(Integer id, Instant createdAt, Map<String, Object> payload, IncomingMessageStatus status) {
        this.id = id;
        this.createdAt = createdAt;
        this.payload = payload;
        this.status = status;
    }

    @Override
    public String toString() {
        return "IncomingMessage{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", payload=" + payload +
                ", status=" + status +
                '}';
    }
}
