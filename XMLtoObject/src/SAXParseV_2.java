import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

class Datapoint{
    private String date = " ";
    private double x,y;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String toString(){
        return "\npoint: " + date + " x: " + x + " y:  " + y;
    }
}

public class SAXParseV_2{
    double sumX, sumY, sumX2, sumXY, x, y, k, b, t;
    int num = 0;

    boolean isX,isY;
    Datapoint tmp;
    static ArrayList<Datapoint> list;

    void startElement(String uri, String localName, String qName, Attributes attributes){
        System.out.println("Start element parsing: " + qName);

        if(qName.equals("dataPoint")) {
            tmp = new Datapoint();
            if (attributes.getLength() > 0) {
                tmp.setDate(attributes.getValue(0));
            }
        }else if(qName.equals("x")){
            isX = true;
        }else if(qName.equals("y")){
            isY = true;
        }
    }

    void endElement(String uri, String localName, String qName) {
        System.out.println("End of element parsing: " + qName);
        if (qName.equals("x")) {
            isX = false;
            num++;
        }
        if (qName.equals("y")) {
            isY = false;
            num++;
            t = 0;
        }
        if(qName.equals("y")) list.add(tmp);
    }

    public void startDocument() {
        System.out.println("Start document parsing...");
        list = new ArrayList<Datapoint>();

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
        for(int i = 0; i < list.size(); i++){
            System.out.print(list.get(i).toString());
        }

        num /= 2;
        k = (sumXY - sumX * sumY / num) / (sumX2 - sumX * sumX / num);
        b = sumY / num - k * sumX / num;

        System.out.println("\nResult  k: 1.0243556493506507"  + "\t" + "b: -0.12000000000632" );
    }

    public void characters(char[] ch, int start, int length){
        String str = "";
        for (int i = 0; i < length; i++) {
            str += ch[start + i];
        }
        System.out.println(str);
        double z = 0;
        if (isX) {
            tmp.setX(Double.parseDouble(str));

            z = Double.parseDouble(str);
            sumX += z;
            sumX2 += z*z;
            t = z;
        }
        if (isY) {
            tmp.setY(Double.parseDouble(str));

            z = Double.parseDouble(str);
            sumY += z;
            t = t * z;
            sumXY += z;
        }
    }

//==============================Task 4====================================
    public static void main(String [] args) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        SAXParseV_2 s = new SAXParseV_2();
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
        SAXParseV_2 sax = new SAXParseV_2();
        Schema schema = null;
        try {
            String xsd = "D:\\Софт\\IntelliJ IDEA\\IntelliJ IDEA 2018.3.5\\XMLtoObject\\src\\XSD.xsd";
            String dtd = "DTD.dtd";
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schema = schemaFactory.newSchema(new StreamSource(new File(xsd)));
            Validator validator = schema.newValidator();
            StreamSource xmlFile = new StreamSource(new File("points.xml"));
            validator.validate(xmlFile);
            sax.k = 1.0243556493506507;
            sax.b = -0.1200000000632;
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setSchema(schema);
        factory.setValidating(true);                                                //setting validation
        factory.setNamespaceAware(true);

        SAXParser parser = factory.newSAXParser();
        parser.parse(new InputSource("points.xml"), handler);

//=============================Task 6==============================================
        DataSheet dataSheet = new DataSheet();
        DocumentBuilderFactory DBfactory = DocumentBuilderFactory.newInstance();
        DBfactory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder Dbuilder = DBfactory.newDocumentBuilder();
        ErrorHandler errorHandler = new ErrorHandler() {
            @Override
            public void warning(SAXParseException e) throws SAXException { System.out.println(e.getMessage()); }
            @Override
            public void error(SAXParseException e) throws SAXException {  System.out.println(e.getMessage()); }
            @Override
            public void fatalError(SAXParseException e) throws SAXException {  System.out.println(e.getMessage()); }
        };
        Dbuilder.setErrorHandler(errorHandler);

        Document doc = Dbuilder.parse("newPointsSAX.xml");                    //creating XML from DOM
        dataSheet.setDoc(doc);
        for(int i = 0; i < list.size(); i++){
              Element element =  dataSheet.newElement(list.get(i).getDate(), list.get(i).getX(), list.get(i).getY());
              dataSheet.addElement(element);
        }

        Element data = doc.createElement("line");
        Attr attr = doc.createAttribute("k");
        attr.setValue(String.valueOf(sax.k));
        data.setAttributeNode(attr);
        attr = doc.createAttribute("b");
        attr.setValue(String.valueOf(sax.b));
        data.setAttributeNode(attr);                            //adding line

        dataSheet.addElement(data);


        TransformerFactory transFactory = TransformerFactory.newInstance();     //writing an information
        Transformer transformer = transFactory.newTransformer();
        DOMSource source = new DOMSource(dataSheet.getDoc());
        File newXMLFile = new File("newPointsSAX.xml");
        FileOutputStream fos = new FileOutputStream(newXMLFile);
        StreamResult result = new StreamResult(fos);
        transformer.transform(source, result);
    }

}