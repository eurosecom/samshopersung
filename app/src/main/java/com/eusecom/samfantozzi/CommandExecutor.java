package com.eusecom.samfantozzi;

import android.content.Context;

public interface CommandExecutor {

	public void runCommand(String perm, AccountReportsHelperFacade.DBTypes dbType
			, AccountReportsHelperFacade.ReportTypes reportType, AccountReportsHelperFacade.ReportName tableName
			, Context context) throws Exception;
}