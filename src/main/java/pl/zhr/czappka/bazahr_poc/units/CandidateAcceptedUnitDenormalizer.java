package pl.zhr.czappka.bazahr_poc.units;

import org.springframework.stereotype.Component;
import pl.zhr.czappka.bazahr_poc.Denormalizer;

import java.util.Collection;
import java.util.Map;

@Component
class CandidateAcceptedUnitDenormalizer implements Denormalizer {

    private final UnitRepository unitRepository;

    CandidateAcceptedUnitDenormalizer(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @Override
    public boolean accepts(final String type) {
        return "urn:ekp:events/candidate-accepted".equals(type);
    }

    @Override
    public void denormalize(final Map<String, Object> payload) {
        var unitId = (String) payload.get("unitId");
        var candidates = (Collection<?>) payload.get("candidateIds");

        unitRepository.findByUnitId(unitId).ifPresent(unit -> {
            unit.numerosity = unit.numerosity + candidates.size();
            unitRepository.save(unit);
        });
    }
}
