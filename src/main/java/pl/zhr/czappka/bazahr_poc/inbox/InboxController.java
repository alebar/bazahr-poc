package pl.zhr.czappka.bazahr_poc.inbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.util.Map;

@RestController
class InboxController {

    private final InboxRepository inboxRepository;

    InboxController(InboxRepository inboxRepository) {
        this.inboxRepository = inboxRepository;
    }

    @PostMapping("/v1/messages/")
    void createMessage() {
        var msg = new IncomingMessage(
                Instant.now(),
                Map.of("personId", "urn:kof:people/123456"),
                IncomingMessageStatus.pending
        );
        try {
            this.inboxRepository.save(msg);
        } catch (JsonProcessingException e) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
    }

}
