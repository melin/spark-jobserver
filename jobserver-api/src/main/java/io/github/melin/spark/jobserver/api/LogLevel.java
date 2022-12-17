package io.github.melin.spark.jobserver.api;

public enum LogLevel {
    STDOUT("Stdout"), INFO("Info"), WARN("Warn"), ERROR("Error"), APPINFO("AppInfo");

    private final String type;

    LogLevel(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
