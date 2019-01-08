package tin.p2p.utils;

import java.io.File;

public class Properties {
    private static File workspaceDirectory;

    public static File getWorkspaceDirectory() {
        return workspaceDirectory;
    }

    public static void setWorkspaceDirectory(File workspaceDirectory) {
        Properties.workspaceDirectory = workspaceDirectory;
    }
}

