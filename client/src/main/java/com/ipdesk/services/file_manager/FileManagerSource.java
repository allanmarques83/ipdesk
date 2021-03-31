package services.file_manager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import java.util.Locale;

import javax.swing.filechooser.FileSystemView;

import org.json.JSONArray;

import gui.file_manager.FileManager;
import remote.ServerConnection;
import resources.Utils;

public class FileManagerSource 
{
	FileManager _FILE_MANAGER;
	FileManagerSender _TRANSFER_FILE;

	ServerConnection _SERVER_CONNECTION;

	String _REMOTE_ID;

	public FileManagerSource(ServerConnection server_connection) {
		_SERVER_CONNECTION = server_connection;
		_FILE_MANAGER = new FileManager(action -> processEventState(action));
		_TRANSFER_FILE = new FileManagerSender(_SERVER_CONNECTION);
	}

	private void processEventState(String action) {
		String event = Utils.getExpression("<(.*?):(.*?)>", action, 1);

		switch (event) {
			case "GET_CONTROLLED_DIRECTORY_CONTENT":
				getControlledDirectoryContent(
					Utils.getExpression("<GET_CONTROLLED_DIRECTORY_CONTENT:(.*?)>", action, 1)
				);
				break;
			case "FILE_TRANSFER_CONTROLLER":
				uploadFileContent();
				break;
			default:
				break;
		}
	}

	private void getControlledDirectoryContent(String directory) {
		_SERVER_CONNECTION._OUTCOMING_USER_ACTION.getControledUserDirectory(
			_REMOTE_ID, directory
		);
	}

	public boolean uploadFileContent() {
		if(!_FILE_MANAGER._TREES.hasSelectionInBothTrees()) {
			return Utils.Error("Please, select in both trees!");
		}
		String destination_path = _FILE_MANAGER._TREES.getSelectDestinationPath(
			"_TREE_CONTROLLED"
		);
		List<File> content_to_upload = _FILE_MANAGER._TREES.getSelectedNodes(
			"_TREE_CONTROLLER"
		);
		
		FileManagerZipCreator zip_creator = new FileManagerZipCreator();
		content_to_upload.stream().forEach(zip_creator::addToZip);
		
		byte[] zip_bytes = zip_creator.getZipBytes();

		_TRANSFER_FILE.sendZipContent(zip_bytes, destination_path, _REMOTE_ID);
		return true;
	}

	public void setVisible(boolean visible, String remote_id) {
		_REMOTE_ID = remote_id;
		_FILE_MANAGER.defVisible(visible);
	}

	public void setControledUserDrives(JSONArray drives) {
		_FILE_MANAGER._TREES.fillTreeWithDrives("_TREE_CONTROLLED", drives);
	}

	public void setControledUserDirectory(JSONArray directory) {
		_FILE_MANAGER._TREES.fillNodeTreeWithContent("_TREE_CONTROLLED", directory);
	}

    public static JSONArray getDirContent(String path_dir) 
	{
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
						isDirectory ? "<path:%s>" : "<file:%s (%s KB)>",
							file.getFileName().toString(), file_size
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
