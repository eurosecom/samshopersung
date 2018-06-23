package com.eusecom.samfantozzi;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;



@SuppressWarnings("deprecation")
public class SettingsActivity extends android.preference.PreferenceActivity {
	

	public static final String SERVER_NAME = "servername";
	public static final String USER_PSW = "userpsw";
	public static final String USER_NAME = "username";
	public static final String USER_UID = "usuid";
	public static final String FIR = "fir";
	public static final String FIRNAZ = "firnaz";
	public static final String FIRIBAN = "firiban";
	public static final String USER_TYPE = "ustype";
	public static final String USER_ICO = "usico";
	public static final String USER_OSC = "usosc";
	public static final String USER_ATW = "usatw";
	public static final String USNAME = "usname";
	public static final String ROK = "rok";
	public static final String UME = "ume";
	public static final String USER_ADMIN = "usadmin";

	//settings mysql parameters of accounting
	public static final String FIRDUCT = "firduct";
	public static final String FIRDPH = "firdph";
	public static final String FIRDPH1 = "firdph1";
	public static final String FIRDPH2 = "firdph2";
	public static final String POKLUCE = "pokluce";
	public static final String DRUPOH = "drupoh";
	public static final String POKLDOK = "pokldok";
	public static final String POKLDOV = "pokldov";
	public static final String BANKUCE = "bankuce";
	public static final String BANKDOK = "bankdok";
	public static final String DODUCE = "doduce";
	public static final String DODDOK = "doddok";
	public static final String ODBUCE = "odbuce";
	public static final String ODBDOK = "odbdok";
	public static final String GENUCE = "genuce";
	public static final String GENDOK = "gendok";
	public static final String NEWDOK = "newdok";
	public static final String EDIDOK = "edidok";

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		addPreferencesFromResource(R.xml.settings);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		String usadmin = SettingsActivity.getUsAdmin(this);
		if (usadmin.equals("1")) {
			findPreference("ustype").setEnabled(true);
			findPreference("usadmin").setEnabled(true);
			findPreference("usosc").setEnabled(true);
			findPreference("usico").setEnabled(true);
			findPreference("usuid").setEnabled(true);
			findPreference("ume").setEnabled(true);
		}
	}

	
	public static String getServerName(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(SERVER_NAME, "");
	}


	public static String getFir(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(FIR, "0");
	}

	public static String getFirnaz(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(FIRNAZ, "");
	}

	public static String getFiriban(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(FIRIBAN, "");
	}

	public static String getUme(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(UME, "1.2017");
	}


	public static String getUserName(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(USER_NAME, "");
	}

	public static String getUserUid(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(USER_UID, "");
	}
	
	public static String getUserPsw(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(USER_PSW, "");
	}

	public static String getUsType(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(USER_TYPE,"0");
	}

	public static String getUsAdmin(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(USER_ADMIN,"0");
	}

	public static String getUsIco(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(USER_ICO,"0");
	}

	public static String getUsOsc(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(USER_OSC,"0");
	}

	public static String getUsAtw(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(USER_ATW,"0");
	}

	public static String getUsname(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(USNAME,"0");
	}

	public static String getRok(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(ROK,"2017");
	}

	//settings mysql parameters of accounting

	public static String getFirduct(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(FIRDUCT, "0");
	}

	public static String getFirdph(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(FIRDPH, "0");
	}

	public static String getFirdph1(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(FIRDPH1, "10");
	}

	public static String getFirdph2(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(FIRDPH2, "20");
	}

	public static String getPokldov(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(POKLDOV, "1");
	}

	public static String getPokldok(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(POKLDOK, "1");
	}

	public static String getPokluce(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(POKLUCE, "0");
	}

	public static String getDrupoh(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(DRUPOH, "1");
	}

	public static String getBankdok(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(BANKDOK, "1");
	}

	public static String getBankuce(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(BANKUCE, "0");
	}

	public static String getOdbdok(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(ODBDOK, "1");
	}

	public static String getOdbuce(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(ODBUCE, "0");
	}

	public static String getGendok(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(GENDOK, "1");
	}

	public static String getGenuce(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(GENUCE, "0");
	}

	public static String getDoddok(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(DODDOK, "1");
	}

	public static String getDoduce(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(DODUCE, "0");
	}

	public static String getNewdok(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(NEWDOK, "0");
	}

	public static String getEdidok(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(EDIDOK, "0");
	}

} 