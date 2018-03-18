package com.epam.xml.node;

import java.util.Objects;

public class Node {

	private NodeType type;
	private String content;


	public NodeType getType() {
		return type;
	}

	public void setType(NodeType type) {
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
