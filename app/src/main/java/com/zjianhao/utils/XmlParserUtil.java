package com.zjianhao.utils;

import android.util.Xml;

import com.zjianhao.bean.App;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjianhao on 15-12-6.
 */
public class XmlParserUtil {
    public static List<App> parseXml(InputStream inputStream) throws XmlPullParserException, IOException {
        List<App> apps = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream,"utf-8");
        int eventType = parser.getEventType();
        App app = null;
        while(eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    apps = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("app"))
                        app = new App();
                    else if (parser.getName().equals("versionId")){
                        eventType = parser.next();
                        app.setVersionId(Integer.parseInt(parser.getText()));
                    }else if (parser.getName().equals("versionName")){
                        eventType = parser.next();
                        app.setVersionName(parser.getText());
                    }else if (parser.getName().equals("fileName")){
                        eventType = parser.next();
                        app.setFileName(parser.getText());
                    } else if ("description".equals(parser.getName())){
                        eventType = parser.next();
                        app.setDescription(parser.getText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("app".equals(parser.getName())){
                        eventType = parser.next();
                        apps.add(app);
                        app = null;
                    }
                    break;
            }
            eventType = parser.next();
        }
        return apps;
    }

}
