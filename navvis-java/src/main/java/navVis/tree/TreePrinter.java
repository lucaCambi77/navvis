/**
 * 
 */
package navVis.tree;

public class TreePrinter
{

    public static void printTree(Node root, String indent, boolean last)
    {
        if (null == root)
            return;

        System.out.println(indent + "+- " + root.frequency + " " + root.word);
        indent += last ? "   " : "|  ";

        printTree(root.left, indent, !root.word.isEmpty());
        printTree(root.right, indent, !root.word.isEmpty());

    }
}
