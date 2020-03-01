package com.njau.mobileplayer.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 解析JSON数据的工具类
 * @author zhangcan
 *
 */
public class UtilsForJSON {

//	带数组形式的：
//	服务器端返回的数据格式为：
//	{"calendar":
//	    {"calendarlist":
//	            [
//	            {"calendar_id":"1705","title":"(\u4eb2\u5b50)ddssd","category_name":"\u9ed8\u8ba4\u5206\u7c7b","showtime":"1288927800","endshowtime":"1288931400","allDay":false},
//	            {"calendar_id":"1706","title":"(\u65c5\u884c)","category_name":"\u9ed8\u8ba4\u5206\u7c7b","showtime":"1288933200","endshowtime":"1288936800","allDay":false}
//	            ]
//	    }
//	}
	
	// 总结，普通形式的只需用JSONObject,带数组形式的需要使用JSONArray 将其变成一个list
	/**
	 * 把JSON数据解析封装成一个一个的String类型的List集合，再调用时得到数据则可以把数据里面的一个一个List集合封装成一个一个对象
	 * @param JSON  传进来的JSON字符串
	 * @param jsonKey  JSON的数据数组对应的Key值，相当于上面的calendarlist
	 * @param strings  希望得到的每一个JSON对象的各字段值所对应的Key
	 * @return
	 */
	public static List<List<String>> parseJSON(String JSON,String jsonKey,String ...strings ) {

		List<List<String>> stringList = new ArrayList<List<String>>();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(JSON);
			JSONArray jsonArray = jsonObject.getJSONArray(jsonKey);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObjectItem = (JSONObject) jsonArray.opt(i);
				if(jsonObjectItem != null){
					List<String> strs = new ArrayList<String>();
					for(String str:strings){
						String value = jsonObjectItem.optString(str);
						strs.add(value);
					}
					stringList.add(strs);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return stringList;
	}

}
