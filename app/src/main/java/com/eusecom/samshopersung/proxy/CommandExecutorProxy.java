package com.eusecom.samshopersung.proxy;


public interface CommandExecutorProxy {

    boolean approveCommand(CommandExecutorProxyImpl.PermType perm, CommandExecutorProxyImpl.ReportTypes reportType
            , CommandExecutorProxyImpl.ReportName tableName) throws Exception;

    void setUserParams(String usuid, String fir, String usadmin);
}