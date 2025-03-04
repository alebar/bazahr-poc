package pl.zhr.czappka.bazahr_poc.memberships;

import org.springframework.stereotype.Component;
import pl.zhr.czappka.bazahr_poc.Denormalizer;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
class CandidateAcceptedMembershipDenormalizer implements Denormalizer {

    private final MembershipRepository repository;

    CandidateAcceptedMembershipDenormalizer(MembershipRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean accepts(String type) {
        return "urn:ekp:events/candidate-accepted".equals(type);
    }

    @Override
    public void denormalize(Map<String, Object> payload) {
        var unitId = (String) payload.get("unitId");
        var candidateIds = ((List<?>) payload.get("candidateIds")).
                stream().
                map(String.class::cast).
                toList();
        var now = Instant.now();
        var memberships = candidateIds.stream().
                map(id -> new Membership(now, id, unitId)).
                toList();

        repository.saveAll(memberships);
    }
}
