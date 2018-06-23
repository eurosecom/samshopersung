package com.eusecom.samfantozzi;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;



public class AccountReportsMySqlHelper {
	
	public static SharedPreferences getMySqlDBConnection(){
		//get MySql DB connection using connection parameters
		return null;
	}
	
	public void generateMySqlPDFReport(AccountReportsHelperFacade.ReportName tableName, SharedPreferences con, Context context){
		//get data from table and generate pdf report
		System.out.println("MySqlHelper " + "PDF");

		switch(tableName) {

			case PENDEN:
				Intent is = new Intent(context, ShowPdfActivity.class);
				Bundle extras = new Bundle();
				extras.putString("fromact", "41");
				extras.putString("drhx", "41");
				extras.putString("ucex", "0");
				extras.putString("dokx", "0");
				extras.putString("icox", "0");
				is.putExtras(extras);
				context.startActivity(is);
				is.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case PENDEN2:
				Intent is2 = new Intent(context, ShowPdfActivity.class);
				Bundle extras2 = new Bundle();
				extras2.putString("fromact", "42");
				extras2.putString("drhx", "42");
				extras2.putString("ucex", "0");
				extras2.putString("dokx", "0");
				extras2.putString("icox", "0");
				is2.putExtras(extras2);
				context.startActivity(is2);
				is2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case PRIVYD:
				Intent is3 = new Intent(context, ShowPdfActivity.class);
				Bundle extras3 = new Bundle();
				extras3.putString("fromact", "43");
				extras3.putString("drhx", "43");
				extras3.putString("ucex", "0");
				extras3.putString("dokx", "0");
				extras3.putString("icox", "0");
				is3.putExtras(extras3);
				context.startActivity(is3);
				is3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case MAJZAV:
				Intent is4 = new Intent(context, ShowPdfActivity.class);
				Bundle extras4 = new Bundle();
				extras4.putString("fromact", "44");
				extras4.putString("drhx", "44");
				extras4.putString("ucex", "0");
				extras4.putString("dokx", "0");
				extras4.putString("icox", "0");
				is4.putExtras(extras4);
				context.startActivity(is4);
				is4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case FINSTA:
				Intent is5 = new Intent(context, ShowPdfActivity.class);
				Bundle extras5 = new Bundle();
				extras5.putString("fromact", "51");
				extras5.putString("drhx", "51");
				extras5.putString("ucex", "0");
				extras5.putString("dokx", "0");
				extras5.putString("icox", "0");
				is5.putExtras(extras5);
				context.startActivity(is5);
				is5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case KNIODB:
				Intent is6 = new Intent(context, ShowPdfActivity.class);
				Bundle extras6 = new Bundle();
				extras6.putString("fromact", "52");
				extras6.putString("drhx", "52");
				extras6.putString("ucex", "0");
				extras6.putString("dokx", "0");
				extras6.putString("icox", "0");
				is6.putExtras(extras6);
				context.startActivity(is6);
				is6.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case KNIDOD:
				Intent is7 = new Intent(context, ShowPdfActivity.class);
				Bundle extras7 = new Bundle();
				extras7.putString("fromact", "53");
				extras7.putString("drhx", "53");
				extras7.putString("ucex", "0");
				extras7.putString("dokx", "0");
				extras7.putString("icox", "0");
				is7.putExtras(extras7);
				context.startActivity(is7);
				is7.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;


			case UCTPOH:
				Intent is8 = new Intent(context, ShowPdfActivity.class);
				Bundle extras8 = new Bundle();
				extras8.putString("fromact", "61");
				extras8.putString("drhx", "61");
				extras8.putString("ucex", "0");
				extras8.putString("dokx", "0");
				extras8.putString("icox", "0");
				is8.putExtras(extras8);
				context.startActivity(is8);
				is8.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case DPHPRZ:
				Intent is9 = new Intent(context, ShowPdfActivity.class);
				Bundle extras9 = new Bundle();
				extras9.putString("fromact", "54");
				extras9.putString("drhx", "54");
				extras9.putString("ucex", "0");
				extras9.putString("dokx", "0");
				extras9.putString("icox", "0");
				is9.putExtras(extras9);
				context.startActivity(is9);
				is9.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case FOBPRZ:
				Intent is10 = new Intent(context, ShowPdfActivity.class);
				Bundle extras10 = new Bundle();
				extras10.putString("fromact", "55");
				extras10.putString("drhx", "55");
				extras10.putString("ucex", "0");
				extras10.putString("dokx", "0");
				extras10.putString("icox", "0");
				is10.putExtras(extras10);
				context.startActivity(is10);
				is10.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case NEXTVERSION:
				new AlertDialog.Builder(context)
						.setTitle(context.getResources().getString(R.string.unpackeditem))
						.setMessage(context.getResources().getString(R.string.innextversion))
						.setPositiveButton(context.getResources().getString(R.string.textok), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {

							}
						}).show();
				break;

			//double accounts reports

			case OBRATOV:
				Intent is101 = new Intent(context, ShowPdfActivity.class);
				Bundle extras101 = new Bundle();
				extras101.putString("fromact", "101");
				extras101.putString("drhx", "101");
				extras101.putString("ucex", "0");
				extras101.putString("dokx", "0");
				extras101.putString("icox", "0");
				is101.putExtras(extras101);
				context.startActivity(is101);
				is101.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case UDENNIK:
				Intent is102 = new Intent(context, ShowPdfActivity.class);
				Bundle extras102 = new Bundle();
				extras102.putString("fromact", "102");
				extras102.putString("drhx", "102");
				extras102.putString("ucex", "0");
				extras102.putString("dokx", "0");
				extras102.putString("icox", "0");
				is102.putExtras(extras102);
				context.startActivity(is102);
				is102.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case SUVAHA:
				Intent is103 = new Intent(context, ShowPdfActivity.class);
				Bundle extras103 = new Bundle();
				extras103.putString("fromact", "103");
				extras103.putString("drhx", "103");
				extras103.putString("ucex", "0");
				extras103.putString("dokx", "0");
				extras103.putString("icox", "0");
				is103.putExtras(extras103);
				context.startActivity(is103);
				is103.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case VYSLED:
				Intent is104 = new Intent(context, ShowPdfActivity.class);
				Bundle extras104 = new Bundle();
				extras104.putString("fromact", "104");
				extras104.putString("drhx", "104");
				extras104.putString("ucex", "0");
				extras104.putString("dokx", "0");
				extras104.putString("icox", "0");
				is104.putExtras(extras104);
				context.startActivity(is104);
				is104.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case HLKNIHA:
				Intent is105 = new Intent(context, ShowPdfActivity.class);
				Bundle extras105 = new Bundle();
				extras105.putString("fromact", "105");
				extras105.putString("drhx", "105");
				extras105.putString("ucex", "0");
				extras105.putString("dokx", "0");
				extras105.putString("icox", "0");
				is105.putExtras(extras105);
				context.startActivity(is105);
				is105.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

		}

	}
	
	public void generateMySqlHTMLReport(AccountReportsHelperFacade.ReportName tableName, SharedPreferences con){
		//get data from table and generate pdf report
		System.out.println("MySqlHelper " + "HTML");
	}

	public void generateMySqlCSVReport(AccountReportsHelperFacade.ReportName tableName, SharedPreferences con){
		//get data from table and generate csv report
		System.out.println("MySqlHelper " + "CSV");
	}

	public void generateMySqlJSONReport(AccountReportsHelperFacade.ReportName tableName, SharedPreferences con){
		//get data from table and generate json report
		System.out.println("MySqlHelper " + "JSON");
	}


}