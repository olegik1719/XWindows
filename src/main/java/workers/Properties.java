package workers;

import java.io.IOException;
import java.io.InputStream;

public class Properties extends java.util.Properties {
    public Properties load(String path) {
        try (InputStream resourceAsStream =
                     getClass().getResourceAsStream(path)) {
            load(resourceAsStream);
        } catch (IOException ignored) {
            // TODO: 2018/01/11 log???
            ignored.printStackTrace();
            System.exit(1);
        }
        return this;
    }
}
