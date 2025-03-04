package pl.zhr.czappka.bazahr_poc.inbox;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.zhr.czappka.bazahr_poc.Denormalizer;
import pl.zhr.czappka.bazahr_poc.outbox.OutboxService;

import java.util.List;

@Component
class InboxProcessor {

    private final List<Denormalizer> denormalizers;
    private final InboxRepository inboxRepository;
    private final OutboxService outboxService;

    InboxProcessor(
            final List<Denormalizer> denormalizers,
            final InboxRepository inboxRepository,
            final OutboxService outboxService) {
        this.denormalizers = denormalizers;
        this.inboxRepository = inboxRepository;
        this.outboxService = outboxService;
    }

    @Transactional
    void process(final IncomingMessage msg) {
        this.denormalizers.
                stream().
                filter(d -> d.accepts(msg.type)).
                forEach(d -> d.denormalize(msg.payload));
        this.outboxService.queue(msg.type, msg.payload);
        msg.finish();
        this.inboxRepository.save(msg);
    }

}
