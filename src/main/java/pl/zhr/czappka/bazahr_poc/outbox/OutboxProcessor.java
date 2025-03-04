package pl.zhr.czappka.bazahr_poc.outbox;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
class OutboxProcessor {

    private final Log log = LogFactory.getLog(OutboxProcessor.class);

    private final OutboxRepository outboxRepository;

    OutboxProcessor(final OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    @Scheduled(fixedDelay = 2, timeUnit = TimeUnit.SECONDS)
    void findAndFireProcessing() {
        var opt = this.outboxRepository.findOldestPendingMessageSettingProcessing();

        if (opt.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("No outgoing message to process.");
            }
        } else {
            var msg = opt.get();
            if (log.isDebugEnabled()) {
                log.debug("Processing outgoing message: " + msg);
            }

            try {
                log.info(
                        String.format(
                                "Sending POST request to targetUrl: %s with event type: %s and payload: %s",
                                msg.targetUrl,
                                msg.type,
                                msg.payload.toString())
                );
                msg.finish();
                this.outboxRepository.save(msg);
            } catch (final Throwable t) {
                // Here we would reschedule publishing for another try,
                // unless retry limit is reached, which would cause message to fail.

                // Not implemented, but you get the idea...
            }
        }
    }

}
