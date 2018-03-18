package com.epam.xml.analyzer;

import com.epam.xml.node.Node;

import java.io.IOException;

public interface XMLNodeAnalyzer {

	Node getNextNode() throws IOException;
}
