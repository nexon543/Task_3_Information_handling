package com.epam.xml.analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class XMLNodeAnalyzerImpl implements XMLNodeAnalyzer {

    private BufferedReader xmlFileReader;
    private StringBuilder currentLine;
    private StringBuilder currentNodeContent;
    private String currentOpenSymbol;
    private String currentCloseSymbol;


    private NodeAlphabet nodeAplphabet;

    private String getOpenSymbol() {
        String result = Constants.CONTENT_KEY;
        for (String openSymbol : nodeAplphabet.getOpenSymbols()) {
            if ((currentLine.indexOf(openSymbol) != -1) && result.length() < openSymbol.length()) {
                result = openSymbol;
            }
        }
        return result;
    }

    private int getIndexOfClosestString(List<String> possibleCloseBrackets) throws IOException {
        int index = -1;

        for (String closeSymbol : possibleCloseBrackets) {
            int currentSymbolIndex = currentLine.indexOf(closeSymbol);
            if (currentSymbolIndex != -1 && currentSymbolIndex < index) {
                index = currentSymbolIndex;
                currentCloseSymbol = closeSymbol;
            }
            if (index == -1 && currentSymbolIndex != -1) {
                index = currentSymbolIndex;
                currentCloseSymbol = closeSymbol;
            }
        }
        if (index == -1) {
            currentNodeContent.append(currentLine);
            readNextLine();
            index=getIndexOfClosestString(possibleCloseBrackets);
        }
        return index;
    }

    private NodeType determineNodeType() {
        Set<NodeType> openSymbolPossibleTypes = nodeAplphabet.getNodeTypesForOpenSymbol(currentOpenSymbol);
        Set<NodeType> closeSymbolPossibleTypes = nodeAplphabet.getNodeTypesForCloseSymbol(currentCloseSymbol);
        openSymbolPossibleTypes.retainAll(closeSymbolPossibleTypes);
        return openSymbolPossibleTypes.iterator().next();
    }

    public XMLNodeAnalyzerImpl(String filePath) throws IOException {
        FileReader fileReader = new FileReader(new File(filePath));
        xmlFileReader = new BufferedReader(fileReader);
        currentLine = new StringBuilder(xmlFileReader.readLine());
        nodeAplphabet = new NodeAlphabet();
        currentNodeContent = new StringBuilder();

    }

    public Node getNextNode() throws IOException {
        Node node = new Node();
        currentNodeContent=new StringBuilder();
        currentOpenSymbol = getOpenSymbol();
        List<String> possibleCloseSymbols = nodeAplphabet.getPossibleCloseSymbol(currentOpenSymbol);
        currentLine.delete(0, currentOpenSymbol.length());
        int nodeEndIndex = getIndexOfClosestString(possibleCloseSymbols);
        while (nodeEndIndex == -1) {
            currentNodeContent.append(currentLine);
            readNextLine();
            nodeEndIndex = getIndexOfClosestString(possibleCloseSymbols);
        }
        if (currentOpenSymbol.equals(Constants.CONTENT_KEY)) {
            nodeEndIndex--;
        }
        currentNodeContent.append(currentLine.substring(0, nodeEndIndex));
        currentLine.delete(0, nodeEndIndex + currentCloseSymbol.length());
        node.setContent(currentNodeContent.toString());
        node.setType(determineNodeType());
        return node;
    }

    public static void main(String[] args) throws IOException {
        XMLNodeAnalyzer a = new XMLNodeAnalyzerImpl("web.xml");
        while (a.hasNext()) {
            Node node = a.getNextNode();
            System.out.println(node.getType() + ":" + node.getContent());
        }
    }


    public boolean hasNext() throws IOException {
        while (currentLine.length() == 0) {
            readNextLine();
        }
        return !(currentLine.length() == 0);
    }

    private void readNextLine() throws IOException {
        String nextString = xmlFileReader.readLine().trim();
        currentLine.delete(0,currentLine.length());
        currentLine.append(nextString);
    }
}