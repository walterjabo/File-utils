package com.wjb.utils.Utils;

/**
 * @author Walter Jabo Bereche
 *
 */
public class App {
	
	public static void main(String[] args) {

		String sourceFolder = "/Volumes/TOSHIBA_1TB/walter/Pictures/AllFotosCamera";
		String targetFolder = "/Volumes/TOSHIBA_1TB/FolderOrganizado";

		ExifUtils.organizePhotos(sourceFolder, targetFolder);
	}
}
