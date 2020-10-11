/**
 *
 */
package navVis.tree;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Node {
    String word = "";
    int frequency;
    Node left, right;

    public Node() {
    }

    public Node(String word) {
        this.word = word;
    }
}
