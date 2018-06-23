package com.eusecom.samfantozzi;

import io.realm.Realm;

public class AccountReportsRealmHelper {

	public static Realm getRealmDBConnection(){
		//get Realm DB connection using connection parameters
		return null;
	}
	
	public void generateRealmPDFReport(AccountReportsHelperFacade.ReportName tableName, Realm con){
		//get data from table and generate pdf report
		System.out.println("RealmHelper " + "PDF");
	}
	
	public void generateRealmHTMLReport(AccountReportsHelperFacade.ReportName tableName, Realm con){
		//get data from table and generate pdf report
		System.out.println("RealmHelper " + "HTML");
	}

	public void generateRealmCSVReport(AccountReportsHelperFacade.ReportName tableName, Realm con){
		//get data from table and generate csv report
		System.out.println("RealmHelper " + "CSV");
	}

	public void generateRealmJSONReport(AccountReportsHelperFacade.ReportName tableName, Realm con){
		//get data from table and generate json report
		System.out.println("RealmHelper " + "JSON");
	}

	
}