package com.whd.aop.sp;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;


public class VideoUtil {
	
	/**
     * 获取指定视频的帧并保存为图片至指定目录
     * @param file  源视频文件
     * @param framefile  截取帧的图片存放路径
     * @throws Exception
     */
    public static void fetchPic(File file, String framefile) throws Exception{
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(file); 
        ff.start();
        int lenght = ff.getLengthInFrames();

        File targetFile = new File(framefile);
        int i = 0;
        Frame frame = null;
        System.out.println(lenght+"");
        while (i < lenght) {
  
            frame = ff.grabFrame();
            if ((i > 5) && (frame.image != null)) {
                break;
            }
            i++;
        }      

        String imgSuffix = "jpg";
        if(framefile.indexOf('.') != -1){
            String[] arr = framefile.split("\\.");
            if(arr.length>=2){
                imgSuffix = arr[1];
            }
        }

        Java2DFrameConverter converter =new Java2DFrameConverter();
        BufferedImage srcBi =converter.getBufferedImage(frame);
        int owidth = srcBi.getWidth();
        int oheight = srcBi.getHeight();
        // 对截取的帧进行等比例缩放
        int width = 200;
        int height = (int) (((double) width / owidth) * oheight);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(srcBi.getScaledInstance(width, height, Image.SCALE_SMOOTH),0, 0, null);
        try {
            ImageIO.write(bi, imgSuffix, targetFile);
        }catch (Exception e) {
            e.printStackTrace();
        }      
        ff.stop();
    }

    /**
     * 获取视频时长，单位为秒
     * @param file
     * @return 时长（s）
     */
    public static Long getVideoTime(File file){
        Long times = 0L;
        try {
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(file); 
            ff.start();
            times = ff.getLengthInTime()/(1000*1000);
            ff.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }
    
    public static void main(String[] args) throws Exception {
    	VideoUtil.fetchPic(new File("D:/test/263.mp4"), "D:/test1/d.jpg");
	}
    
}
