package servlets;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class ImageProvider {
	
	public enum ImagePart { 
		Deutschland(2, "deutschland"), BW(37,"bwb"), Bayern(38,"bayern"), Berlin(39,"berlin"), Brandenburg(40,"brandenburg"), Bremen(41,"bremen"), Hamburg(42,"hamburg"), Hessen(43,"hessen"), MecklenburgVorpommern(44,"mecklenburg-vorpommern"),
		Niedersachsen(45, "niedersachsen"), NordrheinWestfalen(46, "nrw"), RheinlandPfalz(47, "reihnland-pfalt"), Saarland(48,"saarland"), Sachsen(49,"sachsen"), Sachsenanhalt(50,"sachsenanhalt"), SchleswigHolstein(51,"schleswigholstein"), 
		Thueringen(52,"thüringen");
	
	private final int id;
	private final String filename;

    private ImagePart(int id, String filename) {
        this.id = id;
        this.filename = filename;
    	}
    
    public String getFilename()
    {
    	return filename;
    }
    
    public String getFilePathOfPng()
    {
    	return "/images/" + filename + ".png";
    }
    
    public static ImagePart getImagePartById(int idOfPArt)
    {
    	switch (idOfPArt) {
		case 2:
			return Deutschland;
		case 37:
			return BW;
		case 38:
			return Bayern;
		case 39:
			return Berlin;
		case 40:
			return Brandenburg;
		case 41:
			return Bremen;
		case 42:
			return Hamburg;
		case 43:
			return Hessen;
		case 44:
			return MecklenburgVorpommern;
		case 45:
			return Niedersachsen;
		case 46:
			return NordrheinWestfalen;
		case 47:
			return RheinlandPfalz;
		case 48:
			return Saarland;
		case 49:
			return Sachsen;
		case 50:
			return Sachsenanhalt;
		case 51:
			return ImagePart.SchleswigHolstein;
		case 52:
			return Thueringen;

		default:
			break;
		}
    	return BW;
    }
	
	}
	
	
	public ImageProvider()
	{
		Properties properties = new Properties();
		try {
			properties.load(getClass().getResourceAsStream("credentials.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String user = properties.getProperty("name");
		String pw = properties.getProperty("passwort");
		//System.out.println(user + " " + pw);
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
	
	public BufferedImage getImage(String urlOfImage)
	{		
		BufferedImage image = null;
		URL url;
		try {
			System.out.println(urlOfImage);
			url = new URL(urlOfImage); 
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

