package framework.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestStatistics {
    @JsonProperty("success_tests_count")
    private int successTestsCount = 0;

    @JsonProperty("failed_tests_count")
    private int failedTestsCount = 0;

    @JsonProperty("all_tests_count")
    private int allTestsCount = 0;

    public void increaseSuccessTestsCount() {
        successTestsCount += 1;
        allTestsCount += 1;
    }

    public void increaseFailedTestsCount() {
        failedTestsCount += 1;
        allTestsCount += 1;
    }

    public int getSuccessTestsCount() {
        return successTestsCount;
    }

    public int getFailedTestsCount() {
        return failedTestsCount;
    }

    public int allTestsCount() {
        return allTestsCount;
    }
}
