package com.kukool.iosapp.weather.model;

/**
 *2011-5-9 
 *读取城市列表
 */
import java.io.ByteArrayInputStream;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class WebActionCityList {
	
	private static final String TAG = "WebActionCityList";
	private static final boolean DEBUG = true;  
	
	private static final String ADC_DATABASE = "adc_database";
	private static final String CITYLIST = "citylist";
	private static final String CITY = "city";
	private static final String STATE = "state";
	private static final String LOCATION = "location";
	
	private static final String BASE_URI = "http://forecastfox.accuweather.com/adcbin/forecastfox/locate_city.asp?location=";
	
	private Context mContext;
//	private ArrayList<CityListInfo> mCityListInfo = new ArrayList<CityListInfo>();

	public WebActionCityList(Context context) {
		mContext = context;
	}
	
	// 开始下载
	public void startLoadCityList(String searchSrc,String searchStr) {//hanzi  pinyin
		final String finalSearchStr = searchStr;
		final String finalsearchSrc = searchStr;
		if (mListener != null) {
			mListener.onStartDownLoadCityList();
		}
		new Thread(new Runnable() {
			public void run() {
				try {
					
					final String SearchStr0 = finalsearchSrc;
					HttpClient client = new DefaultHttpClient();
					String uir = BASE_URI
							+ encodeUrlData(finalSearchStr);
					Log.i(TAG,"URI:"+uir);
					HttpGet getMethod = new HttpGet(uir);
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					String responseBody = client.execute(getMethod,
							responseHandler);
					Log.i(TAG, "responseBody: "+responseBody);
					parseData(SearchStr0,encodeUrlData(responseBody));
					Log.i(TAG, "search "+SearchStr0+"---  "+encodeUrlData(responseBody));
				} catch (Exception e) {
					Log.e(TAG, "Error message = " + e.getMessage());
					Log.e(TAG, "network error... ");
					//这里在网络断开的时候会报错
				}
			}
		}).start();
	}

	private static String encodeUrlData(String urldata) {
		return urldata.replace("&#36;", "$").replace("&#39;", "'")
				.replace("&#94", "^").replace("%3A", ":").replace("%2F", "/")
				.replace("&#60;", "%3c").replace("|", "%7C");
	}
    
	
	private void parseData(String searchStr,String inform) {
		
//		mCityListInfo = new Vector<CityListInfo>();
//		mCityListInfo.clear();
		Vector <CityListInfo> info = new Vector <CityListInfo>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new ByteArrayInputStream(inform.getBytes()));
			Element root = document.getDocumentElement();
			
			if (root.getNodeName().equals(ADC_DATABASE)) {
				NodeList nodesCitylist = document
						.getElementsByTagName(CITYLIST);
				NodeList nodeitems = nodesCitylist.item(0).getChildNodes();
				for (int j = 0; j < nodeitems.getLength(); j++) { 
					Node node = nodeitems.item(j);
					String nodename = node.getNodeName();
					if (nodename.equals(LOCATION)) {
						String city = getNodeValue(node, CITY).toString().trim();
						String state = getNodeValue(node, STATE).toString().trim();
						String location = getNodeValue(node, LOCATION).toString().trim();
					    
						String cityEn = city + state;
						// 中文城市
						// 只查找在中国境内的城市的中文城市
						// 减少不必要查�?提高效率
						/*String cityCn  = Utils.NO_CHINISE_CITY;
						if (state.startsWith("China") || state.startsWith("Taiwan")|| state.startsWith("Hong Kong") || state.startsWith("Macau")) 
						{
							cityCn = new Utils(mContext).getChineseCity(cityEn);
							if (cityCn.equals(Utils.NO_CHINISE_CITY))
							{
								 
							} 
							else 
							{
								if(searchStr.compareToIgnoreCase(city) == 0)
								{
									info.add(new CityListInfo(city, state, location));
								}
							}
						} 
						else 
						{
							if(searchStr.compareToIgnoreCase(city) == 0)
							{
								info.add(new CityListInfo(city, state, location));
							}							
						}*/
						info.add(new CityListInfo(city, state, location));
					}
				}	
//				// 城市列表
//				Log.e(TAG, "------------------------------------->CityList ");
//				int size1 = mCityListInfo.size();
//				for (int i = 0; i < size1; i++) {
//					Log.e(TAG, "city = " + mCityListInfo.get(i).getCity());
//					Log.e(TAG, "state = " + mCityListInfo.get(i).getState());
//					Log.e(TAG, "location = " + mCityListInfo.get(i).getLocation());
//				}		
				Message msg = mHandler.obtainMessage();
				msg.what = 1100;
				msg.obj = info;
				mHandler.sendMessage(msg);
				return;
			}
		} catch (Exception e) {
			Log.e(TAG, "parseData exception : " + e.toString());
		}
	}
	
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (mListener != null) {
				// onFinishDownLoadCityList中有ui的改变
				// 所以要放到ui线程中处理
				if(msg.what == 1100)
				{
					mListener.onFinishDownLoadCityList((Vector <CityListInfo>)msg.obj);
				}
			}
		}
	};
	
	// 将节点数据放入向量

//	private void pushCityListValue(Node node) {
//		String nodename = node.getNodeName();
//		if (nodename.equals(LOCATION)) {
//			String city = getNodeValue(node, CITY).toString().trim();
//			String state = getNodeValue(node, STATE).toString().trim();
//			String location = getNodeValue(node, LOCATION).toString().trim();
//			mC.add(new CityListInfo(city, state, location));
//		}
//	}

	private String getNodeValue(Node node, String attributes) {
		return node.getAttributes().getNamedItem(attributes).getNodeValue();
	}
	
	/**
	 *2011-5-9 
	 *读取网络城市列表 的监听
	 */
	public DownLoadCityListListener mListener;
	
	public void addDownLoadCityListListener(DownLoadCityListListener listener) {
		mListener = listener;
	}
	
	public interface DownLoadCityListListener {
		public void onStartDownLoadCityList();
	    public void onFinishDownLoadCityList(Vector <CityListInfo> cityListInfo);
	}
	
}
