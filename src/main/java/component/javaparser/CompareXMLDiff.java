package component.javaparser;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.ElementNameAndAttributeQualifier;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class CompareXMLDiff {

    private int numberOfReferenceTag;
    private List<String> fileDifferences = new ArrayList<>();

    public void compareXML(String file1, String file2) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        Document changedDoc = null;
        Document oldDoc = null;
        try {
            db = dbf.newDocumentBuilder();
            changedDoc = db.parse(new File(file1));
            oldDoc = db.parse(new File(file2));

            //changedDoc.

//            Diff diff = new Diff(changedDoc, oldDoc);
//            setNumberofReferenceTag(changedDoc);
//            DetailedDiff detDiff = new DetailedDiff(diff);
//            MatchTrackerImpl matchTracker = new MatchTrackerImpl();
//            detDiff.overrideMatchTracker(matchTracker);
//            detDiff.overrideElementQualifier(new ElementNameAndAttributeQualifier());
//            detDiff.getAllDifferences();

            NodeList changedComponent = changedDoc.getElementsByTagName("scr:component");
            String changedName = changedComponent.item(0).getAttributes().getNamedItem("name").toString();
            String changedImmediate = changedComponent.item(0).getAttributes().getNamedItem("immediate").toString();

            NodeList oldComponent = oldDoc.getElementsByTagName("scr:component");
            String oldName = oldComponent.item(0).getAttributes().getNamedItem("name").toString();;
            String oldImmediate = oldComponent.item(0).getAttributes().getNamedItem("immediate").toString();
            System.out.println("\nBefore Change Component  " + oldName + " " + oldImmediate);
            System.out.println("After Change  " + changedName + " " + changedImmediate);

            boolean isMatchComponentTag = false;
            boolean isMatchImplementationTags = false;
            if (changedName.equals(oldName) && changedImmediate.equals(oldImmediate)) {
                isMatchComponentTag = true;
                System.out.println("Component name and immediate are same");
            } else {
                System.out.println("Component name and immediate  are not same");
            }

            NodeList changedImplementation = changedDoc.getElementsByTagName("implementation");
            if (changedImplementation.getLength() == 0) {
                changedImplementation = changedDoc.getElementsByTagName("reference");
            }
            List<String> Classes = new ArrayList<>();
            for (int i = 0;  i < changedImplementation.getLength(); i++) {
                Classes.add(changedImplementation.item(i).getAttributes().getNamedItem("class").toString());
            }

            NodeList oldImplementation = oldDoc.getElementsByTagName("implementation");
            if (oldImplementation.getLength() == 0) {
                oldImplementation = oldDoc.getElementsByTagName("reference");
            }
            for (int i = 0;  i < oldImplementation.getLength(); i++) {
                String aClass = oldImplementation.item(i).getAttributes().getNamedItem("class").toString();
                if (Classes.contains(aClass)) {
                    Classes.remove(aClass);
                } else {
                    System.out.println(aClass + " is not found in the changed jar");
                    isMatchImplementationTags = true;
                }
            }

            if (Classes.size() == 0) {
                isMatchImplementationTags = true;
            }
            if (isMatchImplementationTags && isMatchComponentTag) {
                System.out.println(file1 + "\n" + file2 + "\nFiles are same");
                fileDifferences.add("matched\n");
            } else {
                String result = file1 + " and " + file2 + "\nFiles are different !!!!!!!!!!!!!!!!!!!! \n";
                System.out.println(result);
                fileDifferences.add(result);
                //writeFile(result,targetFile);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }


    public List getDifferences() {
        return fileDifferences;
    }

    public int getNumberofReferenceTag() {
        return numberOfReferenceTag;
    }

    public void setNumberofReferenceTag(Document doc) {
        NodeList nodeList = doc.getElementsByTagName("reference");
        numberOfReferenceTag = nodeList.getLength();

    }

    public List<String> getTargetXML(String file) {
        List<String> paths = new ArrayList<>();
        WSO2JavaParser wso2JavaParser = new WSO2JavaParser();
        Document doc = wso2JavaParser.readXMLDoc(file);
        NodeList nodeList = doc.getElementsByTagName("Path");
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            paths.add(nodeList.item(temp).getTextContent());
            //System.out.println(nodeList.item(temp).getTextContent());
        }

        return paths;
    }

}
