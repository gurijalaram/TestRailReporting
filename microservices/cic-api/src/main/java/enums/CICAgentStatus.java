package enums;

public enum CICAgentStatus {
    QUERY_IN_PROGRESS("Query In Progress"),
    FINISHED("Finished"),
    PENDING("Pending");

    private final String agentStatus;

    CICAgentStatus(String st) {
        agentStatus = st;
    }

    public String getAgentStatus() {
        return agentStatus;
    }
}
