package pl.zhr.czappka.bazahr_poc.inbox;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
class InboxProcessor {

    private final Log log = LogFactory.getLog(InboxProcessor.class);

    private final InboxRepository repository;
    private final DenormalizationService denormalizationService;

    InboxProcessor(InboxRepository repository, DenormalizationService denormalizationService) {
        this.repository = repository;
        this.denormalizationService = denormalizationService;
    }

    @Scheduled(fixedDelay = 2, timeUnit = TimeUnit.SECONDS)
    void findAndProcess() {
        var opt = repository.findOldestPendingMessageSettingProcessing();

        if (opt.isEmpty()) {
            log.debug("No incoming message to process.");
        } else {
            var msg = opt.get();
            if (log.isDebugEnabled()) {
                log.debug("Processing incoming message: " + msg);
            }
            try {
                this.denormalizationService.handle(msg);
                msg.finish();
                this.repository.save(msg);
            } catch (Throwable t) {
                log.warn("Problem while processing msg with id: " + msg.id, t);
                msg.fail();
                this.repository.save(msg);
            }
        }
    }
}
