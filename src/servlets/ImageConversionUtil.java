package servlets;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class ImageConversionUtil {
	
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
	
	public static BufferedImage matToBufferedImage(Mat mat)
	{
		byte[] data = new byte[mat.rows()*mat.cols()*(int)(mat.elemSize())];
		mat.get(0, 0, data);
		if (mat.channels() == 3) {
		 for (int i = 0; i < data.length; i += 3) {
		  byte temp = data[i];
		  data[i] = data[i + 2];
		  data[i + 2] = temp;
		 }
		}
		BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), BufferedImage.TYPE_3BYTE_BGR);
		image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
		return image;
	}
	
	public static Mat img2Mat(BufferedImage in, boolean rgb)
    {
          Mat out;
          byte[] data;
          int r, g, b;

          if(rgb)//in.getType() == BufferedImage.TYPE_INT_RGB) 
          {
              out = new Mat(616, 760, CvType.CV_8UC3);
              data = new byte[760 * 616 * (int)out.elemSize()];
              int[] dataBuff = in.getRGB(0, 0, 760, 616, null, 0, 760);
              for(int i = 0; i < dataBuff.length; i++)
              {
                  data[i*3] = (byte) ((dataBuff[i] >> 16) & 0xFF);
                  data[i*3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
                  data[i*3 + 2] = (byte) ((dataBuff[i] >> 0) & 0xFF);
              }
          }
          else
          {
              out = new Mat(616, 760, CvType.CV_8UC1);
              data = new byte[760 * 616 * (int)out.elemSize()];
              int[] dataBuff = in.getRGB(0, 0, 760, 616, null, 0, 760);
              for(int i = 0; i < dataBuff.length; i++)
              {
                r = (byte) ((dataBuff[i] >> 16) & 0xFF);
                g = (byte) ((dataBuff[i] >> 8) & 0xFF);
                b = (byte) ((dataBuff[i] >> 0) & 0xFF);
                data[i] = (byte)((0.21 * r) + (0.71 * g) + (0.07 * b)); //luminosity
              }
           }
           out.put(0, 0, data);
           return out;
     } 
	
	public static BufferedImage mat2Img(Mat in)
    {
        BufferedImage out;
        byte[] data = new byte[in.width() * in.height() * (int)in.elemSize() ];
        int type;
        in.get(0, 0, data);

        if(in.channels() == 1)
            type = BufferedImage.TYPE_BYTE_GRAY;
        else
            type = BufferedImage.TYPE_3BYTE_BGR;

        out = new BufferedImage(in.width(), in.height(), type);

        out.getRaster().setDataElements(0, 0, in.width(), in.height(), data);
        return out;
    } 

}
