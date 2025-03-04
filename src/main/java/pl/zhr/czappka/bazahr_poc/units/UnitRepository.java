package pl.zhr.czappka.bazahr_poc.units;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

interface UnitRepository extends CrudRepository<Unit, Integer> {

    Optional<Unit> findByUnitId(final String unitId);

}
