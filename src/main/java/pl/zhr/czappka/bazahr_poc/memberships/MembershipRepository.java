package pl.zhr.czappka.bazahr_poc.memberships;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

interface MembershipRepository extends CrudRepository<Membership, UUID> {
}
