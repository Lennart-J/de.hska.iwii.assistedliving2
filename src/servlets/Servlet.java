package servlets;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.apache.tomcat.util.codec.binary.Base64;
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
	private ImageProvider provider;
	
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Servlet() {
		super();
		this.provider = new ImageProvider();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Started");

		String bundeslandParam = request.getParameter("bundesland");
		String coordParam = request.getParameter("coordinates");
		String url1 = request.getParameter("url1");
		String url2 = request.getParameter("url2");
		String[] coords = new String[2];
		if (bundeslandParam != null && coordParam != null && url1 != null && url2 != null) {
			System.out.println("url1: " + url1);
			System.out.println("url2: " + url2);

			coords = coordParam.split("\\s*,\\s*");
			System.out.println("bundesland " + bundeslandParam + " coords: " + coords[0] + " " + coords[1]);
		}

		
		BufferedImage prog = getPrognose(url2, url1, provider);
		BufferedImage morphProg = makeOpeningAndClosing(prog);
		BufferedImage progGreyBack = replaceGreyBackground(prog, -9539986);
		progGreyBack  = addCountryBoarders(Integer.parseInt(bundeslandParam), progGreyBack);
		

		boolean raining = isRainingInNextStep(morphProg, Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
		System.out.println(raining);
		
		ByteArrayOutputStream tmp = new ByteArrayOutputStream();
	    ImageIO.write(progGreyBack, "png", tmp);
	    tmp.close();
	    
	    ByteArrayOutputStream tmp2 = new ByteArrayOutputStream();
	    ImageIO.write(morphProg, "png", tmp2);
	    tmp2.close();

	    response.setContentType("application/json");
	    //response.setHeader("Content-Length",contentLength.toString());
	    PrintWriter out = response.getWriter();
	    
	    String json = toJSON(raining, Base64.encodeBase64String(tmp.toByteArray()), Base64.encodeBase64String(tmp2.toByteArray()));//Base64.encode(tmp.toByteArray())
	    System.out.println(json);
	    out.write(json); //
	    out.close();
	}

	private BufferedImage addCountryBoarders(int id, BufferedImage src) {
		String filepath = ImageProvider.ImagePart.getImagePartById(id).getFilePathOfPng();
		String realFilepath = this.getServletContext().getRealPath(filepath);
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(realFilepath));
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return img;
	}

	private BufferedImage makeOpeningAndClosing(BufferedImage prog) {
		// closing & opening auf prognosebild
		Mat morphMatSrc = ImageConversionUtil.img2Mat(prog, false);
		Mat morphMatDst = new Mat(morphMatSrc.rows(), morphMatSrc.cols(), 0);
		Mat kernel1 = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(15, 15));
		Mat kernel2 = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(10, 10));
		Imgproc.morphologyEx(morphMatSrc, morphMatDst, Imgproc.MORPH_CLOSE, kernel1);
		Imgproc.morphologyEx(morphMatDst, morphMatDst, Imgproc.MORPH_OPEN, kernel2);

		BufferedImage morphProg = ImageConversionUtil.mat2Img(morphMatDst);
		return morphProg;
	}

	private BufferedImage getPrognose(String urlFirst, String urlNext, ImageProvider provider) {
		
		BufferedImage imgFirst = provider.getImage(urlFirst);
		BufferedImage imgNext = provider.getImage(urlNext);

		Mat srcFirst = ImageConversionUtil.img2Mat(imgFirst, false);
		Mat srcNext = ImageConversionUtil.img2Mat(imgNext, false);

		// do stuff with mat
		Mat optFlow = new Mat(srcFirst.rows(), srcFirst.cols(), 0);

		Video.calcOpticalFlowFarneback(srcFirst, srcNext, optFlow, 0.5, 3, 5, 3, 5, 1.1, 0);

		// referenzbilder: wo findet bewegung statt
		// BufferedImage refImgX = getRefImage(optFlow,0);
		// BufferedImage refImgY = getRefImage(optFlow,1);

		// prognosebild erstellen
		BufferedImage progSrc = replaceGreyBackground(imgNext, 0x000000);
		BufferedImage prog = getProgImage(optFlow, progSrc);

		return prog;
	}

	private boolean isRainingInNextStep(BufferedImage morphProg, int posX, int posY) {

		if (posX < morphProg.getWidth() && posY < morphProg.getHeight()) {
			System.out.println(morphProg.getRGB(posX, posY));
			if (morphProg.getRGB(posX, posY) != -16777216) {
				return true;
			}
		}
		return false;
	}

	private BufferedImage replaceGreyBackground(BufferedImage src, int replacement) {
		BufferedImage noBgImg = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		noBgImg = src;
		for (int x = 0; x < noBgImg.getWidth(); x++) {
			for (int y = 0; y < noBgImg.getHeight(); y++) {
				if (noBgImg.getRGB(x, y) == -9539986 || noBgImg.getRGB(x, y) == -16777216) {
					noBgImg.setRGB(x, y, replacement);
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
				if (newX >= 0 && newY >= 0 && newX < dest.cols() && newY < dest.rows()) {
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

				if (dest.get(r, c) != null) {
					double[] values = dest.get(r, c);
					for (int j = 0; j < values.length; j++) {
						if (values[j] != 0.0 && values[j] < Math.pow(10.0, 5) && values[j] > 0.1) {
							if (j == channel) {
								refImg.setRGB(c, r, 0xFFFFFF);
							}
						}
					}
				}
			}
		}

		return refImg;
	}
	
	private String toJSON(boolean bool, String str1, String str2) {
		return "{\"raining\": " + bool + ", \"prog\": \""+ str1 +"\", \"morphProg\": \""+ str2 + "\" }";
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}