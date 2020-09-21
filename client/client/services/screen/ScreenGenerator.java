package client.services.screen;

import client.services.screen.Scalr;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.zip.Deflater;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageWriter;
import javax.imageio.ImageIO;
import javax.imageio.IIOImage;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.ImageWriteParam;

public class ScreenGenerator
{
	Robot robot;
	Rectangle screen_rect;
	ImageWriter image_writer;
	ImageWriteParam iamage_param;

	public ScreenGenerator() {
		try {
			robot = new Robot();
			screen_rect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			image_writer = (ImageWriter) ImageIO.getImageWritersByFormatName("jpg").next();

		} catch (Exception exception) {
            exception.printStackTrace();
        }
	}

	public byte[] getCompressBytesScreen(int image_width, float image_quality) {
		try {	
			BufferedImage full_image = robot.createScreenCapture(screen_rect);
			BufferedImage scaled_image = Scalr.resize(full_image, 
				Scalr.Method.QUALITY,image_width, 1);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageOutputStream ios = ImageIO.createImageOutputStream(out);
			image_writer.setOutput(ios);
			IIOImage im = new IIOImage(scaled_image, null, null);
			
			iamage_param = image_writer.getDefaultWriteParam();
			iamage_param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			iamage_param.setCompressionQuality(image_quality);  // Change the quality value you prefer
			
			image_writer.write(null,im,iamage_param);

			return compress(out.toByteArray());
		}
		catch(Exception exception) {
			return null;
		}
	}

	private byte[] compress(byte[] image) {
		try {
			Deflater deflater = new Deflater();
			deflater.setLevel(Deflater.BEST_COMPRESSION);
			deflater.setInput(image); 
			deflater.finish();  
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(image.length);
			
			byte[] buffer = new byte[1024];   

			while (!deflater.finished())  {  
				int count = deflater.deflate(buffer); // returns the generated code... index  
				outputStream.write(buffer, 0, count);   
			}  
			outputStream.close();  
			return outputStream.toByteArray();  	
		}
		catch(Exception exception) {
			return null;
		}
	}
}