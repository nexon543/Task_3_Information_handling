package com.epam.xml.analyzer;

import java.io.IOException;

public interface XMLNodeAnalyzer {

	Node getNextNode() throws IOException;
	
}
