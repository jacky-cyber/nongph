package com.felix.nsf;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * <P>
 * This class process the module correlative information
 * </P>
 * @author felix
 */
public class ModuleHandler {
	private static List<String> ALL_MODULES_NAME;
	
	public static List<String> getAllModulesName( String webPath ) {
		if( ALL_MODULES_NAME == null ) {
			List<String> result = new ArrayList<String>();
			
			File webRoot = new File( webPath );
			FileFilter ff = new DirectoryFileFilter();
			File[] directorys =  webRoot.listFiles( ff );
			
			for( File dir : directorys ) {
				boolean haveLocaleDir = false;
				boolean haveMenuDir = false;
				
				File[] childDirectorys = dir.listFiles( ff );
				for( File cdir : childDirectorys ) {
					if( cdir.getName().equals( "locale") )
						haveLocaleDir = true;
					else if ( cdir.getName().equals( "menu") )
						haveMenuDir = true;
				}
				
				if( haveLocaleDir && haveMenuDir )
					result.add( dir.getName() );
			}
			
			ALL_MODULES_NAME = result;
			ALL_MODULES_NAME.remove("public");
			ALL_MODULES_NAME.add("public");
		}
		return ALL_MODULES_NAME;
	}
	
	static class DirectoryFileFilter implements FileFilter {
		@Override
		public boolean accept(File pathname) {
			return pathname.isDirectory();
		}
	}
}
