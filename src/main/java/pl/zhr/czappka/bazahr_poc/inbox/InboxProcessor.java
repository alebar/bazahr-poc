package pl.zhr.czappka.bazahr_poc.inbox;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
class InboxProcessor implements Job {

    private Log log = LogFactory.getLog(InboxProcessor.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("job się odpalił");
    }
}
