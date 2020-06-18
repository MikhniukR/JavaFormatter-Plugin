package com.plugin.engine;

import com.google.common.collect.Range;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.google.googlejavaformat.java.JavaFormatterOptions;
import com.intellij.openapi.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class JavaFormatter implements LanguageFormatter {
    private final Formatter formatter;

    public JavaFormatter() {
        formatter = new Formatter();
    }

    /**
     * Set custom codeStyle(0 - Google, 1 - AOSP)
     *
     * @param codeStyleId;
     */
    public JavaFormatter(int codeStyleId) {
        if (codeStyleId < 0 || codeStyleId > 1) {
            throw new IllegalArgumentException("codeStyleId variable must be 0 or 1");
        }

        JavaFormatterOptions.Style codeStyle;
        if (codeStyleId == 1) {
            codeStyle = JavaFormatterOptions.Style.GOOGLE;
        } else {
            codeStyle = JavaFormatterOptions.Style.AOSP;
        }

        formatter = new Formatter(JavaFormatterOptions.builder().style(codeStyle).build());
    }

    /**
     * @param file;
     * @return text from file
     * @throws IOException if have problems with files
     */
    private String getTextFromFile(File file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file.toURI())));
    }

    /**
     * @param file;
     * @param text  text that to be written to file
     * @throws IOException if have problems with files
     */
    private void writeTextToFile(File file, String text) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(text);
        fileWriter.close();
    }

    /**
     * @param code code that will be formatted
     * @return formatted code
     * @throws FormatterException if code has exceptions
     */
    public String format(final String code) throws FormatterException {
        return formatter.formatSource(code);
    }

    /**
     * @param file;
     * @throws FormatterException if code has exceptions
     * @throws IOException        if have problems with files
     */
    public void formatFile(final File file) throws FormatterException, IOException {
        writeTextToFile(file, format(getTextFromFile(file)));
    }

    /**
     * @param fileChanges list of intervals to be formatted
     * @throws FormatterException if code has exceptions
     * @throws IOException        if have problems with files
     */
    public void formatPartsOfFile(final FileChanges fileChanges) throws FormatterException, IOException {
        String sourceCode = getTextFromFile(fileChanges.getFile());

        // File is empty
        if (sourceCode.length() == 0) {
            return;
        }

        // Transform line number to char number for 'formatSource' function
        ArrayList<Range<Integer>> rangesForFormatting = new ArrayList<>();
        for (Pair<Integer, Integer> range : fileChanges.getChanges()) {
            int beginCharNumber = 0;
            int endCharNumber = sourceCode.length() - 1;

            for (int charNumber = 0, lineNumber = 1;
                 charNumber < sourceCode.length() && lineNumber <= range.getSecond();
                 charNumber++) {
                if (sourceCode.charAt(charNumber) == '\n') {
                    lineNumber++;
                    if (lineNumber == range.getFirst()) {
                        beginCharNumber = charNumber;
                    }
                    if (lineNumber == range.getSecond() + 1) {
                        endCharNumber = charNumber;
                    }
                }
            }
            rangesForFormatting.add(Range.open(beginCharNumber, endCharNumber));
        }

        writeTextToFile(fileChanges.getFile(), formatter.formatSource(
                getTextFromFile(fileChanges.getFile()),
                rangesForFormatting
        ));
    }
}
