package cn.ecnu.weixin.search;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.ParseConversionEvent;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.ecnu.weixin.util.CreateFloderFile;
import cn.ecnu.weixin.util.TheCrawlerUtil;
import cn.ecnu.weixin.util.WebConstructor;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 *@ClassName   : SearchOnWeb.java
 *@Package     : cn.ecnu.weixin.search
 *@Author      : baoquanhuang 
 *@Email       : baoquanhuang@gmail.com
 *@Date        : 2015年12月1日下午7:11:43
 *@Description : TODO
 */
public class SearchOnWeb {
	private static Logger log = Logger.getLogger(SearchOnWeb.class);
	public final static String WEIXIN = "http://weixin.sogou.com/weixin?type=1&query=";

	/** 
	* @Title       : SearchOnWeb
	* @Description : search weixinID on sogou web and fetch the public ID web
	* @return      : @param weixinID
	* @return      : @return the public ID web 
	*/
	public static void searchSogou(String weixinID, String path)
	{
		WebClient webClient = WebConstructor.ConstructWebClient();
		try {
			String htmlContent;
			String articleTitle;
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(weixinID);
			weixinID = m.replaceAll("");
//			Search the weixin ID on sogou search engine
			HtmlPage page = webClient.getPage(WEIXIN + weixinID);
			Document doc = Jsoup.parse(page.asXml());
			Element div_weixinID = doc.getElementById("sogou_vr_11002301_box_0");
			String weixinhomeUrl = div_weixinID.attr("href").toString().trim();
			weixinhomeUrl = "http://weixin.sogou.com" + weixinhomeUrl;
			
//			Parse Each article URL
			HtmlPage homepage = webClient.getPage(weixinhomeUrl);
			Document homeDoc = Jsoup.parse(homepage.asXml());
			Element  div_wxbox =  homeDoc.getElementById("gzh_arts_1");
			Elements articleLists = div_wxbox.select(".txt-box h4 a");
			int count = 1;
			for(Element articlelist : articleLists)
			{
				String eachArticleUrl = articlelist.attr("href").toString().trim();
				HtmlPage articleContent = webClient.getPage("http://weixin.sogou.com"+ eachArticleUrl);
				htmlContent = articleContent.asText();
				articleTitle = articleContent.getTitleText().toString().trim() + TheCrawlerUtil.GetCurrentDate();
				CreateFloderFile.CreateFileAndWrite(path, htmlContent, articleTitle);
				System.out.println(weixinID +" : "+ count + " Task Done.");
				count++;
			}
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
		}
	}
}