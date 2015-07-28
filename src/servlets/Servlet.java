package servlets;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
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
		System.out.println(destination1.type());
		System.out.println(src1.channels() + " jo");

		Video.calcOpticalFlowFarneback(src1,src2,destination1,0.5 ,1 ,1 ,1 ,7 ,1.5 ,1 );
		
		System.out.println(destination1.type());
		
		BufferedImage newImg = ImageConversionUtil.mat2Img(src1);
		
		if (newImg != null)
		{
		JDialog dialog = new JDialog();
		ImageIcon icon = new ImageIcon(newImg);
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
