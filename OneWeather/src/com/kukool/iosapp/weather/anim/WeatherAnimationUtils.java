package com.kukool.iosapp.weather.anim;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


import android.content.Context;
import android.content.res.XmlResourceParser;
import android.content.res.Resources.NotFoundException;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

public class WeatherAnimationUtils extends AnimationUtils {

	public static Animation loadAnimation(Context context, int id)
			throws NotFoundException {

		XmlResourceParser parser = null;
		try {
			parser = context.getResources().getAnimation(id);
			return createAnimationFromXml(context, parser);
		} catch (XmlPullParserException ex) {
			NotFoundException rnf = new NotFoundException(
					"Can't load animation resource ID #0x"
							+ Integer.toHexString(id));
			rnf.initCause(ex);
			throw rnf;
		} catch (IOException ex) {
			NotFoundException rnf = new NotFoundException(
					"Can't load animation resource ID #0x"
							+ Integer.toHexString(id));
			rnf.initCause(ex);
			throw rnf;
		} finally {
			if (parser != null)
				parser.close();
		}
	}

	private static Animation createAnimationFromXml(Context c,
			XmlPullParser parser) throws XmlPullParserException, IOException {

		return createAnimationFromXml(c, parser, null,
				Xml.asAttributeSet(parser));
	}

	private static Animation createAnimationFromXml(Context c,
			XmlPullParser parser, AnimationSet parent, AttributeSet attrs)
			throws XmlPullParserException, IOException {

		Animation anim = null;

		// Make sure we are on a start tag.
		int type;
		int depth = parser.getDepth();

		while (((type = parser.next()) != XmlPullParser.END_TAG || parser
				.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {

			if (type != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();

			if (name.equals("set")) {
				anim = new AnimationSet(c, attrs);
				createAnimationFromXml(c, parser, (AnimationSet) anim, attrs);
			} else if (name.equals("IphoneRotate3d")) {
				anim = new Rotate3dAnimation(c, attrs);
			} else {
				throw new RuntimeException("Unknown animation name: "
						+ parser.getName());
			}

			if (parent != null) {
				parent.addAnimation(anim);
			}
		}

		return anim;

	}

}
