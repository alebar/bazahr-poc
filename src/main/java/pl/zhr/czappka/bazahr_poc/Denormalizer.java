package pl.zhr.czappka.bazahr_poc;

import java.util.Map;

public interface Denormalizer {

    boolean accepts(String type);

    void denormalize(Map<String, Object> payload);

}
