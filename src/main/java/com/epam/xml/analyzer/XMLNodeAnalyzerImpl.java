package com.epam.xml.analyzer;

import java.io.*;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class XMLNodeAnalyzerImpl implements XMLNodeAnalyzer {

    private BufferedReader xmlFileReade;
    private StringBuilder currentLine;
    private StringBuilder currentNodeContent;


    private final String startDocumentElement="<?";
    private final String endDocumentElement="?>";
    private final String LEFT_BREKET_TAG="<";
    private final String LEFT_BREKET_SINGLE_TAG="</";
    private final String RIGHT_BRAKET_OPEN_TAG=">";
    private final String RIGHT_BRAKET_SINGLE_TAG="/>";
    private final String TOO_MANY_END_BRACKETS="let's make it easier";

    private Set<String> startOfNode;
    private Set<String> endOfNode;

    //type defining
    private Map<String, EnumSet<NodeType>> openBracketNodeTypeMap;
    private Map<String, EnumSet<NodeType>> closeBracketNodeTypeMap;

    private Map<String,Set<String>> openCloseBracketsOfNodes;

    private int getLastCharachterIndex(String openBracket){
        Set<String> closeBracketSet=openCloseBracketsOfNodes.get(openBracket);
    }

    private String getNodeContent(int endIndex){

    }

    private NodeType determineNodeType(String startBraket, String endBracket){

    }

    public XMLNodeAnalyzerImpl(String filePath) throws FileNotFoundException {
        FileReader fileReader = new FileReader(new File(filePath));
        BufferedReader xmlFileReade = new BufferedReader(fileReader);
        openCloseBracketsOfNodes =new HashMap<>();
        openCloseBracketsOfNodes.put(LEFT_BREKET_TAG, RIGHT_BRAKET_OPEN_TAG);
    }

    public Node getNextNode() throws IOException {
        Node node=new Node();


        return node;
    }

    private int getStartElementIndex(){
        int elementIndex=0;
        return elementIndex;
    }

    private void cutBeginning(int endIndex){
        currentLine.delete(0, endIndex);
    }
    private void readeNextLine() throws IOException {
        currentLine=new StringBuilder(xmlFileReade.readLine());
    }
}