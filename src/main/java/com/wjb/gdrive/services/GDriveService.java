package com.wjb.gdrive.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.wjb.gdrive.process.DriveAuthorization;
import com.wjb.gdrive.process.GDriveUpload;
import com.wjb.utils.Utils.StringParamUtils;

public class GDriveService {
	
	public static boolean lock = false;
	static String file = "/tmp/lock.txt";
	
	public static boolean isLocked() {
		String sCurrentLine;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.trim().startsWith("lock")) {
					return true;
				} else {
					return false;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return false;
	}
	public static void writeLock(boolean lock) {
		FileWriter br = null;
		try {
			br = new FileWriter(file);
			br.write(lock ? "lock" : "unlock");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.flush();
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {

		if (args != null && args.length > 0) {
			String path = args[0];
			System.out.println("path[" + path + "]");
			String fileName = "Upload " + StringParamUtils.formatTime("yyyy.MM.dd-HH.mm.ss.SSS", "GMT-05");
			try {
				System.out.println("Iniciando Request..." + StringParamUtils.formatTime("yyyy/MM/dd-HH:mm:ss:SSS", "GMT-05"));
				String folderId = args.length > 1 ? args[1] : null;
				
				System.out.println("El archivo se copiará en el folderId[" + folderId + "]");
				
				while (isLocked()) {
					System.out.println("Proceso bloqueado esperando 5 segundos.");
					Thread.sleep(5000);
				}
				System.out.println("Iniciando proceso. No bloqueado -> a SI bloqueado");
				writeLock(true);
				uploadFile(path, fileName, folderId);
				writeLock(false);
				System.out.print("Terminado OK... ");
			} catch (IOException e) {
				System.out.print("Terminado ERROR... " + e);
				e.printStackTrace();
			} catch (Exception e) {
				System.out.print("Terminado Exception... " + e);
				e.printStackTrace();
			} catch (Throwable e) {
				System.out.print("Terminado Throwable... " + e);
				e.printStackTrace();
			} 
			System.out.println(StringParamUtils.formatTime("yyyy/MM/dd-HH:mm:ss:SSS", "GMT-05"));
		} else {
			System.out.println("Parámetros no definidos: java -jar filejarname.jar file_to_upload [drive_folder_id]");
		}
	}

	public static void uploadFile(String strFile, String fileName, String folderId) throws IOException {
		folderId = (folderId == null || folderId.trim().isEmpty()) ? null : folderId;
//		folderId = "0BxLrz-PhMtZJflFmQS1qV0J4UXgtLVdUR0N3ejdyenphczU2NDJVYVBIRGdjRmJfTzctRFU";
		GDriveUpload.insertFile(DriveAuthorization.getDriveService(), fileName, "Original name:" + strFile, folderId, "image/jpeg", strFile);
	}

}
