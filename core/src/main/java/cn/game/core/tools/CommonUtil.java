package cn.game.core.tools;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.game.core.tools.PropertyFilter.MatchType;

public class CommonUtil {
	
	public static final char UNDERLINE = '_';

	/**
	 * 将字符串查询条件转换成Groups对象
	 * @param params
	 * @return
	 */
	public static Groups filterGroup(String params) {
		Gson gson = new Gson();
		List<Group> searchGroups = gson.fromJson(params, new TypeToken<List<Group>>() {
		}.getType());
		Groups groups = new Groups();
		try {
			if (searchGroups != null && !searchGroups.isEmpty()) {
				for (Group group : searchGroups) {
					String propertyName = group.getPropertyName();
					if (propertyName != null) {
						if (propertyName.indexOf(",") > -1) {// 处理or的关系
							List<Groups> childGroups2 = new ArrayList<Groups>();
							String[] propertyNames = propertyName.split(",");
							Groups groups2 = new Groups();
							for (String name : propertyNames) {
								if (!"".equals(name)) {
									Group group2 = new Group();
									group2.setPropertyName(name);
									group2.setPropertyValue1(group.getPropertyValue1());
									group2.setTempMatchType(group.getTempMatchType());
									group2.setTempType(group.getTempType());
									group2.setRelation(MatchType.OR);
									groups2.Add(group2);
								}
							}
							groups2.setChildrelation(MatchType.AND);// 与外部条件的关系
							childGroups2.add(groups2);
							groups.setChildGroups2(childGroups2);
						} else {
							group.setPropertyName(propertyName);
							group.setTempMatchType(group.getTempMatchType());
							group.setTempType(group.getTempType());
							groups.Add(group);
						}
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return groups;
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 */
	public static void deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.isFile()) {
			file.delete();
		}
	}

	
	/**
	 * 
	 * @param content
	 * @param savePath
	 * @param filename
	 * @return
	 */
	public static String writeToFile(String content, String savePath, String filename) {
		int len = content.getBytes().length;
		if (len > 2 * 1024 * 1024) {//控制在2M以内
			return "";
		}
		content = content.replace("data:image/jpeg;base64,", "").replace("data:image/png;base64,", "")
				.replace("data:image/bmp;base64,", "").replace("data:image/gif;base64,", "");
		byte[] byteArray = Base64.getDecoder().decode(content);// base64解码
		OutputStream os = null;
		String newFileName = "";
		try {
			String extName = "";// 扩展名
			String nowTime = getNow(6, null);// 当前时间 毫秒数

			File dir = new File(savePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			if (filename.lastIndexOf(".") >= 0) {
				extName = filename.substring(filename.lastIndexOf("."));
			}
			newFileName = nowTime + extName;
			File file = new File(savePath + newFileName);
			os = new BufferedOutputStream(new FileOutputStream(file));
			os.write(byteArray);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
		}
		return newFileName;
	}

	/**
	 * 写文件
	 * @param source
	 * @param destination
	 * @param filename
	 * @return
	 */
	public static String writeTxtFile(String source, String destination, String filename) {
		StringBuffer sbufer = new StringBuffer();
		try {
			File dir = new File(destination);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			destination += filename;
			FileWriter writer = new FileWriter(new File(destination));
			writer.write(source);
			writer.flush();
			writer.close();

			InputStream inputStream = new FileInputStream(new File(destination));
			InputStreamReader read = new InputStreamReader(inputStream);// 考虑到编码格式
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				sbufer.append(lineTxt);
			}
			read.close();
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sbufer.toString();
	}

	
	
	/**
	 * 根据图片路径生成缩略图
	 * @param imgFile 原图片路径
	 * @param w 缩略图宽
	 * @param h 缩略图高
	 * @param prevfix 生成缩略图的前缀
	 * @param force 是否强制按照宽高生成缩略图(如果为false，则生成最佳比例缩略图)
	 */
	public static void thumbnailImage(File imgFile, int w, int h, String prevfix, boolean force) {
		if (imgFile.exists()) {
			try {
				// ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG,
				// JPEG, WBMP, GIF, gif]
				String types = Arrays.toString(ImageIO.getReaderFormatNames());
				String suffix = null;
				// 获取图片后缀
				if (imgFile.getName().indexOf(".") > -1) {
					suffix = imgFile.getName().substring(imgFile.getName().lastIndexOf(".") + 1);
				} // 类型和图片后缀全部小写，然后判断后缀是否合法
				if (suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()) < 0) {
					System.out.println("Sorry, the image suffix is illegal. the standard image suffix is {}." + types);
					return;
				}
				Image img = ImageIO.read(imgFile);
				if (!force) {
					// 根据原图与要求的缩略图比例，找到最合适的缩略图比例
					int width = img.getWidth(null);
					int height = img.getHeight(null);
					if ((width * 1.0) / w < (height * 1.0) / h) {
						if (width > w) {
							h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w / (width * 1.0)));
						}
					} else {
						if (height > h) {
							w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h / (height * 1.0)));
						}
					}
					if (width < w) {
						w = width;
					}
					if (height < h) {
						h = height;
					}
				}
				BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				Graphics g = bi.getGraphics();
				g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
				g.dispose();
				String p = imgFile.getPath();
				// 将图片保存在原目录并加上前缀
				ImageIO.write(bi, suffix, new File(
						p.substring(0, p.lastIndexOf(File.separator)) + File.separator + prevfix + imgFile.getName()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("the image is not exist.");
		}
	}
	
	
	/**
	 * 根据原图与裁切size截取局部图片
	 * @param srcImg  源图片
	 * @param output  图片输出流
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public static void cutImage(File srcImg, OutputStream output, int x, int y, int width, int height) {
		Rectangle rect = new Rectangle(x, y, width, height);
		if (srcImg.exists()) {
			java.io.FileInputStream fis = null;
			ImageInputStream iis = null;
			try {
				fis = new FileInputStream(srcImg);
				// ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG,
				// JPEG, WBMP, GIF, gif]
				String types = Arrays.toString(ImageIO.getReaderFormatNames()).replace("]", ",");
				String suffix = null;
				// 获取图片后缀
				if (srcImg.getName().indexOf(".") > -1) {
					suffix = srcImg.getName().substring(srcImg.getName().lastIndexOf(".") + 1);
				} // 类型和图片后缀全部小写，然后判断后缀是否合法
				if (suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase() + ",") < 0) {
					System.out.println("Sorry, the image suffix is illegal. the standard image suffix is {}." + types);
					return;
				}
				// 将FileInputStream 转换为ImageInputStream
				iis = ImageIO.createImageInputStream(fis);
				// 根据图片类型获取该种类型的ImageReader
				ImageReader reader = ImageIO.getImageReadersBySuffix(suffix).next();
				reader.setInput(iis, true);
				ImageReadParam param = reader.getDefaultReadParam();
				param.setSourceRegion(rect);
				BufferedImage bi = reader.read(0, param);
				ImageIO.write(bi, suffix, output);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fis != null)
						fis.close();
					if (iis != null)
						iis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("the src image is not exist.");
		}
	}

	/**
	 * 判断字符是否为空
	 * @param str
	 * @return
	 */
	public static boolean isNull(Object str) {
		boolean isTrue = false;
		if (str == null || "".equals(str)) {
			isTrue = true;
		}
		return isTrue;
	}
	
	/**
	 * 返回指定时间段内的所有日期
	 * @param dBegin
	 * @param dEnd
	 * @return
	 */
	public static List<Date> findDates(Date dBegin, Date dEnd) {
		List<Date> lDate = new ArrayList<Date>();
		lDate.add(dBegin);
		Calendar calBegin = Calendar.getInstance();
		calBegin.setTime(dBegin);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(calBegin.getTime());
		}
		return lDate;
	}

	/**
	 * split方法
	 * @param s
	 * @param delimiter
	 * @return
	 */
	public static String[] split(String s, String delimiter) {
		int delimiterLength;
		int stringLength = s.length();
		if (delimiter == null || (delimiterLength = delimiter.length()) == 0) {
			return new String[] { s };
		}

		int count;
		int start;
		int end;

		count = 0;
		start = 0;
		while ((end = s.indexOf(delimiter, start)) != -1) {
			count++;
			start = end + delimiterLength;
		}
		count++;

		String[] result = new String[count];
		count = 0;
		start = 0;
		while ((end = s.indexOf(delimiter, start)) != -1) {
			result[count] = (s.substring(start, end));
			count++;
			start = end + delimiterLength;
		}
		end = stringLength;
		result[count] = s.substring(start, end);

		return (result);
	}

	/**
	 * Date转换成String
	 * @param type
	 * @param time
	 * @return
	 */
	public static String getNow(int type, Date time) {
		SimpleDateFormat sdf = null;
		switch (type) {
		case 0:
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			break;
		case 1:
			sdf = new SimpleDateFormat("yyyyMMdd");
			break;
		case 2:
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			break;
		case 3:
			sdf = new SimpleDateFormat("yyyyMM");
			break;
		case 4:
			sdf = new SimpleDateFormat("yyyy年MM月dd日");
			break;
		case 5:
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			break;
		case 6:
			sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			break;
		case 7:
			sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			break;
		default:
			sdf = new SimpleDateFormat("yyyy-M-D");
			break;
		}
		if (time == null) {
			time = new Date();
		}

		return sdf.format(time);
	}

	/**
	 * String转换成Date
	 * @param type
	 * @param time
	 * @return
	 */
	public static Date parseDate(int type, String time) {
		SimpleDateFormat sdf = null;
		switch (type) {
		case 0:
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			break;
		case 1:
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			break;
		case 2:
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			break;
		}

		try {
			return sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 驼峰格式字符串转换为下划线格式字符串
	 * @param param
	 * @return
	 */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        if(sb.charAt(0)==UNDERLINE){
        	sb.deleteCharAt(0);
        }
        return sb.toString();
    }
    
    /**
     * 产生随机的2位数
     * @author liubin
     * @date  2017年2月28日
     */
    public static String getTwo(){  
        Random rad=new Random();  
        String result  = rad.nextInt(100) +"";  
        if(result.length()==1){  
            result = "0" + result;  
        }  
        return result;  
    }
    
    /**
     * 生成订单批次号，在大并发的情况下会有bug
     * @author liubin
     * @date  2017年2月28日
     */
    @Deprecated
    public static String getBatchNo(){
    	String batchNo = CommonUtil.getNow(6,new Date())+CommonUtil.getTwo();
    	return batchNo;
    }

}
