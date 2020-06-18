package com.plugin.frontend.actions;

import com.google.googlejavaformat.java.FormatterException;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.plugin.engine.FileChanges;
import com.plugin.engine.JavaFormatter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Objects;

public class FormatFileAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        JavaFormatter javaFormatter = new JavaFormatter(0);
        VirtualFile file = null;

        try {
            Document document = (Objects.requireNonNull(e.getData(LangDataKeys.EDITOR))).getDocument();
            file = FileDocumentManager.getInstance().getFile(document);
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }


        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        int startOffset = -1;
        int endOffset = -1;

        if (file == null || editor == null) {
            return;
        }

        try {
            startOffset = editor.getSelectionModel().getSelectionStart();
            endOffset = editor.getSelectionModel().getSelectionEnd();
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }

        if (startOffset == endOffset) {
            try {
                javaFormatter.formatFile(new File(file.getPath()));
                file.refresh(false, false);
            } catch (FormatterException | IOException exception) {
                exception.printStackTrace();
            }
            return;
        }

        LineNumberReader r;
        int startLine = 0;
        int endLine = 0;
        int count = 0;

        try {
            r = new LineNumberReader(new FileReader(new File(file.getPath())));
            LineInfo start = getLineNumber(startOffset, false, r, count);
            startLine = start.getLine();
            count = start.getCount();
            endLine = getLineNumber(endOffset, true, r, count).getLine();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        Integer finalStartLine = startLine;
        Integer finalEndLine = endLine;

        try {
            javaFormatter.formatPartsOfFile(new FileChanges(new File(file.getPath()), new ArrayList<>() {{
                add(new Pair<>(finalStartLine, finalEndLine));
            }}));
        } catch (FormatterException | IOException exception) {
            exception.printStackTrace();
        }
        file.refresh(false, false);
    }

    //position 0 - start; position 1 - end
    private LineInfo getLineNumber(int offset, boolean position, LineNumberReader r, int count) throws IOException {
        int line = 0;
        boolean crutch = false;
        int codeOfSymbol = 0;

        while (count < offset && (codeOfSymbol = r.read()) != -1) {
            count++;
        }

        if (count == offset) {
            if (codeOfSymbol != 10 || !position) {
                crutch = true;
            }
            line = r.getLineNumber() + (crutch ? 1 : 0);
        }

        return new LineInfo(count, line);
    }

    private static class LineInfo{
        private final int count;
        private final int line;

        LineInfo(int count, int line) {
            this.count = count;
            this.line = line;
        }

        public int getCount() {
            return count;
        }
        public int getLine() {
            return line;
        }
    }
}
