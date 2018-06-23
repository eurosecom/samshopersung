package com.eusecom.samfantozzi.models;

public class BankItem extends AccountItem {

	private String drh;
	private String cpl;
	private String dok;
	private String dat;
	private String ucm;
	private String ucd;
	private String rdp;
	private String ico;
	private String nai;
	private String fak;
	private String hod;
	private String pop;
	private String bal;
	
	public BankItem(String drh, String cpl, String dok, String dat, String ucm, String ucd,
			String rdp, String ico, String nai, String fak, String hod, String pop, String bal){
		this.drh=drh;
		this.cpl=cpl;
		this.dok=dok;
		this.dat=dat;
		this.ucm=ucm;
		this.ucd=ucd;
		this.rdp=rdp;
		this.ico=ico;
		this.nai=nai;
		this.fak=fak;
		this.hod=hod;
		this.pop=pop;
		this.bal=bal;
	}

	@Override
	public String getDrh() {
		return drh;
	}

	@Override
	public String getCpl() {
		return cpl;
	}

	@Override
	public String getDok() {
		return dok;
	}

	@Override
	public String getDat() {
		return dat;
	}

	@Override
	public String getUcm() {
		return ucm;
	}

	@Override
	public String getUcd() {
		return ucd;
	}

	@Override
	public String getRdp() {
		return rdp;
	}

	@Override
	public String getIco() {
		return ico;
	}

	@Override
	public String getNai() {
		return nai;
	}

	@Override
	public String getFak() {
		return fak;
	}

	@Override
	public String getHod() {
		return hod;
	}

	@Override
	public String getPop() {
		return pop;
	}

	@Override
	public String getBal() {
		return bal;
	}
}