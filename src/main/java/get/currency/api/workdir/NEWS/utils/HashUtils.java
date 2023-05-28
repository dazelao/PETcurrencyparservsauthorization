package get.currency.api.workdir.NEWS.utils;

import java.time.LocalDateTime;

public class HashUtils {
    public static String generateHash(String url, LocalDateTime publishedAt) {
        String dataToHash = url + publishedAt.toString();
        return HashCalculator.calculateHash(dataToHash);
    }
}