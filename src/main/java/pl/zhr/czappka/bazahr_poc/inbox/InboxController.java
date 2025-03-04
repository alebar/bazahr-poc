package pl.zhr.czappka.bazahr_poc.inbox;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
class InboxController {

    private final static Log log = LogFactory.getLog(InboxController.class);

    private final InboxRepository inboxRepository;

    InboxController(final InboxRepository inboxRepository) {
        this.inboxRepository = inboxRepository;
    }

    @PostMapping("/v1/messages/")
    void createMessage() {
        var msg = new IncomingMessage(
                "urn:ekp:events/candidate-accepted",
                Instant.now(),
                Map.of(
                        "commanderId", "urn:kof:people/123456",
                        "candidateIds", List.of("urn:kof:people/1", "urn:kof:people/2"),
                        "unitId", "urn:ekp:units/1a"
                ),
                IncomingMessage.Status.pending
        );
        this.inboxRepository.save(msg);
    }

    @GetMapping("/v1/message-test")
    void testUpdateReturning() {
        inboxRepository.findOldestPendingMessageSettingProcessing().
                ifPresent(m ->
                        log.info("retrieved incoming message: " + m));
    }

}
