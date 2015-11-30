package com.wjb.gdrive.params;

import java.io.File;

public class LocateResources {

	public static String getClientSecretPath() {
		
		return new File(".").getAbsolutePath() + File.separator + "resources"
				+ File.separator + "client_secret.json";
		
	}
	
	public static void main(String[] args) {
		
		System.out.println(new File(getClientSecretPath()).length());
	}
	
}
