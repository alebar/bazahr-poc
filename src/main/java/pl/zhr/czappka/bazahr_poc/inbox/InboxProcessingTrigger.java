package pl.zhr.czappka.bazahr_poc.inbox;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
class InboxProcessingTrigger {

    private final Log log = LogFactory.getLog(InboxProcessingTrigger.class);

    private final InboxRepository repository;
    private final InboxProcessor inboxProcessor;

    InboxProcessingTrigger(final InboxRepository repository, final InboxProcessor inboxProcessor) {
        this.repository = repository;
        this.inboxProcessor = inboxProcessor;
    }

    @Scheduled(fixedDelay = 2, timeUnit = TimeUnit.SECONDS)
    void findAndFireProcessing() {
        var opt = repository.findOldestPendingMessageSettingProcessing();

        if (opt.isEmpty()) {
            log.debug("No incoming message to process.");
        } else {
            var msg = opt.get();
            if (log.isDebugEnabled()) {
                log.debug("Processing incoming message: " + msg);
            }
            try {
                this.inboxProcessor.process(msg);
            } catch (Throwable t) {
                log.warn("Problem while processing msg with id: " + msg.id, t);
                msg.fail();
                this.repository.save(msg);
            }
        }
    }
}
