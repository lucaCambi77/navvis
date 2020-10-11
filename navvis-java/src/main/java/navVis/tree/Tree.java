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
import java.util.function.Supplier;
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

    /* computes number of nodes in tree */
    int size(Node node) {
        if (node == null)
            return 0;
        else
            return (size(node.left) + 1 + size(node.right));
    }

    /**
     * @param nodes
     * @param frequency
     */
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

    public void buildTree(File file) throws IOException {

        StringBuilder builder = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            in.lines().forEach(l -> builder.append(l).append(" "));
        }

        String fileToString = builder.toString();

        /**
         * Split file by space and carriage return and order by frequency ascending
         */
        String[] split = fileToString.split("[\\s]+");
        Supplier<Stream<String>> streamSupplier = () -> Stream.of(split).filter(w -> !w.isEmpty());

        Map<String, Long> wordToFrequency = streamSupplier.get().collect(groupingBy(Function.identity(), counting()));

        /**
         * Create a frequency to Node map
         */
        Map<Integer, LinkedList<Node>> map = new HashMap<>();
        streamSupplier.get().distinct().forEach(w -> {

            int frequency = wordToFrequency.get(w).intValue();

            Node node = new Node(w);
            node.frequency = frequency;

            LinkedList<Node> freqList = map.getOrDefault(frequency, new LinkedList<>());
            freqList.add(node);
            map.put(frequency, freqList);
        });

        /**
         * Create two Nodes and add it to the tree
         */
        for (Entry<Integer, LinkedList<Node>> element : map.entrySet()) {

            LinkedList<Node> frequencyList = element.getValue();
            int count = 0;
            int frequenceListSize = frequencyList.size();

            while (count < frequenceListSize) {
                List<Node> nodeToParent = frequencyList.subList(count,
                        count + 2 > frequenceListSize ? frequenceListSize : count + 2);

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
}
