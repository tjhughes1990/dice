package dice.service.common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Extract a shared object from the jar.
 */
public class ExtractSharedObject {

    /**
     * Constructor.
     */
    private ExtractSharedObject() {
    }

    /**
     * Extract a shared object placed in "./lib" relative to the jar root directory. The object name
     * must match the filename of the argument.
     *
     * @param path
     *            the path to extract to.
     *
     * @throws DiceException
     *             if the shared object could not be extracted to the file system.
     */
    public static void extract(final Path path) throws DiceException {
        final String resourceLibName = "lib/" + path.getFileName();
        final ClassLoader cl = ExtractSharedObject.class.getClassLoader();
        final InputStream is = cl.getResourceAsStream(resourceLibName);
        if (is == null) {
            throw new DiceException("Failed to get shared object from jar: " + resourceLibName);
        }

        try {
            Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException e) {
            throw new DiceException("Failed to copy shared object to $TMP.", e);
        }
    }
}
