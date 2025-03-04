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

    InboxProcessor(InboxRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedDelay = 2, timeUnit = TimeUnit.SECONDS)
    public void scheduledTask() {
        log.info("job się odpalił z scheduled");

    }
}
