package com.plugin.engine;

import com.intellij.openapi.util.Pair;

import java.io.File;
import java.util.ArrayList;

/**
 * Class that stores file and list of intervals to be formatted
 */
public class FileChanges {
    private final File file;
    private final ArrayList<Pair<Integer, Integer>> changes;

    public FileChanges(File file, ArrayList<Pair<Integer, Integer>> changes) {
        this.file = file;
        this.changes = changes;
    }

    public File getFile() {
        return file;
    }

    public ArrayList<Pair<Integer, Integer>> getChanges() {
        return changes;
    }
}
