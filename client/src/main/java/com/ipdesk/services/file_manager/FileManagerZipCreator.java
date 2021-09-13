package services.file_manager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import resources.Utils;

public class FileManagerZipCreator {

    public static void addToZipp(ZipFile zipFile, File file) {
        try {
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setIncludeRootFolder(false);

            if(file.isDirectory())
                zipFile.addFolder(file, zipParameters);
            else
                zipFile.addFile(file, zipParameters);
        } catch (IOException e) {
            Utils.Error(e.getMessage());
        }
    }

    public static byte[] generateZipFile(List<File> files) {
        try {
            Files.deleteIfExists(
                Paths.get("ipdesk.zip")
            );

            ZipFile zipFile = new ZipFile("ipdesk.zip");
            zipFile.setCharset(StandardCharsets.UTF_8);
            
            files.stream().forEach(file -> FileManagerZipCreator.addToZipp(zipFile, file));
    
            return Files.readAllBytes(zipFile.getFile().toPath());
        } catch (IOException e) {
            Utils.Error(e.getMessage());
            return null;
        }
    }

    public static boolean decompressZipFile(String destination_path) {
        try {
            String zip_file = String.format("%s/ipdesk.zip", destination_path);

            byte[] file_bytes = Files.readAllBytes(Paths.get(zip_file));

            ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(file_bytes));
            ZipEntry ze = zis.getNextEntry();
            byte[] buffer = new byte[1024];

            while(ze != null)
			{
                String fileName = ze.getName();

				File newFile = new File(destination_path + File.separator + fileName);

                String lastFileNameChar = fileName.substring(fileName.length()-1,fileName.length());

                if( lastFileNameChar.equals("/") ) {
					new File(
                        destination_path + File.separator + fileName.substring(0,fileName.length()-1) 
                    ).mkdirs();
                }
                else 
				{				
					new File(newFile.getParent()).mkdirs();
				
					FileOutputStream fos = new FileOutputStream(newFile);
					int len;
				
					while ((len = zis.read(buffer)) > 0)
						fos.write(buffer, 0, len);
					
                    closeFileStream(fos);
				}
                ze = zis.getNextEntry();
            }

            return deleteFileZip(zip_file);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
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

    public static void closeFileStream(FileOutputStream fos)
    {
        try {
            if(fos != null)
                fos.close();
        }
        catch(IOException ex) {
		   ex.printStackTrace();
		}
    }

    public static boolean deleteFileZip(String file_zip) {
        try {
            return Files.deleteIfExists(new File(file_zip).toPath());
        }
        catch(IOException ex) {
		   ex.printStackTrace();
           return false;
		}
    }
}
