package ru.flevix;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: flevix
 * Date: 04.12.12
 * Time: 22:29
 */

public class DownloadHelper {

    URL url;
    URLConnection urlConnection;
    DocumentBuilderFactory documentBuilderFactory;
    DocumentBuilder documentBuilder;
    Document document;
    Node node;
    NodeList nodeList, nodeList1, nodeList2, nodeList3;
    int firstId, currID;
    ContentValues contentValues = new ContentValues();
    NamedNodeMap namedNodeMap;
    String[] forecastDay = null;
    final Uri WeatherUri = MyContentProvider.WeatherContentUri;

    void getForecast(ContentResolver contentResolver, int id, String city) throws IOException, ParserConfigurationException, SAXException {
        url = new URL("http://xml.weather.co.ua/1.2/forecast/" + id + "?dayf=3&lang=ru");
        init();
        contentValues = DataBaseHandler.fillCVbyNull(city);
        contentValues.put(DataBaseHandler.lastUpdate, System.currentTimeMillis() / 1000);

        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeName().equals("current")) {
                nodeList1 = nodeList.item(i).getChildNodes();

                for (int j = 0; j < nodeList1.getLength(); j++) {
                    if (nodeList1.item(j).getNodeName().equals("pict")) {
                        contentValues.put(DataBaseHandler.currPict, nodeList1.item(j).getTextContent());
                    } else if (nodeList1.item(j).getNodeName().equals("t")) {
                        contentValues.put(DataBaseHandler.currT, nodeList1.item(j).getTextContent());
                    } else if (nodeList1.item(j).getNodeName().equals("h")) {
                        contentValues.put(DataBaseHandler.currH, nodeList1.item(j).getTextContent());
                    } else if (nodeList1.item(j).getNodeName().equals("p")) {
                        contentValues.put(DataBaseHandler.currP, nodeList1.item(j).getTextContent());
                    } else if (nodeList1.item(j).getNodeName().equals("w")) {
                        contentValues.put(DataBaseHandler.currW, nodeList1.item(j).getTextContent());
                    }
                }
            } else if (nodeList.item(i).getNodeName().equals("forecast")) {
                nodeList1 = nodeList.item(i).getChildNodes();
                int firstId = -1;
                for (int j = 0; j < nodeList1.getLength(); j++) {
                    if (nodeList1.item(j).getNodeName().equals("day")) {
                        nodeList2 = nodeList1.item(j).getChildNodes();
                        if (firstId == -1) {
                            namedNodeMap = nodeList1.item(j).getAttributes();
                            for (int naa = 0; naa < namedNodeMap.getLength(); naa++) {
                                if (namedNodeMap.item(naa).getNodeName().equals("hour")) {
                                    int x = Integer.parseInt(namedNodeMap.item(naa).getTextContent());
                                    switch (x) {
                                        case 3:
                                            firstId = 1;
                                            break;
                                        case 9:
                                            firstId = 2;
                                            break;
                                        case 15:
                                            firstId = 3;
                                            break;
                                        case 21:
                                            firstId = 4;
                                            break;
                                    }
                                    currID = firstId;
                                }
                            }
                        }
                        for (int j2 = 0; j2 < nodeList2.getLength(); j2++) {
                            forecastDay = getForecastById(currID);
                            if (nodeList2.item(j2).getNodeName().equals("pict")) {
                                contentValues.put(forecastDay[0], nodeList2.item(j2).getTextContent());
                            } else if (nodeList2.item(j2).getNodeName().equals("t")) {
                                nodeList3 = nodeList2.item(j2).getChildNodes();
                                dT = "";
                                for (int temp1 = 0; temp1 < nodeList3.getLength(); temp1++) {
                                    if (nodeList3.item(temp1).getNodeName().equals("min")) {
                                        dT = nodeList3.item(temp1).getTextContent();
                                    } else if (nodeList3.item(temp1).getNodeName().equals("max")) {
                                        dT += ".. " + nodeList3.item(temp1).getTextContent();
                                    }
                                }
                                contentValues.put(forecastDay[1], dT);

                            }
                        }
                        currID++;
                        if (currID > 12) break;
                    }
                }
            }
            for (int c = 1; c < firstId; c++) {
                forecastDay = getForecastById(c);
                contentValues.put(forecastDay[0], "_255_na");
                contentValues.put(forecastDay[1], "N/A");
            }
        }
        Uri uri = ContentUris.withAppendedId(WeatherUri, id);
        contentResolver.update(uri, contentValues, null, null);
    }
    String dT = "";

    HashMap<String, Integer> citys = new HashMap<String, Integer>();
    int id_city;
    String name_city;

    HashMap<String, Integer> getCitys(String cityQuery) throws IOException, ParserConfigurationException, SAXException {
        url = new URL("http://xml.weather.co.ua/1.2/city/?search=" + URLEncoder.encode(cityQuery, "UTF-8") + "&lang=ru");
        init();

        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeName().equals("city")) {
                namedNodeMap = nodeList.item(i).getAttributes();
                if (namedNodeMap == null) {
                    throw new IOException("=(");
                } else {
                   id_city  = Integer.parseInt(namedNodeMap.item(0).getTextContent());
                }
                nodeList1 = nodeList.item(i).getChildNodes();
                for (int i1 = 0; i1 < nodeList1.getLength(); i1++) {
                    if (nodeList1.item(i1).getNodeName().equals("name")) {
                        name_city = nodeList1.item(i1).getTextContent().trim();
                    } else if (nodeList1.item(i1).getNodeName().equals("country")) {
                        name_city += ". " + nodeList1.item(i1).getTextContent().trim();
                    }
                }
                citys.put(name_city, id_city);
            }
        }
        return citys;
    }

    void init() throws IOException, ParserConfigurationException, SAXException {
        urlConnection = url.openConnection();
        urlConnection.setConnectTimeout(20000);
        urlConnection.connect();
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(false);
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
        document = documentBuilder.parse(urlConnection.getInputStream());
        node = document.getChildNodes().item(0).getFirstChild();
        nodeList = document.getChildNodes().item(0).getChildNodes();
    }

    String[] getForecastById(int x) {
        switch (x) {
            case 1:
                return new String[]{"PICT1", "T1"};
            case 2:
                return new String[]{"PICT2", "T2"};
            case 3:
                return new String[]{"PICT3", "T3"};
            case 4:
                return new String[]{"PICT4", "T4"};
            case 5:
                return new String[]{"PICT5", "T5"};
            case 6:
                return new String[]{"PICT6", "T6"};
            case 7:
                return new String[]{"PICT7", "T7"};
            case 8:
                return new String[]{"PICT8", "T8"};
            case 9:
                return new String[]{"PICT9", "T9"};
            case 10:
                return new String[]{"PICT10", "T10"};
            case 11:
                return new String[]{"PICT11", "T11"};
            case 12:
                return new String[]{"PICT12", "T12"};
            default:
                return new String[]{"", ""};
        }

    }
}
