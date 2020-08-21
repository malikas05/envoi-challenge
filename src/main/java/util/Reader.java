package util;

import java.util.List;

public interface Reader<T> {
    List<T> readFile(String pathToFile);
}
