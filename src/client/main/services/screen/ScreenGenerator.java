package main.services.screen;

import main.resources.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
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
			// int size_w = new Double((double)image_width/2).intValue();

			BufferedImage full_image = robot.createScreenCapture(screen_rect);
			BufferedImage scaled_image = Scalr.resize(full_image, 
				Scalr.Method.QUALITY,image_width, 1);

			// BufferedImage cropped_image = Scalr.crop(scaled_image,size_w,
			// 	scaled_image.getHeight());


			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageOutputStream ios = ImageIO.createImageOutputStream(out);
			image_writer.setOutput(ios);
			IIOImage im = new IIOImage(scaled_image, null, null);
			
			iamage_param = image_writer.getDefaultWriteParam();
			iamage_param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			iamage_param.setCompressionQuality(image_quality);  // Change the quality value you prefer
			
			image_writer.write(null,im,iamage_param);

			return Utils.compressBytes(out.toByteArray());
		}
		catch(Exception exception) {
			return null;
		}
	}
}