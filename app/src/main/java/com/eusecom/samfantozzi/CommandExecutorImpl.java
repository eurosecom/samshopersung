package com.eusecom.samfantozzi;

import android.content.Context;
import java.io.IOException;

public class CommandExecutorImpl implements CommandExecutor {

	@Override
	public void runCommand(String perm, AccountReportsHelperFacade.DBTypes dbType
			, AccountReportsHelperFacade.ReportTypes reportType, AccountReportsHelperFacade.ReportName tableName
			, Context context) throws IOException {
        //some heavy implementation
		AccountReportsHelperFacade.generateReport(dbType, reportType, tableName, context);
		System.out.println("'" + perm + "' command executed.");
	}

}