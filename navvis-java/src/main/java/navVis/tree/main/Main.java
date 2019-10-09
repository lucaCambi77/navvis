/**
 * 
 */
package navVis.tree.main;

import java.io.File;
import java.io.IOException;

import navVis.tree.Tree;
import navVis.utils.Errors;

/**
 * @author luca
 *
 */
public class Main {

	private static Tree treeInstance = new Tree();

	public static void main(String[] args) throws IOException {

		if (args.length == 0) {
			System.out.println(Errors.ERR_NO_FILE_INPUT);
			return;
		}

		File file = new File(args[0]);

		if (!file.exists()) {
			System.out.println(Errors.ERR_FILE_NOT_EXISTS);
			return;
		}

		treeInstance.buildTree(file);
		treeInstance.print(treeInstance.getRoot());
	}
}
