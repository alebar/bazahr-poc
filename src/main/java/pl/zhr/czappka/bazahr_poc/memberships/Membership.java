package pl.zhr.czappka.bazahr_poc.memberships;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.Instant;
import java.util.UUID;

@Entity
class Membership {

    @Id
    @GeneratedValue
    UUID id;

    Instant createdAt;
}
