package com.edgar.lilyhouse.Controllers;

import android.os.Handler;
import android.util.Log;

import com.edgar.lilyhouse.Utils.HtmlParser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ReaderController {

    private String jsonString;

    private static final ReaderController instance = new ReaderController();

    private ReaderController() {}

    public static ReaderController getInstance() {
        return instance;
    }

    public void setupImageUrl(String urlString, Handler handler) {

        Log.d("URRRRRRRRRRRRRE", "setupImageUrl: " + urlString);

        HtmlParser htmlParser = new HtmlParser(urlString, handler);
        htmlParser.setDataFromDocument(imageData);
        htmlParser.start();

    }

    private HtmlParser.DataFromDocument imageData = new HtmlParser.DataFromDocument() {
        @Override
        public void setData(Document document) {

            Elements scriptElements = document.select("script");
            for (Element scriptElement : scriptElements) {
                String scriptString = scriptElement.html();
                if (scriptString.contains("initData") && scriptString.contains("page_url")) {
                    String[] strings = scriptString.split("\n");
                    for (int i = 0; i < strings.length; i++) {
                        String str = strings[i];
                        if (str.contains("initData") && str.contains("page_url")) {
                            int startPos = str.indexOf("{");
                            int endPos = str.indexOf("}") + 1;
                            jsonString = str.substring(startPos, endPos);
                            break;

                        }
                    }
                    break;
                }
            }
        }
    };

    public String getJsonString() {
        return jsonString;
    }
}
