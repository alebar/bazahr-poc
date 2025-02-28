package pl.zhr.czappka.bazahr_poc.memberships;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
class MembershipsQueryController {

    private final MembershipRepository repository;

    MembershipsQueryController(MembershipRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/v1/memberships/")
    void crateMembership() {
        var m = new Membership();
        m.createdAt = Instant.now();
        this.repository.save(m);
    }

    @GetMapping("/v1/memberships")
    MembershipsCollectionDto readMembershipsCollection() {
        return new MembershipsCollectionDto(this.repository.count());
    }

}
