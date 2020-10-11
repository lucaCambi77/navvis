/**
 * 
 */
package navVis.tree.main;

import navVis.tree.Tree;
import navVis.tree.TreePrinter;
import navVis.utils.Errors;

import java.io.File;
import java.io.IOException;

public class Main
{

    private static Tree treeInstance = new Tree();

    public static void main(String[] args) throws IOException
    {

        if (args.length == 0)
        {
            System.out.println(Errors.ERR_NO_FILE_INPUT);
            return;
        }

        File file = new File(args[0]);

        if (!file.exists())
        {
            System.out.println(Errors.ERR_FILE_NOT_EXISTS);
            return;
        }

        treeInstance.buildTree(file);
        TreePrinter.printTree(treeInstance.getRoot(), "", true);
    }
}
