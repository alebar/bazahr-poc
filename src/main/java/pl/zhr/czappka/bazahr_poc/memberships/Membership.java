package pl.zhr.czappka.bazahr_poc.memberships;

import org.springframework.data.annotation.Id;

import java.time.Instant;

class Membership {

    @Id
    Integer id;

    Instant createdAt;

    String personId;

    String unitId;

    Membership(
            final Instant createdAt,
            final String personId,
            final String unitId) {
        this.createdAt = createdAt;
        this.personId = personId;
        this.unitId = unitId;
    }
}
