package common.fileprovider;

import java.io.File;

import common.exception.FactoryException;

public class FileProviderFactoryImpl implements FileProviderFactory {

	private File destDirectory;
	
	public FileProviderFactoryImpl(File destinationDirectory){
		this.destDirectory = destinationDirectory;
	}
	
	@Override
	public FileProvider createProvider() throws FactoryException {
		if(destDirectory == null){
			throw new FactoryException("Destination directory is null");
		}
		if( !destDirectory.exists() || !destDirectory.isDirectory()){
			throw new FactoryException("Destination directory is not exist or not real directory");
		}
		return new FileProviderImpl(destDirectory);
	}

}
