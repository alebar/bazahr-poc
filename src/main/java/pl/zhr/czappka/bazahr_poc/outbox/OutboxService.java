package pl.zhr.czappka.bazahr_poc.outbox;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
public class OutboxService {

    private final static Map<String, List<String>> listenersRegistry = initSampleListenersRegistry();

    private final OutboxRepository outboxRepository;

    OutboxService(final OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    public void queue(
            final String type,
            final Map<String, Object> payload) {
        var currentTime = Instant.now();
        listenersRegistry.get(type).stream().
                map(targetUrl -> new OutgoingMessage(type, payload, currentTime, targetUrl)).
                forEach(outboxRepository::save);
    }

    private static Map<String, List<String>> initSampleListenersRegistry() {
        var kof = "http://localhost:8082/api/v1/events";
        var zhrid = "http://localhost:8083/api/v1/events";
        var eUniform = "http://localhost:8084/api/v1/events";

        return Map.of(
                "urn:ekp:events/candidate-accepted", List.of(eUniform, zhrid),
                "urn:ekp:events/member-died", List.of(kof, zhrid)
        );
    }

}
