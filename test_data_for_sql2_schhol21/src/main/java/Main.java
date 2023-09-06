import models.Peer;
import services.ImportFromCsv;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String peerFileName = getResourceFilePath("peers.csv");
        List<Peer> peerList = ImportFromCsv.importFromFile(peerFileName, Peer.class);
        System.out.println(peerList);

    }

    public static String getResourceFilePath(String resourceName) throws URISyntaxException {
        ClassLoader classLoader = Main.class.getClassLoader();
        URL resourceUrl = classLoader.getResource(resourceName);

        if (resourceUrl != null) {
            return Paths.get(resourceUrl.toURI()).toAbsolutePath().toString();
        } else {
            throw new IllegalArgumentException("Resource not found: " + resourceName);
        }
    }
}

// todo Мини-генератор CSV для тасок