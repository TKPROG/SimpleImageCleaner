package de.tkprog.simpleimagecleaner;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import de.tkprog.log.Logger;

public class SimpleImageCleaner {
	
	public static Logger log;

	public static final String NAME = "Simple Image Cleaner";
	public static final String VERSION = "v0.1.0-pre-stable";
	public static final String MADEBY = "TKPRO(G)";
	public static final String START = "17.02.2015";
	public static final String LAST = "17.02.2015";
	public static final String DESCRIPTION = "\"To clear and to protect the user of pictures.\"";

	public static void main(String[] args) {
		log = new Logger("log/log_"+System.currentTimeMillis()+".txt");
		log.setLogToCLI(true);
		log.setLogToFile(true);
		log.logAll(true);
		if(args.length==0){
			GUI();
		}
		else if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("-help") || args[0].equalsIgnoreCase("--help") || args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("/?")
				 || args[0].equalsIgnoreCase("\\?") || args[0].equalsIgnoreCase("/help") || args[0].equalsIgnoreCase("\\help") || args[0].equalsIgnoreCase("-?") || args[0].equalsIgnoreCase("--?")){
			help();
		}
		else{
			log.logInfoln("Starting SimpleImageCleaner... ");
			CLI(args);
		}
	}

	private static void help() {
		log.logln("+----------------------------------------------------+\r\n\t"+NAME+" "+VERSION+"\r\n\r\nMade by "+MADEBY+" from "+START+" till "+LAST+"\r\n\r\n"+DESCRIPTION+"\r\n+----------------------------------------------------+\r\n");
		log.logln("java -jar SimpleImageCleaner.jar [(help|-help|--help|?|/?|\\?|/help|\\help|-?|--?)|Source-Picture] [Destination-Picture]\r\n");
		log.logln("help|-help|--help|?|/?|\\?|/help|\\help|-?|--?\r\n\tShows this help.");
		log.logln("Source-Picture\r\n\tFor CLI: The source-picture to be loaded. (Doesn't work for GUI)");
		log.logln("Destination-Picture\r\n\tFor CLI: The destination for the processed picture. (Doesn't work for GUI)");
		log.logln("\r\nFor GUI Usage: Don't pass an argument.");
	}

	private static void exit() {
		log.logInfoln("Exiting normally...");
		System.exit(0);
	}

	private static void CLI(String[] args) {
		if(!(args.length==2 || args.length ==3)){
			log.logErrorln("To run this programm use this: jar nogui <src> <dest>");
			return;
		}
		else{
			String src = args[1];
			log.logInfoln("Got source picture: \""+src+"\"");
			String dest = null;
			if(args.length==3){
				dest = args[2];
				if((new File(dest)).exists()){
					log.logErrorln("Destination does already exist! (\""+dest+"\")");
					exit();
				}
				log.logInfoln("Got destination location: \""+dest+"\"");
			}
			else{
				log.logInfoln("Didn't receive a destination. Generating one by myself...");
				dest = src.substring(0, src.lastIndexOf("."))+"_converted_"+"."+src.substring(src.lastIndexOf(".")+1, src.length());
				if((new File(dest)).exists()){
					log.logErrorln("Newly created destination does already exist! (\""+dest+"\")");
					exit();
				}
				log.logInfoln("Got new destination: \""+dest+"\"");
			}
			
			log.logInfoln("Loading the src picture...");
			BufferedImage srcb = null;
			try{
				srcb = ImageIO.read(new File(src));
			} catch(Exception e){
				e.printStackTrace();
				log.logErrorln("Wasn't able to read the file: \""+e.getMessage()+"\"");
				exit();
			}
			log.logInfoln("DONE");
			
			log.logInfoln("Generating destination picture...");
			BufferedImage destb = new BufferedImage(srcb.getWidth(), srcb.getHeight(),srcb.getType());
			log.logInfoln("DONE");
			log.logInfoln("Starting copying-process...");
			long lastUpdate = System.currentTimeMillis();
			long nextUpdate = 1000;
			for(int a = 0;a < srcb.getWidth();a++){
				for(int b = 0;b < srcb.getHeight();b++){
					destb.setRGB(a, b, srcb.getRGB(a, b));
					if((System.currentTimeMillis()-lastUpdate)>nextUpdate){
						lastUpdate = System.currentTimeMillis();
						log.logInfoln("\tProcessed: "+((double)((int)((double)( (double)((a*srcb.getHeight())+b)/(double)(srcb.getWidth()*srcb.getHeight()) )*(double)100000))/(double)1000)+"%");
					}
				}
			}
			log.logInfoln("DONE");
			
			log.logInfoln("Saving picture to \""+dest+"\"...");
			try{
				String type = src.substring(src.lastIndexOf(".")+1,src.length());
				log.logInfoln("Using type \""+type+"\"...");
				ImageIO.write(destb, type, new File(dest));
			} catch(Exception e){
				e.printStackTrace();
				log.logErrorln("Wasn't able to save the picture: \""+e.getMessage()+"\"");
				exit();
			}
			log.logInfoln("DONE");
		}
	}

	private static void GUI() {
		log.logErrorln("The GUI is not made yet.");
	}

}
