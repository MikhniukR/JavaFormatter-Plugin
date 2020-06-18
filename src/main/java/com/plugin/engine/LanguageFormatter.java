package com.plugin.engine;

import com.google.googlejavaformat.java.FormatterException;

import java.io.File;
import java.io.IOException;

public interface LanguageFormatter {
    String format(final String code) throws FormatterException;

    void formatFile(final File file) throws FormatterException, IOException;

    void formatPartsOfFile(final FileChanges fileChanges) throws FormatterException, IOException;
}
