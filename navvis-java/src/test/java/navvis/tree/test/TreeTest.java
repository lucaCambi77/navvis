/**
 * 
 */
package navvis.tree.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import navVis.tree.Node;
import navVis.tree.Tree;
import navVis.tree.TreePrinter1;

/**
 * @author luca
 *
 */
public class TreeTest
{

    @Test
    public void buildTree() throws IOException
    {

        Tree tree = new Tree();
        tree.buildTree(new File("src/test/resources/navvis.txt"));

        TreePrinter1.printTree(tree.getRoot(), "", true);

        Tree tree1 = new Tree();
        tree1.setTotFrequency(7);
        Node root = getRootTest();
        tree1.setRoot(root);

        assertEquals(true, identicalTrees(tree.getRoot(), tree1.getRoot()));
    }

    /**
     * @return Node for equality test
     */
    private Node getRootTest()
    {
        Node root = new Node();
        root.setFrequency(7);

        Node left1 = new Node();
        left1.setFrequency(3);

        Node left11 = new Node();
        left11.setFrequency(1);
        left11.setWord("problems");

        Node right2 = new Node();
        right2.setFrequency(2);

        Node right3 = new Node();
        right3.setFrequency(1);
        right3.setWord("to");

        Node left3 = new Node();
        left3.setFrequency(1);
        left3.setWord("She");

        right2.setLeft(left3);
        right2.setRight(right3);

        left1.setRight(right2);
        left1.setLeft(left11);

        root.setLeft(left1);

        Node right4 = new Node();
        right4.setFrequency(4);

        Node right5 = new Node();
        right5.setFrequency(2);
        right5.setWord("address");

        Node left5 = new Node();
        left5.setFrequency(2);
        left5.setWord("had");

        right4.setLeft(left5);
        right4.setRight(right5);

        root.setRight(right4);
        return root;
    }

    boolean identicalTrees(Node a, Node b)
    {
        /* 1. both empty */
        if (a == null && b == null)
            return true;

        /* 2. both non-empty -> compare them */
        if (a != null && b != null)
            return (a.getFrequency() == b.getFrequency() && a.getWord().equals(b.getWord())
                    && identicalTrees(a.getLeft(), b.getLeft()) && identicalTrees(a.getRight(), b.getRight()));

        /* 3. one empty, one not -> false */
        return false;
    }

}
