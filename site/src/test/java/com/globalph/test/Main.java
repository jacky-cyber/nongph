package com.globalph.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Main {
	
	private static final String SQL = "INSERT INTO `CMS_STATIC_ASSET` (`ALT_TEXT`,`CREATED_BY`,`DATE_CREATED`,`DATE_UPDATED`,`UPDATED_BY`,`FILE_EXTENSION`,`FILE_SIZE`,`FULL_URL`,`MIME_TYPE`,`NAME`,`STORAGE_TYPE`,`TITLE`) VALUES (NULL,NULL,NULL,NULL,NULL,NULL,NULL,'/img/pear/%s','image/jpg','%s','FILESYSTEM',NULL);\n";
	
	public static void main(String[] args) throws IOException {
		File d = new File("/home/fwu/workspace/globalph/globalph/core/src/main/resources/cms/static/img/pear");
		File[] files = d.listFiles();
		FileOutputStream fos = FileUtils.openOutputStream( new File("/home/fwu/image.sql") );
		for( File sf : files ) {
			if( sf.isDirectory() ) {
				File[] imageFiles = sf.listFiles();
				for( File imageFile : imageFiles ) {
					fos.write( String.format( SQL, sf.getName() + "/" + imageFile.getName(), sf.getName()+"-"+imageFile.getName().substring(0, imageFile.getName().length()-4) ).getBytes() );
				}
			}
		}
		
		fos.close();
	}

}
