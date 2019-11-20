import java.io.File;
import java.io.IOException;
// DOM
// SAX
import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.helpers.DefaultHandler;

public class SAXParseWV_1 {
    double sumX, sumY, sumX2, sumXY, x, y, k, b, t;
    boolean isX = false, isY = false;
    int num = 0;

    void startElement(String uri, String localName, String qName, Attributes attributes){
        //String qName = " ";
        System.out.println("Start element parsing: " + qName);

        //qName = element.getElementsByTagName("x").item(0).getNodeName();
        if (qName.equals("x")) {
            isX = true;
        }
       // qName = element.getElementsByTagName("y").item(0).getNodeName();
        if (qName.equals("y")) {
            isY = true;
        }
    }

    void endElement(String uri, String localName, String qName) {
        //String qName =  element.getTagName();

        System.out.println("End of element parsing: " + qName);
        //qName = element.getElementsByTagName("x").item(0).getNodeName();

        if (qName.equals("x")) {
            isX = false;
            num++;
        }
        //qName = element.getElementsByTagName("y").item(0).getNodeName();
        if (qName.equals("y")) {
            isY = false;
            t = 0;
            num++;
        }
    }

    public void startDocument() {
        System.out.println("Start document parsing...");
        x = 0;
        y = 0;
        sumX = 0;
        sumY = 0;
        sumX2 = 0;
        sumXY = 0;
        num = 0;
        t = 0;
        k = 0;
        b = 0;
    }
    public void endDocument() {
        System.out.println("End of document parsing...");
        num /= 2;
        k = (sumXY - sumX * sumY / num) / (sumX2 - sumX * sumX / num);
        b = sumY / num - k * sumX / num;

        System.out.println("Result  k: " + k + "\t" + "b:" + b );
        //+" " +  sumX + " " + sumX2+ " " + sumY + " " + sumXY);
    }

    public void characters(char[] ch, int start, int length){
        String str = "";
        for (int i = 0; i < length; i++) {
            str += ch[start + i];
        }
        System.out.println(str);
        double tmp = 0;
        if (isX) {
           // Element element = (Element) node;
            tmp = Double.parseDouble(str);

            sumX += tmp;
            sumX2 += tmp * tmp;
            t = tmp;
        }
        if (isY) {
            //Element element = (Element) node;
            tmp = Double.parseDouble(str);

            sumY += tmp;
            t = t * tmp;
            sumXY += t;
        }
    }

//====================================Task 2=====================================
    public static void main(String [] args) throws ParserConfigurationException, SAXException, IOException {
        SAXParseWV_1 s = new SAXParseWV_1();
        DefaultHandler handler = new DefaultHandler(){

            @Override
            public void startDocument() throws SAXException {
                s.startDocument();
            }
            @Override
            public void endDocument() throws SAXException {
                s.endDocument();
            }
            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException{
                s.endElement(uri, localName, qName);
            }
            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                s.characters(ch,start,length);
            }
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                s.startElement(uri,localName,qName,attributes);
            }
        };


        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(new File("points.xml"), handler);

    }
}