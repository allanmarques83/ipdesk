package services.file_manager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.lingala.zip4j.ZipFile;

public class FileManagerZipCreator {
    ByteArrayOutputStream _BYTES_STREAM;
    ZipOutputStream _ZIP_STREAM;

    File _ROOT_DIR;

    public FileManagerZipCreator() {
        _BYTES_STREAM = new ByteArrayOutputStream();
        _ZIP_STREAM = new ZipOutputStream(_BYTES_STREAM);
        _ZIP_STREAM.setLevel(9);

        _ROOT_DIR = null;
    }

    public void addToZip(File file) 
	{
		try  {	
            this.setRootDirectory(file);	

			this.process(null, file);
		} 
		catch (IOException e)  {
			e.printStackTrace();
		} 
	}

    private void process(File parent_dir, File file_target) throws IOException, StringIndexOutOfBoundsException 
	{
		if(file_target.isDirectory())	
			this.compressDirectory(parent_dir, file_target);
		else
            this.compressFile(parent_dir, file_target);
	}

    private void compressDirectory(File parent_dir, File target_dir) throws IOException
    {
        File[] list_dir_content = target_dir.listFiles();

        if(list_dir_content.length == 0) 
		{
			String file_path = this.getPathFromEmptyDir(target_dir);

            addEmptyFolderToZip(file_path);
        }
        else
        {
            for (File file : list_dir_content) 
            {
                parent_dir = parent_dir == null ? target_dir : parent_dir;

                if (file.isFile())
                    this.compressFile(parent_dir, file);

                if (file.isDirectory())
                    this.compressDirectory(parent_dir, file);
            } 
        }
    }

    private void compressFile(File parent_dir, File target_file) throws IOException {
        if(parent_dir == null)
            parent_dir = new File(target_file.getParent());

        String zip_path = target_file.getCanonicalPath().substring(
            parent_dir.getCanonicalPath().length() + 1, 
            target_file.getCanonicalPath().length()
        );

        if(_ROOT_DIR != null)
            zip_path = String.format("%s/%s", parent_dir.getName(), zip_path);
        
        writeBytesToZip(zip_path, target_file);
    }

    private void writeBytesToZip(String zip_path, File target_file) throws IOException {
        FileInputStream file_stream = new FileInputStream(target_file);
        
        _ZIP_STREAM.putNextEntry(new ZipEntry(zip_path));

        byte[] bytes = new byte[1024];
		int length;
		while ((length = file_stream.read(bytes)) >= 0) 
		{
			_ZIP_STREAM.write(bytes, 0, length);
		}
		file_stream.close();
		_ZIP_STREAM.closeEntry();
    }

    private String getPathFromEmptyDir(File target_dir) throws IOException {
        if(_ROOT_DIR.getCanonicalPath().equals(target_dir.getCanonicalPath()))
        {
            String file_name = target_dir.getParent().substring(
                target_dir.getParent().length()-1, target_dir.getParent().length()
            );

            String path_name = file_name.equals("\\") ? "" : target_dir.getParent();

            return target_dir.getCanonicalPath().substring(
                path_name.length() + 1, target_dir.getCanonicalPath().length()
            );
        }

        String current_dir_path = target_dir.getCanonicalPath().substring(
            _ROOT_DIR.getCanonicalPath().length() + 1, target_dir.getCanonicalPath().length()
        );
        return String.format("%s/%s/*",_ROOT_DIR.getName(), current_dir_path);
    }

    private void setRootDirectory(File file) {
        if(file.isDirectory()) {
            _ROOT_DIR = file;
        }
        else {
            _ROOT_DIR = null;
        }
    }

    private void addEmptyFolderToZip(String path) throws IOException {
        _ZIP_STREAM.putNextEntry(new ZipEntry( path ));
        _ZIP_STREAM.closeEntry();
    }

    public byte[] getZipBytes() {
        try
		{
			_ZIP_STREAM.close();
			_BYTES_STREAM.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return _BYTES_STREAM.toByteArray();
    }

    public static String writeZipBytes(byte[] zip_bytes, String destination_path) {
        try {
            File file_write = new File(
                String.format("%s/ipdesk.zip", destination_path)
            );
            file_write.createNewFile();

            FileOutputStream file_out_stream = new FileOutputStream(file_write, true);
            file_out_stream.write(zip_bytes);
            file_out_stream.close();

            return null;
        }
        catch (IOException e) {
            return e.getMessage();
        }
    }

    public static boolean decompressZipFile(String destination_path) {
        try {
            String zip_file = String.format("%s/ipdesk.zip", destination_path);

            ZipFile zipFile = new ZipFile(zip_file);
            zipFile.extractAll(destination_path);
            return true;
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
