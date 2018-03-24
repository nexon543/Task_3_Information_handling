package com.epam.xml.node;


import java.util.*;

public class NodeAlphabet {
    private List<String> openSymbols;
    private List<String> closeSymbols;
    private Map<String, Set<NodeType>> openSymbolNodeTypeMap;
    private Map<String, Set<NodeType>> closeSymbolNodeTypeMap;
    private Map<String, List<String>> openSymbolPossibleCloseSymbolsMap;


    public NodeAlphabet() {
        openSymbols = new ArrayList<>();
        closeSymbols = new ArrayList<>();
        openSymbols.add(Constants.LEFT_BREKET_OPEN_TAG_OR_SINGLE_TAG);
        openSymbols.add(Constants.LEFT_XML_DECLARATION);
        openSymbols.add(Constants.LEFT_BREKET_CLOSE_TAG);
        closeSymbols.add(Constants.RIGHT_BRAKET_OPEN_TAG);
        closeSymbols.add(Constants.RIGHT_BRAKET_SINGLE_TAG);
        closeSymbols.add(Constants.RIGHT_XML_DECLARATION);

        openSymbolNodeTypeMap = new HashMap<>();
        openSymbolNodeTypeMap.put(Constants.LEFT_BREKET_OPEN_TAG_OR_SINGLE_TAG, EnumSet.of(NodeType.OPEN_TAG, NodeType.SINGLE_TAG));
        openSymbolNodeTypeMap.put(Constants.LEFT_XML_DECLARATION, EnumSet.of(NodeType.XML_DECLARATION));
        openSymbolNodeTypeMap.put(Constants.CONTENT_KEY, EnumSet.of(NodeType.CONTENT));
        openSymbolNodeTypeMap.put(Constants.LEFT_BREKET_CLOSE_TAG, EnumSet.of(NodeType.CLOSE_TAG));
        closeSymbolNodeTypeMap = new HashMap<>();
        closeSymbolNodeTypeMap.put(Constants.RIGHT_BRAKET_OPEN_TAG, EnumSet.of(NodeType.OPEN_TAG, NodeType.CLOSE_TAG));
        closeSymbolNodeTypeMap.put(Constants.RIGHT_XML_DECLARATION, EnumSet.of(NodeType.XML_DECLARATION));
        closeSymbolNodeTypeMap.put(Constants.RIGHT_BRAKET_SINGLE_TAG, EnumSet.of(NodeType.SINGLE_TAG));
        closeSymbolNodeTypeMap.put(Constants.CONTENT_KEY, EnumSet.of(NodeType.CONTENT));

        openSymbolPossibleCloseSymbolsMap = new HashMap<>();
        for (String openSymbol : openSymbols) {
            Set<NodeType> openSymbolNodeTypes = openSymbolNodeTypeMap.get(openSymbol);
            List<String> closeSymbols = getCloseSymbolsByNodeType(openSymbolNodeTypes);
            openSymbolPossibleCloseSymbolsMap.put(openSymbol, closeSymbols);
        }
        openSymbolPossibleCloseSymbolsMap.put(Constants.CONTENT_KEY, openSymbols);
    }

    public List<String> getPossibleCloseSymbol(String openSymbol){
        return openSymbolPossibleCloseSymbolsMap.get(openSymbol);
    }
    private List<String> getCloseSymbolsByNodeType(Set<NodeType> nodeTypeSet) {
        List<String> result = new ArrayList<>();
        for (NodeType nodeType : nodeTypeSet) {
            result.addAll(getCloseSymbolsByNodeType(nodeType));
        }
        return result;
    }

    private List<String> getCloseSymbolsByNodeType(NodeType nodeType) {
        List<String> result = new ArrayList<>();
        for (String closeSymbol : closeSymbols) {
            if (closeSymbolNodeTypeMap.get(closeSymbol).contains(nodeType)) {
                result.add(closeSymbol);
            }
        }
        return result;
    }

    public Set<NodeType> getNodeTypesForCloseSymbol(String closeSymbol){
        return EnumSet.copyOf(closeSymbolNodeTypeMap.get(closeSymbol));
    }
    public Set<NodeType> getNodeTypesForOpenSymbol(String openSymbol){
        return EnumSet.copyOf(openSymbolNodeTypeMap.get(openSymbol));
    }

    public List<String> getOpenSymbols() {
        return openSymbols;
    }
}
