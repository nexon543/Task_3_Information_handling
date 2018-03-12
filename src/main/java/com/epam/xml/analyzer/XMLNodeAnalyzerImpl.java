package com.epam.xml.analyzer;

import java.io.*;

public class XMLNodeAnalyzerImpl implements XMLNodeAnalyzer {

    BufferedReader xmlFileReade;
    String currentLine;
    int lastElementIndex;

    public XMLNodeAnalyzerImpl(String filePath) throws FileNotFoundException {
        FileReader fileReader = new FileReader(new File(filePath));
        BufferedReader xmlFileReade = new BufferedReader(fileReader);
    }

    @Override
    public Node getNextNode() throws IOException {
        Node node=new Node();
        String line;

    }

    private int getStartElementIndex(){
        int elementIndex=0;
        return elementIndex;
    }
}