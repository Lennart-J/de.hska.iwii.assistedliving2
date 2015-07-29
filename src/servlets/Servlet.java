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
		BufferedImage img1 = provider.getImage("2015","07", "27", "14", "30");
		BufferedImage img2 = provider.getImage("2015","07", "27", "14", "35");
		BufferedImage img3 = provider.getImage("2015","07", "27", "14", "40");

		
		Mat src1 = ImageConversionUtil.img2Mat(img1);
		Mat src2 = ImageConversionUtil.img2Mat(img1);
		Mat src3 = ImageConversionUtil.img2Mat(img3);
		
		
		//do stuff with mat
		Mat destination1 = new Mat(src1.rows(), src1.cols(),0);
		System.out.println(src1.type() + " - " + img1.getType());
		System.out.println(src1.channels() + " jo");

		Video.calcOpticalFlowFarneback(src1,src3,destination1,0.5 ,3 ,5 ,3 ,5 ,1.1 ,0 );
		
		
		
		BufferedImage refImgX = new BufferedImage(destination1.cols(), destination1.rows(), BufferedImage.TYPE_BYTE_GRAY);
		BufferedImage refImgY = new BufferedImage(destination1.cols(), destination1.rows(), BufferedImage.TYPE_BYTE_GRAY);
		int dataX[] = new int[destination1.cols() * destination1.rows()];
		int dataY[] = new int[destination1.cols() * destination1.rows()];
		for (int r = 0; r < destination1.rows(); r++) {
			for (int c = 0; c < destination1.cols(); c++) {
				
				if (destination1.get(c, r) != null){
					double[] values = destination1.get(c, r);
					for (int j = 0; j < values.length; j++) {
						if (values[j] != 0.0 && values[j] < Math.pow(10.0, 5) && values[j] > 0.1)
						{
							if (j == 0)
							{
								dataX[destination1.cols()*r+c] = 0xAAAAAA;
							}
							else
							{
								dataY[destination1.cols()*r+c] = 0xAAAAAA;
							}
						}
					}
				}
			} 
		}
//		WritableRaster raster = (WritableRaster) refImgX.getData();
//        raster.setPixels(0,0,destination1.cols(),destination1.rows(),dataX);
        refImgX.setRGB(0, 0, destination1.cols(), destination1.rows(), dataX, 0, destination1.cols());
		refImgY.setRGB(0, 0, destination1.cols(), destination1.rows(), dataY, 0, destination1.cols());
		
		BufferedImage newImg = ImageConversionUtil.mat2Img(src1);
		
		if (newImg != null)
		{
		JDialog dialog = new JDialog();
		ImageIcon icon = new ImageIcon(refImgX);
		JLabel label = new JLabel(icon);
		dialog.add( label );
		dialog.pack();
		dialog.setVisible(true);
		
		}
		else
		{
			System.out.println("NULL!!");
		}
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
		Mat m  = Mat.eye(3, 3, CvType.CV_8UC1);
	    System.out.println("m = " + m.dump());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
