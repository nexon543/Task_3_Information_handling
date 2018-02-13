package com.epam.xml.analyzer;

public abstract class XMLNodeAnalyzer {

	public abstract void startTag(String tagName);
	public abstract void endTag(String tagName);
	public abstract void content(String content);
	public abstract void parse(String uri);
	public abstract Node nextNode();
	
}
