package cn.ecnu.weixin.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;
import cn.ecnu.weixin.search.SearchOnWeb;
import cn.ecnu.weixin.util.CreateFloderFile;
import cn.ecnu.weixin.util.TheCrawlerUtil;

/**
 *@ClassName   : weixinCrawler.java
 *@Package     : cn.ecnu.weixin.crawler
 *@Author      : baoquanhuang 
 *@Email       : baoquanhuang@gmail.com
 *@Date        : 2015年12月2日上午10:14:54
 *@Description : This is weixin main function
 */
public class weixinCrawler {
	private final static Logger LOG = Logger.getLogger(weixinCrawler.class);
	public static String path = System.getProperty("user.dir");
	public final static String WEIXINID_FILE_PATH = System.getProperty("user.dir") + File.separator + "weixinID.txt";
	
	public static void main(String[] args) {
		InputStreamReader inputStreamReader;
		String weixinID;
		try {
			inputStreamReader = new InputStreamReader(new FileInputStream(WEIXINID_FILE_PATH),"gb2312");
			BufferedReader fReader = new BufferedReader(inputStreamReader);
			while((weixinID = fReader.readLine()) != null)
			{
				weixinID = weixinID.trim();
				//Create Floder to storage Data
				CreateFloderFile.JudgeDicrectory(path, weixinID);
				String folderPath = path + File.separator + weixinID;
				CreateFloderFile.JudgeDicrectory(folderPath, TheCrawlerUtil.GetCurrentDate());
				String filePath = folderPath + File.separator + TheCrawlerUtil.GetCurrentDate();
				//Crawl each weixin of public acount
				SearchOnWeb.searchSogou(weixinID,filePath);
//				System.out.println(weixinID);
			}
			fReader.close();
			inputStreamReader.close();
		} catch (UnsupportedEncodingException e) {
			LOG.error(e.toString());
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			LOG.error(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			LOG.error(e.toString());
			e.printStackTrace();
		}
		System.out.println("Today's task is done! see ya");
	}
}