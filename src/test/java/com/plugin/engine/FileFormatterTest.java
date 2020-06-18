package com.plugin.engine;

import com.google.googlejavaformat.java.FormatterException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class FileFormatterTest extends FormatterTest {
    @Test
    public void testSamples() throws IOException, FormatterException {
        for (File file : getFile("java/samples").listFiles()) {
            if (file.getName().matches("\\d\\.java")) {
                File copiedFile = copyFile(file, "file_" + getAnswerFile(file).getName());
                new JavaFormatter(0).formatFile(copiedFile);
                if (!FileUtils.contentEquals(copiedFile, getAnswerFile(file))) {
                    System.out.println(file.getName());
                    assertTrue(false);
                }
            }
        }
        assertTrue(true);
    }
}
