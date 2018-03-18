package com.epam.xml.analyzer;

import com.epam.xml.node.Constants;
import com.epam.xml.node.Node;
import com.epam.xml.node.NodeAlphabet;
import com.epam.xml.node.NodeType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class XMLNodeAnalyzerImpl implements XMLNodeAnalyzer {

    private BufferedReader xmlFileReader;

    private StringBuilder currentLine;
    private StringBuilder currentNodeContent;
    private String currentNodeOpenSymbol;
    private String currentNodeCloseSymbol;
    private List<String> possibleCloseSymbols;
    private int currentNodeEndIndex;

    private NodeAlphabet nodeAplphabet;


    public XMLNodeAnalyzerImpl(String filePath) throws IOException {
        FileReader fileReader = new FileReader(new File(filePath));
        xmlFileReader = new BufferedReader(fileReader);
        currentLine = new StringBuilder();
        nodeAplphabet = new NodeAlphabet();
        currentNodeContent = new StringBuilder();

    }

    public Node getNextNode() throws IOException {
        Node node = new Node();
        readNextLine();
        getOpenSymbol();
        cutCurrentLineFromTheBeginning(currentNodeOpenSymbol.length());
        getContentAndSetCloseSymbol();
        cutCurrentLineFromTheBeginning(currentNodeEndIndex + currentNodeCloseSymbol.length());
        node.setContent(currentNodeContent.toString());
        node.setType(determineNodeType());
        return node;
    }

    private void getContentAndSetCloseSymbol() throws IOException {
        currentNodeContent.setLength(0);
        if (currentNodeOpenSymbol.equals(Constants.CONTENT_KEY)) {
            currentNodeCloseSymbol = Constants.CONTENT_KEY;
        }
        else {
            possibleCloseSymbols = nodeAplphabet.getPossibleCloseSymbol(currentNodeOpenSymbol);
        }
        currentNodeEndIndex= getCurrentNodeCloseSymbol(possibleCloseSymbols);
        while (currentNodeEndIndex == -1) {
            currentNodeContent.append(currentLine);
            readNextLine();
            currentNodeEndIndex = getCurrentNodeCloseSymbol(possibleCloseSymbols);
        }
        currentNodeContent.append(currentLine.substring(0, currentNodeEndIndex));
    }
    private void cutCurrentLineFromTheBeginning(int length){
        currentLine.delete(0, length);
    }
    private void readNextLine() throws IOException {
        String newLine="";
        while (newLine.length() == 0) {
            newLine = xmlFileReader.readLine().trim();
        }
        currentLine.setLength(0);
        currentLine.append(newLine);
    }

    private void getOpenSymbol() {
        currentNodeOpenSymbol = Constants.CONTENT_KEY;
        for (String openSymbol : nodeAplphabet.getOpenSymbols()) {
            int openSymbolIndex = currentLine.indexOf(openSymbol);
            int openSymbolLength = openSymbol.length();
            if ((openSymbolIndex == 0)
                    && (currentNodeOpenSymbol.length() < openSymbolLength)) {
                currentNodeOpenSymbol = openSymbol;
            }
        }
    }

    private int getCurrentNodeCloseSymbol(List<String> possibleCloseBrackets) throws IOException {
        int index = -1;

        for (String closeSymbol : possibleCloseBrackets) {
            int currentSymbolIndex = currentLine.indexOf(closeSymbol);
            if (currentSymbolIndex != -1 && currentSymbolIndex < index) {
                index = currentSymbolIndex;
                currentNodeCloseSymbol = closeSymbol;
            }
            if (index == -1 && currentSymbolIndex != -1) {
                index = currentSymbolIndex;
                currentNodeCloseSymbol = closeSymbol;
            }
        }
        if (index == -1) {
            currentNodeContent.append(currentLine);
            readNextLine();
            index = getCurrentNodeCloseSymbol(possibleCloseBrackets);
        }
        return index;
    }

    private NodeType determineNodeType() {
        Set<NodeType> openSymbolPossibleTypes = nodeAplphabet.getNodeTypesForOpenSymbol(currentNodeOpenSymbol);
        Set<NodeType> closeSymbolPossibleTypes = nodeAplphabet.getNodeTypesForCloseSymbol(currentNodeCloseSymbol);
        openSymbolPossibleTypes.retainAll(closeSymbolPossibleTypes);
        NodeType nodeType = null;
        Iterator iterator = openSymbolPossibleTypes.iterator();
        if (iterator.hasNext())
            nodeType = (NodeType) iterator.next();
        return nodeType;
    }
}