package pl.zhr.czappka.bazahr_poc.memberships;

public class MembershipsCollectionDto {
    long count;

    MembershipsCollectionDto(long count) {
        this.count = count;
    }

    public long getCount() {
        return count;
    }
}
