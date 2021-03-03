package services.file_manager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;
import java.util.Locale;

import javax.swing.filechooser.FileSystemView;

import org.json.JSONArray;

public class FileManagerSource 
{
    public static JSONArray getDirContent(String path_dir) {

        JSONArray json = new JSONArray();

		Path dir = Paths.get(path_dir);

		try (
			Stream<Path> files = Files.list(dir).sorted(
    			Comparator.comparing((Path p) -> !Files.isDirectory(p))
        			.thenComparing(Comparator.naturalOrder()))
		) {

    		files.forEach(file -> {
				boolean isDirectory = file.toFile().isDirectory();
				
				String file_size = String.format(
					Locale.US, "%.2f", ( (double)file.toFile().length()/1024 )
				);

				json.put(file.toFile().toString().concat(
                    String.format(
						isDirectory ? "<path:%s>" : "<file:%s (%s KB)>", file.getFileName().toString(), file_size
					)
                ));
			});
		}
		catch(Exception e) {
			System.out.println(e);
		}	
		return json;
	}

    public static JSONArray getDrives()
	{
        JSONArray array = new JSONArray();

        File[] files = File.listRoots();
        FileSystemView fsv = FileSystemView.getFileSystemView();

		for(File file : files)
		{
			if(fsv.isFloppyDrive(file))
				continue;
			
			array.put(
				String.format("%s<drive:%s>", file.toString(), file.toString())
			);
		}
		array.put((fsv.getHomeDirectory().getAbsolutePath().concat(
            String.format("<desktop:%s>", fsv.getHomeDirectory().getName()))
        ));
		return array;
	}
}
