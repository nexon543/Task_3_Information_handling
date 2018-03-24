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
    private NodeAlphabet nodeAplphabet;
    private boolean isEndOfFile;

    public XMLNodeAnalyzerImpl(String filePath) throws IOException {
        FileReader fileReader = new FileReader(new File(filePath));
        xmlFileReader = new BufferedReader(fileReader);
        currentLine = new StringBuilder();
        nodeAplphabet = new NodeAlphabet();
    }

    public Node getNextNode() throws IOException {
        if (isEndOfFile){
            return null;
        }
        String currentNodeOpenSymbol;
        String currentNodeCloseSymbol;
        Node node = new Node();
        currentNodeOpenSymbol = getOpenSymbol();
        cutCurrentLineFromTheBeginning(currentNodeOpenSymbol.length());
        String currentNodeContent = getContent(currentNodeOpenSymbol);
        cutCurrentLineFromTheBeginning(currentNodeContent.length());
        currentNodeCloseSymbol=getCloseSymbol(currentNodeOpenSymbol);
        cutCurrentLineFromTheBeginning(currentNodeCloseSymbol.length());
        node.setContent(currentNodeContent.toString());
        node.setType(determineNodeType(currentNodeOpenSymbol, currentNodeCloseSymbol));
        return node;
    }

    private String getContent(String currentNodeOpenSymbol) throws IOException {
        StringBuilder content = new StringBuilder();
        List<String> possibleCloseSymbols = nodeAplphabet.getPossibleCloseSymbol(currentNodeOpenSymbol);
        int currentNodeEndIndex = getCurrentNodeCloseSymbolIndex(possibleCloseSymbols);
        while (currentNodeEndIndex == -1) {
            content.append(currentLine);
            readNextLine();
            currentNodeEndIndex = getCurrentNodeCloseSymbolIndex(possibleCloseSymbols);
        }
        content.append(currentLine.substring(0, currentNodeEndIndex));
        return content.toString();
    }

    private String getOpenSymbol() throws IOException {
        return getLongestSymbolFromTheBegining(nodeAplphabet.getOpenSymbols());
    }
    private String getCloseSymbol(String currentNodeOpenSymbol) throws IOException {
        if (currentNodeOpenSymbol.equals(Constants.CONTENT_KEY)) {
            return Constants.CONTENT_KEY;
        }
        return getLongestSymbolFromTheBegining(nodeAplphabet.getPossibleCloseSymbol(currentNodeOpenSymbol));
    }

    private void cutCurrentLineFromTheBeginning(int length) {
        currentLine.delete(0, length);
    }

    private void readNextLine() throws IOException {
        String newLine = "";
        while (newLine.length() == 0) {
            newLine = xmlFileReader.readLine().trim();
        }
        currentLine.setLength(0);
        currentLine.append(newLine);
        if (currentLine.length()==0){
            isEndOfFile=true;
        }
    }

    private String getLongestSymbolFromTheBegining(List<String> symbols) throws IOException {
        String result = Constants.CONTENT_KEY;
        if ("".equals(currentLine.toString())){
            readNextLine();
        }
        for (String openSymbol :symbols) {
            int openSymbolIndex = currentLine.indexOf(openSymbol);
            int openSymbolLength = openSymbol.length();
            if ((openSymbolIndex == 0)
                    && (result.length() < openSymbolLength)) {
                result = openSymbol;
            }
        }
        return result;
    }

    private int getCurrentNodeCloseSymbolIndex(List<String> possibleCloseSymbols) throws IOException {
        int index = -1;
        for (String closeSymbol : possibleCloseSymbols) {
            int currentSymbolIndex = currentLine.indexOf(closeSymbol);
            if (currentSymbolIndex != -1 && currentSymbolIndex < index) {
                index = currentSymbolIndex;
            }
            if (index == -1 && currentSymbolIndex != -1) {
                index = currentSymbolIndex;
            }
        }
        return index;
    }

    private NodeType determineNodeType(String currentNodeOpenSymbol, String currentNodeCloseSymbol) {
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