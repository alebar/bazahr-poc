package pl.zhr.czappka.bazahr_poc.units;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
class UnitController {

    private final UnitRepository unitRepository;

    UnitController(final UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @GetMapping("/v1/units/x")
    UnitDto readUnit() {
        return this.unitRepository.findByUnitId("urn:ekp:units/1a").
                map(UnitDto::from).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
