package com.kukool.iosapp.weather.provider;
/**
 * 保存中文，英文城市的表
 * 一共三个字段id, city_state_en ,city_cn
 * 暂时不需要
 */
import android.net.Uri;
import android.provider.BaseColumns;

public class Cities {
	private Cities() {

	}

	public static final class Cities_Column implements BaseColumns {
		private Cities_Column() {
		};

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ OneWeatherProvider.AUTHORITY + "/cities");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.cities";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.cities";
		
		// 城市,省份(英文名)
		public static final String CITY_STATE_EN = "city_state_en";
		// 城市(中文名)
		public static final String CITY_CN = "city_cn";
	}

}
