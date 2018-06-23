package com.eusecom.samfantozzi;

import android.content.Context;

public class CommandExecutorProxy implements CommandExecutor {

	private boolean isAdmin, isLogin, isCompany;
	private CommandExecutor executor;
	
	public CommandExecutorProxy(String usuid, String fir, String usadmin){
		if(usadmin.equals("1")) isAdmin=true;
		if(!usuid.equals("0")) isLogin=true;
        if(usuid.equals("")) isLogin=false;
		if(!fir.equals("")) isCompany=true;
        if(fir.equals("0")) isCompany=false;
		executor = new CommandExecutorImpl();
	}
	
	@Override
	public void runCommand(String perm, AccountReportsHelperFacade.DBTypes dbType
			, AccountReportsHelperFacade.ReportTypes reportType, AccountReportsHelperFacade.ReportName tableName
			, Context context) throws Exception {

		if(perm.trim().startsWith("adm")) {
			if (!isAdmin) {
				throw new Exception("adm");
			} else {
				executor.runCommand(perm, dbType, reportType, tableName, context);
			}
		}

		if(perm.trim().startsWith("lgn")) {
			if (!isLogin) {
				throw new Exception("lgn");
			} else {
                if (!isCompany) {
                    throw new Exception("cmp");
                } else {
                    executor.runCommand(perm, dbType, reportType, tableName, context);
                }
			}
		}

		if(perm.trim().startsWith("cmp")) {
			if (!isCompany) {
				throw new Exception("cmp");
			} else {
				executor.runCommand(perm, dbType, reportType, tableName, context);
			}
		}



	}

}