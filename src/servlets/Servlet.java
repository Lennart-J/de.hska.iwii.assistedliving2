package servlets;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.*;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.*;
import org.opencv.photo.*;
import org.opencv.video.Video;

/**
 * Servlet implementation class Servlet
 */
@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Started");
		ImageProvider provider = new ImageProvider();
		BufferedImage img1 = provider.getImage("2015","06", "27", "04", "00");
		BufferedImage img2 = provider.getImage("2015","06", "27", "04", "05");
		BufferedImage img3 = provider.getImage("2015","06", "27", "04", "10");
		BufferedImage img4 = provider.getImage("2015","06", "27", "04", "20");

		
		Mat src1 = ImageConversionUtil.img2Mat(img1, false);
		Mat src2 = ImageConversionUtil.img2Mat(img1, false);
		Mat src3 = ImageConversionUtil.img2Mat(img3, false);
		
		
		//do stuff with mat
		Mat destination1 = new Mat(src1.rows(), src1.cols(),0);

		Video.calcOpticalFlowFarneback(src1,src3,destination1,0.5 ,3 ,5 ,3 ,5 ,1.1 ,0 );
		
		// referenzbilder: wo findet bewegung statt
		BufferedImage refImgX = getRefImage(destination1,0);
		BufferedImage refImgY = getRefImage(destination1,1);
		
		// prognosebild erstellen
		BufferedImage progSrc = removeGreyBackground(img3);
		BufferedImage prog = getProgImage(destination1, progSrc);
		// closing & opening auf prognosebild
		Mat morphMatSrc = ImageConversionUtil.img2Mat(prog, false);
		Mat morphMatDst = new Mat(morphMatSrc.rows(), morphMatSrc.cols(),0);
		Mat kernel1 = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(15,15));
		Mat kernel2 = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(10,10));
		Imgproc.morphologyEx(morphMatSrc, morphMatDst, Imgproc.MORPH_CLOSE, kernel1);
		Imgproc.morphologyEx(morphMatDst, morphMatDst, Imgproc.MORPH_OPEN, kernel2);
		
		BufferedImage newImg = ImageConversionUtil.mat2Img(morphMatDst);
		
		if (newImg != null)
		{
		JDialog dialog = new JDialog();
		ImageIcon icon = new ImageIcon(newImg);
		JLabel label = new JLabel(icon);
		dialog.add( label );
		dialog.pack();
		dialog.setVisible(true);
		
		}
		
	}

	private BufferedImage removeGreyBackground(BufferedImage src) {
		BufferedImage noBgImg = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		noBgImg = src;
		for (int x = 0; x < noBgImg.getWidth(); x++) {
			for (int y = 0; y < noBgImg.getHeight(); y++) {
				if (noBgImg.getRGB(x,y) == -9539986 || noBgImg.getRGB(x, y) == -16777216 )
				{
					noBgImg.setRGB(x, y, 0x000000);
				}
			}
		}
		return noBgImg;
	}

	private BufferedImage getProgImage(Mat dest, BufferedImage src) {
		BufferedImage progImg = new BufferedImage(dest.cols(), dest.rows(), BufferedImage.TYPE_3BYTE_BGR);
		for (int r = 0; r < dest.rows(); r++) {
			for (int c = 0; c < dest.cols(); c++) {
				double[] values = dest.get(r, c);
				int newX = (int) Math.round(values[0]) + c;
				int newY = (int) Math.round(values[1]) + r;
				if (newX >= 0 && newY >= 0 && newX < dest.cols() && newY < dest.rows())
				{
					int color = src.getRGB(c, r);
					progImg.setRGB(newX, newY, color);
				}
			} 
		}
		return progImg;
	}

	private BufferedImage getRefImage(Mat dest, int channel) {
		BufferedImage refImg = new BufferedImage(dest.cols(), dest.rows(), BufferedImage.TYPE_BYTE_GRAY);
		for (int r = 0; r < dest.rows(); r++) {
			for (int c = 0; c < dest.cols(); c++) {
				
				if (dest.get(r, c) != null){
					double[] values = dest.get(r, c);
					for (int j = 0; j < values.length; j++) {
						if (values[j] != 0.0 && values[j] < Math.pow(10.0, 5) && values[j] > 0.1)
						{
							if (j == channel)
							{
								refImg.setRGB(c, r, 0xFFFFFF);
							}
						}
					}
				}
			} 
		}

		return refImg;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}


}