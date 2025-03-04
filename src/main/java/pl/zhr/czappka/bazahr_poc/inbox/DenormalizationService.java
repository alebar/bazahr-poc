package pl.zhr.czappka.bazahr_poc.inbox;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.zhr.czappka.bazahr_poc.Denormalizer;

import java.util.List;

@Component
class DenormalizationService {

    private final List<Denormalizer> denormalizers;

    DenormalizationService(List<Denormalizer> denormalizers) {
        this.denormalizers = denormalizers;
    }

    @Transactional
    void handle(final IncomingMessage msg) {
        denormalizers.
                stream().
                filter(d -> d.accepts(msg.type)).
                forEach(d -> d.denormalize(msg.payload));
    }

}
