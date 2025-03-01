package pl.zhr.czappka.bazahr_poc.inbox;

import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

class IncomingMessage {

    @Id
    UUID id;

    Instant createdAt;


}
