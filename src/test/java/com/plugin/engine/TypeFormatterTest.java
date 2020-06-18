package com.plugin.engine;

import com.google.googlejavaformat.java.FormatterException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class TypeFormatterTest extends FormatterTest {
    private void testType(Integer formatType, String ansPathFile) throws IOException, FormatterException {
        File copiedFile = copyFile(
                getFile("java/type_formatting/FormattingTypeSample.java"),
                "_" + getFile(ansPathFile).getName()
        );
        new JavaFormatter(formatType).formatFile(copiedFile);
        assertTrue(FileUtils.contentEquals(copiedFile, getFile(ansPathFile)));
    }

    @Test
    public void testGoogleFormattingType() throws FormatterException, IOException {
        testType(0, "java/type_formatting/GoogleFormattingType.java");
    }

    @Test
    public void testAOSPFormattingType() throws IOException, FormatterException {
        testType(1, "java/type_formatting/AOSPFormattingType.java");
    }
}
