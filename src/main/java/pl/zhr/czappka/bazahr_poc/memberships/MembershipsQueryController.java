package pl.zhr.czappka.bazahr_poc.memberships;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class MembershipsQueryController {

    private final MembershipRepository repository;

    MembershipsQueryController(final MembershipRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/v1/memberships")
    MembershipsCollectionDto readMembershipsCollection() {
        return new MembershipsCollectionDto(this.repository.count());
    }

}
