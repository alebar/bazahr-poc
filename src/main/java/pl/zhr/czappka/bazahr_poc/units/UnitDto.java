package pl.zhr.czappka.bazahr_poc.units;

public class UnitDto {

    String id;
    Integer numerosity;

    static UnitDto from(final Unit unit) {
        return new UnitDto(unit.unitId, unit.numerosity);
    }

    UnitDto(final String id, final Integer numerosity) {
        this.id = id;
        this.numerosity = numerosity;
    }

    public String getId() {
        return id;
    }

    public Integer getNumerosity() {
        return numerosity;
    }
}
