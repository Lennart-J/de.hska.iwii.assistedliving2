package servlets;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class ImageProvider {
	
	private static final String user = "basi1015";
	private static final String pw = "";
	
	public enum ImagePart { 
		Deutschland(2), BW(37), Bayern(38), Berlin(39), Brandenburg(40), Bremen(41), Hamburg(42), Hessen(43), MecklenburgVorpommern(44),
		Niedersachsen(45), NordrheinWestfalen(46), RheinlandPfalz(47), Saarland(48), Sachsen(49), Sachsenanhalt(50), SchleswigHolstein(51), 
		Thueringen(52);
	
	private final int id;

    private ImagePart(int id) {
        this.id = id;
    	}
	
	}
	
	
	public ImageProvider()
	{
		Authenticator.setDefault(
				   new Authenticator() {
				      public PasswordAuthentication getPasswordAuthentication() {
				         return new PasswordAuthentication(
				        		 user, pw.toCharArray());
				      }
				   });
		
		System.setProperty("http.proxyHost", "proxy.hs-karlsruhe.de");
		System.setProperty("http.proxyPort", "8888");	
		System.setProperty("https.proxyHost", "proxy.hs-karlsruhe.de");
		System.setProperty("https.proxyPort", "8888");
	}
	
	public BufferedImage getImage(String year, String month, String day, String hour, String minute)
	{
		return getImage(year,month,day,hour,minute, ImagePart.BW);
	}
	
	/**
	 * 
	 * @param year 4 digits!
	 * @param month 2 digits!
	 * @param day 2 digits!
	 * @param hour allways 2 digits!
	 * @param minute in 5 min steps and allways 2 digits!
	 * @param part
	 * @return
	 */
	public BufferedImage getImage(String year, String month, String day, String hour, String minute, ImagePart part)
	{		
		BufferedImage image = null;
		URL url;
		try {
			url = new URL("http://kachelmannwetter.com/images/data/cache/px250/px250_"+ year +"_"+ month +"_" + day + "_" + part.id + "_" + hour + minute +".png"); // 
			image = ImageIO.read(url);
			BufferedImage convertedImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		    convertedImg.getGraphics().drawImage(image, 0, 0, null);
			return convertedImg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

