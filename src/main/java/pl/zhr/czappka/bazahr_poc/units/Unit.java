package pl.zhr.czappka.bazahr_poc.units;

import org.springframework.data.annotation.Id;

class Unit {

    @Id
    Integer id;

    String unitId;

    Integer numerosity;
}
