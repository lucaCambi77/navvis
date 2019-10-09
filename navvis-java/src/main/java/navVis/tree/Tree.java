/**
 * 
 */
package navVis.tree;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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

import lombok.Getter;
import lombok.Setter;

/**
 * @author luca
 *
 */
@Getter
@Setter
public class Tree
{
    protected Node root;
    int totFrequency = 0;

    int size()
    {
        return size(root);
    }

    /* computes number of nodes in tree */
    int size(Node node)
    {
        if (node == null)
            return 0;
        else
            return (size(node.left) + 1 + size(node.right));
    }

    /**
     * @param nodes
     * @param frequency
     */
    public void insertParent(Node[] nodes, Integer frequency)
    {

        if (nodes.length > 0)
        {

            Node left = null;
            Node right = null;

            left = nodes[0];

            if (1 < nodes.length && null != nodes[1])
                right = nodes[1];

            Node parent = new Node();
            parent.left = left;
            parent.right = right;

            if (root == null)
            {
                parent.frequency = totFrequency + frequency;
                root = parent;
                return;
            }

            /**
             * We insert to the right if it is only one Node, otherwise we create another parent
             */
            if (right == null)
            {
                parent.frequency = frequency + root.frequency;
                parent.right = root;
                root = parent;
            }
            else
            {
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

    /**
     * Build Tree method
     * 
     * @param s
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void buildTree(File file) throws FileNotFoundException, IOException
    {

        StringBuilder builder = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new FileReader(file)))
        {

            in.lines().forEach(l -> builder.append(l).append(" "));
        }

        String fileToString = builder.toString();

        /**
         * Split file by space and carriage return and order by frequency ascending
         */
        String[] split = fileToString.toString().split("[\\s]+");
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
        for (Entry<Integer, LinkedList<Node>> element : map.entrySet())
        {

            LinkedList<Node> frequencyList = element.getValue();
            int count = 0;
            int frequenceListSize = frequencyList.size();

            while (count < frequenceListSize)
            {
                List<Node> nodeToParent = frequencyList.subList(count,
                        count + 2 > frequenceListSize ? frequenceListSize : count + 2);

                Node[] nodePair = new Node[2];

                insertParent(nodePair, addToNodeParent(nodeToParent, nodePair));

                count += 2;
            }
        }
    }

    /**
     * @param nodeToParent
     * @param nodePair
     */
    private int addToNodeParent(List<Node> nodeToParent, Node[] nodePair)
    {

        int frequency = 0;

        if (nodeToParent.get(0) != null)
        {

            nodePair[0] = nodeToParent.get(0);
            frequency += nodePair[0].frequency;
        }

        /**
         * Second element can be null
         */
        if (1 < nodeToParent.size() && nodeToParent.get(1) != null)
        {

            nodePair[1] = nodeToParent.get(1);
            frequency += nodePair[1].frequency;

        }

        return frequency;
    }

    /**
     * Given a binary tree, print out all of its root-to-leaf paths, one per line. Uses a recursive helper to do the work.
     */
    public void print(Node node)
    {
        String path[] = new String[100000];
        printRecur(node, path, 0);
    }

    /**
     * Recursive helper function -- given a node, and an array containing the path from the root node up to but not including this node, print out all
     * the root-leaf paths.
     */
    void printRecur(Node node, String path[], int pathLen)
    {
        if (node == null)
            return;

        /** append this node to the path array */
        path[pathLen] = node.getFrequency() + " " + node.getWord();
        pathLen++;

        /** it's a leaf, so print the path that led to here */
        if (node.left == null && node.right == null)
            print(path, pathLen);
        else
        {
            /* otherwise try both subtrees */
            printRecur(node.left, path, pathLen);
            printRecur(node.right, path, pathLen);
        }
    }

    /** Utility function that prints out an array on a line. */
    void print(String paths[], int len)
    {
        int i;
        for (i = 0; i < len; i++)
        {
            System.out.print(paths[i] + " ");
        }
        System.out.println("");
    }

}
