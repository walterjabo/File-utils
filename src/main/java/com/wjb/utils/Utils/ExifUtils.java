package com.wjb.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class ExifUtils {

	static int DATE_TIME_ORIGINAL_TAKEN_PHOTO = 36867;
	
	static String[] months = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
	
	public static void organizePhotos(String pathSourceFolder, String pathTargetFolder)  {
		
		File sourceFolder = new File(pathSourceFolder);
		
		if(!sourceFolder.exists()) {
			System.out.println(String.format("El folder origen [%s] no existe.", pathSourceFolder));
			return;
		}
		
		File files[] = sourceFolder.listFiles();
		
		int i = 0;
		for(File f: files) {
			i++;
			if(f.isDirectory()) {
				continue;
			}
			Date date = null;
			try {
				System.out.println(f.getAbsolutePath());
				Metadata data = ImageMetadataReader.readMetadata(f);
				
				if (data != null) {
					Directory directory = data.getDirectory(ExifSubIFDDirectory.class);
					date = directory != null ? directory.getDate(DATE_TIME_ORIGINAL_TAKEN_PHOTO) : null;
				} 
			} catch(ImageProcessingException | IOException e) {
				System.out.println("No se pudo obtener la fecha por exif.");
			}
			if (date == null) {
				Path objPath = Paths.get(f.getAbsolutePath());
				BasicFileAttributes attributes;
				try {
					attributes = java.nio.file.Files.readAttributes(objPath, BasicFileAttributes.class);
					FileTime creationTime = attributes.creationTime();
					date = new Date(creationTime.toMillis());
				} catch (IOException e1) {
					e1.printStackTrace();
					continue;
				}
			}
					
			if (date != null) {
				Calendar cal = GregorianCalendar.getInstance();
				cal.setTime(date);
				int month = cal.get(Calendar.MONTH) + 1;
				String strMonth = month < 10 ? ("0" + month) : "" + month; 
				
				int day = cal.get(Calendar.DAY_OF_MONTH);
				String strDay = day < 10 ? ("0" + day) : "" + day;
				
				String folder = String.format("/%d/%s_%s/%s/", cal.get(Calendar.YEAR), strMonth, months[month-1], strDay);
				
				String newFileName = pathTargetFolder + folder + f.getName();
				
				File targetFolder = new File(pathTargetFolder + folder);
				if (!targetFolder.exists()) {
					targetFolder.mkdirs();
				}
				
				try {
					File newFile = new File(newFileName);
					if(!newFile.exists()) {
						Files.move(Paths.get(f.getAbsolutePath()), Paths.get(newFile.getAbsolutePath()), StandardCopyOption.COPY_ATTRIBUTES);
						System.out.println(i + " de " + files.length + ", " + date + " -> " + newFileName + "... Done...");
					} else {
						System.out.println(i + " de " + files.length + ", " + newFileName + "... ALREADY EXISTS AND DIDN'T WAS MOVED!...");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void showFutureNameOfDates(String strFilePath) {
		
		File f = new File(strFilePath);
		
		try {
			Metadata data = ImageMetadataReader.readMetadata(f);
			
			Directory directory = data.getDirectory(ExifSubIFDDirectory.class);
			Date date = directory.getDate(DATE_TIME_ORIGINAL_TAKEN_PHOTO);
			
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(date);
			int month = cal.get(Calendar.MONTH) + 1;
			String strMonth = month < 10 ? ("0" + month) : "" + month; 
			String folder = String.format("/%d/%s_%s/%d/", cal.get(Calendar.YEAR), strMonth, months[month-1], cal.get(Calendar.DAY_OF_MONTH));
			
			System.out.println(date + " -> " + folder);
			
		} catch (ImageProcessingException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void showsFileMetadata(String strFilePath) {
		
		File f = new File(strFilePath);
		try {
			Metadata data = ImageMetadataReader.readMetadata(f);
			
			for(Directory dir: data.getDirectories()) {
				System.out.println(String.format("Name[%s], Class[%s]", dir.getName(), dir.getClass()));
				for(Tag tag: dir.getTags()) {
					System.out.println(String.format("\t[%s]=[%s], Type[%s], TypeHex[%s]", tag.getTagName(), tag.getDescription(), tag.getTagType(), tag.getTagTypeHex()));
				}
			}
			
		} catch (ImageProcessingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
