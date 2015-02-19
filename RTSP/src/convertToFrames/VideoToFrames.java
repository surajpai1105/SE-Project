package convertToFrames;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.DemuxerTrack;
import org.jcodec.common.model.Packet;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;
public class VideoToFrames
{

	/**
	 * @param args
	 * @throws JCodecException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, JCodecException
	{
//		int frameNumber = 150;
//		File f=new File("D:\\movies\\Dawn of the Planet of the Apes (2014)\\Dawn.of.the.Planet.of.the.Apes.2014.720p.BluRay.x264.YIFY.mp4");
//		for(int i=0;i<10;i++)
//		{
//			BufferedImage frame = FrameGrab.getFrame(f,150);
//			ImageIO.write(frame, "jpg", new File("D:\\movies\\Dawn of the Planet of the Apes (2014)\\frame_"+i+".jpg"));
//		}
//		String path = "D:\\movies\\Dawn of the Planet of the Apes (2014)\\Dawn.of.the.Planet.of.the.Apes.2014.720p.BluRay.x264.YIFY.mp4";

//	    MP4Demuxer demuxer1 = new MP4Demuxer(new FileInputStream(new File(path)));
//	    DemuxerTrack videoTrack = demuxer1.getVideoTrack();
//
//	    Packet firstFrame = videoTrack.nextFrame();//getFrames(1);
//	    byte[] data = firstFrame.getData();
		
		double startSec = 51.632;
		FrameGrab grab = new FrameGrab(new File("filename.mp4"));
		grab.seek(startSec);
		for (int i = 0; i < 100; i++) {
		  ImageIO.write(grab.getFrame(), "png",
		    new File(System.getProperty("user.home"), String.format("Desktop/frame_%08d.png", i)));
		}
	}

}
