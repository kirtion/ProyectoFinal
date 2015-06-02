package com.example.connecttest;


public class AjustesManager {

	private static AjustesManager ajustesManeger = new AjustesManager();
	private String ip;
	private String puerto;
	private String mac;
	private String nombre;
	private NXTComunicacion nt = new NXTComunicacion();

	private AjustesManager (){
		this.mac = "";
	}

	public static AjustesManager getInstance(){
		return ajustesManeger;
	}

	public void setnt(NXTComunicacion nt){
		this.nt = nt;
	}
	
	public NXTComunicacion getnt(){
		return nt;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPuerto() {
		return puerto;
	}

	public void setPuerto(String puerto) {
		this.puerto = puerto;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


}