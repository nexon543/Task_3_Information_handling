package com.epam.xml.analyzer;

import java.util.Objects;

public class Node {

	String type;
	String content;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Node node = (Node) o;
		return Objects.equals(type, node.type) &&
				Objects.equals(content, node.content);
	}

	@Override
	public int hashCode() {

		return Objects.hash(type, content);
	}

	@Override
	public String toString() {
		return "Node{" +
				"type='" + type + '\'' +
				", content='" + content + '\'' +
				'}';
	}


}
