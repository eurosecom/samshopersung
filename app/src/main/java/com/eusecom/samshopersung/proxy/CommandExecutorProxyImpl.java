package com.eusecom.samshopersung.proxy;

public class CommandExecutorProxyImpl implements CommandExecutorProxy {

    private boolean isAdmin, isLogin, isCompany;
    private boolean approved = false;

    public enum PermType {
        ADM, LGN, CMP;
    }

    public enum ReportTypes {
        HTML, PDF, CSV, JSON, XML;
    }

    public enum ReportName {
        ORDER, INVOICE;
    }

    public CommandExecutorProxyImpl() {

    }

    public void setUserParams(String usuid, String fir, String usadmin) {
        if (usadmin.equals("1")) isAdmin = true;
        if (!usuid.equals("0")) isLogin = true;
        if (usuid.equals("")) isLogin = false;
        if (!fir.equals("")) isCompany = true;
        if (fir.equals("0")) isCompany = false;
    }
    @Override
    public boolean approveCommand(PermType perm, ReportTypes reportType,
                           ReportName tableName) throws Exception {

        if (perm.equals(PermType.ADM)) {
            if (!isAdmin) {
                throw new Exception(PermType.ADM.toString());
            } else {
                approved = true;
            }
        }

        if (perm.equals(PermType.LGN)) {
            if (!isLogin) {
                throw new Exception(PermType.LGN.toString());
            } else {
                if (!isCompany) {
                    throw new Exception(PermType.CMP.toString());
                } else {
                    approved = true;
                }
            }
        }

        if (perm.equals(PermType.CMP)) {
            if (!isCompany) {
                throw new Exception(PermType.CMP.toString());
            } else {
                approved = true;
            }
        }


        return approved;
    }

}