/**
 *
 */
package navVis.tree;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * @author luca
 *
 */
@Getter
@Setter
public class Tree {
    protected Node root;
    int totFrequency = 0;

    int size() {
        return size(root);
    }

    int size(Node node) {
        if (node == null)
            return 0;
        else
            return (size(node.left) + 1 + size(node.right));
    }

    public void buildTree(File file) throws IOException {

        StringBuilder builder = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            in.lines().forEach(l -> builder.append(l).append(" "));
        }

        /**
         * Split file by space and group by frequency
         */
        Map<String, Long> wordToFrequency = Stream
                .of(builder.toString().split("[\\s]+"))
                .filter(w -> !w.isEmpty())
                .collect(groupingBy(Function.identity(), counting()));

        Map<Integer, LinkedList<Node>> freqToNodesMap = new HashMap<>();

        wordToFrequency.entrySet().stream()
                .sorted(Map.Entry.comparingByValue()).forEach(w -> {

            Node node = new Node(w.getKey());
            node.frequency = w.getValue().intValue();

            LinkedList<Node> freqList = freqToNodesMap.getOrDefault(w.getValue().intValue(), new LinkedList<>());
            freqList.add(node);
            freqToNodesMap.put(w.getValue().intValue(), freqList);
        });

        /**
         * Create two Nodes and add it to the tree
         */
        for (Entry<Integer, LinkedList<Node>> element : freqToNodesMap.entrySet()) {

            LinkedList<Node> frequencyList = element.getValue();
            int count = 0;
            int frequencyListSize = frequencyList.size();

            while (count < frequencyListSize) {
                List<Node> nodeToParent = frequencyList.subList(count,
                        Math.min(count + 2, frequencyListSize));

                Node[] nodePair = new Node[2];

                insertParent(nodePair, addToNodeParent(nodeToParent, nodePair));

                count += 2;
            }
        }
    }

    private int addToNodeParent(List<Node> nodeToParent, Node[] nodePair) {

        int frequency = 0;

        if (nodeToParent.get(0) != null) {

            nodePair[0] = nodeToParent.get(0);
            frequency += nodePair[0].frequency;
        }

        /**
         * Second element can be null
         */
        if (1 < nodeToParent.size() && nodeToParent.get(1) != null) {

            nodePair[1] = nodeToParent.get(1);
            frequency += nodePair[1].frequency;

        }

        return frequency;
    }

    public void insertParent(Node[] nodes, Integer frequency) {

        if (nodes.length > 0) {

            Node left = null;
            Node right = null;

            left = nodes[0];

            if (1 < nodes.length && null != nodes[1])
                right = nodes[1];

            Node parent = new Node();
            parent.left = left;
            parent.right = right;

            if (root == null) {
                parent.frequency = totFrequency + frequency;
                root = parent;
                return;
            }

            /**
             * We insert to the right if it is only one Node, otherwise we create another parent
             */
            if (right == null) {
                parent.frequency = frequency + root.frequency;
                parent.right = root;
                root = parent;
            } else {
                parent.frequency = frequency;

                Node parentUp = new Node();
                parentUp.frequency = totFrequency + frequency;
                parentUp.left = root;
                parentUp.right = parent;
                root = parentUp;

            }

            totFrequency = root.frequency;
        }

    }

}
