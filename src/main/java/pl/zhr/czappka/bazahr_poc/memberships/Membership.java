package pl.zhr.czappka.bazahr_poc.memberships;

import org.springframework.data.annotation.Id;

import java.time.Instant;

class Membership {

    @Id
    Integer id;

    Instant createdAt;

    Membership(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
