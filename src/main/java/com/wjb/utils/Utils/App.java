package com.wjb.utils.Utils;

import java.io.File;
import java.io.IOException;

/**
 * @author Walter Jabo Bereche
 *
 */
public class App {
	
	public static void main(String[] args) throws IOException {

		String sourceFolder = "/Volumes/MEDIA/Nueva carpeta/Dakar 2013";
		String targetFolder = "/Volumes/MEDIA/Fotos-Familia";
	
//		Calendar cal = new GregorianCalendar();
////		cal.set(Calendar.DAY_OF_MONTH, 16);
//		System.out.println(cal.get(Calendar.DAY_OF_WEEK));
		
		File logFile = ExifUtils.setupLog(sourceFolder);
		ExifUtils.organizePhotos(sourceFolder, targetFolder, logFile);
	}
}
