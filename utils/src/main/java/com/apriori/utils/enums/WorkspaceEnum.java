package com.apriori.utils.enums;

/**
 * @author cfrith
 */

public enum WorkspaceEnum {

    PRIVATE("Private Workspace"),
    PUBLIC("Public Workspace"),
    PRIVATE_API("PRIVATE"),
    PUBLIC_API("PUBLIC"),
    RECENT("Recent"),
    COMPARISONS("Comparisons"),
    FILTERED_VIEW("Filtered View");

    private final String workspace;

    WorkspaceEnum(String workspace) {
        this.workspace = workspace;
    }

    public String getWorkspace() {
        return this.workspace;
    }
}
