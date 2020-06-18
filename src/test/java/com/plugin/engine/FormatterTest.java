package com.plugin.engine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public abstract class FormatterTest {
    protected File copyFile(final File sourceFile, String newFileName) throws IOException {
        Path copied = Paths.get(sourceFile.getParent(), newFileName);
        Files.copy(sourceFile.toPath(), copied, StandardCopyOption.REPLACE_EXISTING);
        return copied.toFile();
    }

    protected File getFile(String url) {
        return new File(ClassLoader.getSystemClassLoader().getResource(url).getFile());
    }

    protected File getAnswerFile(File file) {
        String[] splittedName = file.getName().split("\\.");
        return Paths.get(
                file.getParent(),
                splittedName[0] + "Ans." + splittedName[1]
        ).toFile();
    }
}
