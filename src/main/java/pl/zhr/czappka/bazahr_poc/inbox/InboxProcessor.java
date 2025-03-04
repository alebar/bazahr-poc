package pl.zhr.czappka.bazahr_poc.inbox;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.zhr.czappka.bazahr_poc.Denormalizer;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
class InboxProcessor {

    private final Log log = LogFactory.getLog(InboxProcessor.class);

    private final InboxRepository repository;
    private final List<Denormalizer> denormalizers;

    InboxProcessor(InboxRepository repository, List<Denormalizer> denormalizers) {
        this.repository = repository;
        this.denormalizers = denormalizers;
    }

    @Scheduled(fixedDelay = 2, timeUnit = TimeUnit.SECONDS)
    public void scheduledTask() {
        var opt = repository.findOldestPendingMessageSettingProcessing();

        if (opt.isEmpty()) {
            log.debug("No incoming message to process.");
            return;
        }

        var msg = opt.get();

        if (log.isDebugEnabled()) {
            log.debug("Processing incoming message: " + msg);
        }

        denormalizers.
                stream().
                filter(d -> d.accepts(msg.type)).
                forEach(d -> d.denormalize(msg.payload));
    }
}
