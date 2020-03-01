package com.njau.mobileplayer.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * ����JSON���ݵĹ�����
 * @author zhangcan
 *
 */
public class UtilsForJSON {

//	��������ʽ�ģ�
//	�������˷��ص����ݸ�ʽΪ��
//	{"calendar":
//	    {"calendarlist":
//	            [
//	            {"calendar_id":"1705","title":"(\u4eb2\u5b50)ddssd","category_name":"\u9ed8\u8ba4\u5206\u7c7b","showtime":"1288927800","endshowtime":"1288931400","allDay":false},
//	            {"calendar_id":"1706","title":"(\u65c5\u884c)","category_name":"\u9ed8\u8ba4\u5206\u7c7b","showtime":"1288933200","endshowtime":"1288936800","allDay":false}
//	            ]
//	    }
//	}
	
	// �ܽᣬ��ͨ��ʽ��ֻ����JSONObject,��������ʽ����Ҫʹ��JSONArray ������һ��list
	/**
	 * ��JSON���ݽ�����װ��һ��һ����String���͵�List���ϣ��ٵ���ʱ�õ���������԰����������һ��һ��List���Ϸ�װ��һ��һ������
	 * @param JSON  ��������JSON�ַ���
	 * @param jsonKey  JSON�����������Ӧ��Keyֵ���൱�������calendarlist
	 * @param strings  ϣ���õ���ÿһ��JSON����ĸ��ֶ�ֵ����Ӧ��Key
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
