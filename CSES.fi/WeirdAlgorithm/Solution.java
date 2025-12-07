import java.util.*;

public class Solution {
    // Red-Black Tree Node
    static class RBNode {
        int key;
        int value;
        RBNode left, right, parent;
        boolean isRed;
        
        RBNode(int key, int value) {
            this.key = key;
            this.value = value;
            this.isRed = true; // New nodes are red
            this.left = this.right = this.parent = null;
        }
    }
    
    // Red-Black Tree implementation
    static class RedBlackTree {
        private RBNode root;
        private RBNode NIL;
        
        public RedBlackTree() {
            NIL = new RBNode(0, 0);
            NIL.isRed = false;
            root = NIL;
        }
        
        public void insert(int key, int value) {
            RBNode node = new RBNode(key, value);
            node.left = NIL;
            node.right = NIL;
            
            RBNode y = null;
            RBNode x = root;
            
            while (x != NIL) {
                y = x;
                if (node.key < x.key) {
                    x = x.left;
                } else {
                    x = x.right;
                }
            }
            
            node.parent = y;
            if (y == null) {
                root = node;
            } else if (node.key < y.key) {
                y.left = node;
            } else {
                y.right = node;
            }
            
            if (node.parent == null) {
                node.isRed = false;
                return;
            }
            
            if (node.parent.parent == null) {
                return;
            }
            
            fixInsert(node);
        }
        
        private void fixInsert(RBNode k) {
            while (k.parent != null && k.parent.isRed) {
                if (k.parent == k.parent.parent.right) {
                    RBNode u = k.parent.parent.left;
                    if (u.isRed) {
                        u.isRed = false;
                        k.parent.isRed = false;
                        k.parent.parent.isRed = true;
                        k = k.parent.parent;
                    } else {
                        if (k == k.parent.left) {
                            k = k.parent;
                            rotateRight(k);
                        }
                        k.parent.isRed = false;
                        k.parent.parent.isRed = true;
                        rotateLeft(k.parent.parent);
                    }
                } else {
                    RBNode u = k.parent.parent.right;
                    if (u.isRed) {
                        u.isRed = false;
                        k.parent.isRed = false;
                        k.parent.parent.isRed = true;
                        k = k.parent.parent;
                    } else {
                        if (k == k.parent.right) {
                            k = k.parent;
                            rotateLeft(k);
                        }
                        k.parent.isRed = false;
                        k.parent.parent.isRed = true;
                        rotateRight(k.parent.parent);
                    }
                }
                if (k == root) {
                    break;
                }
            }
            root.isRed = false;
        }
        
        private void rotateLeft(RBNode x) {
            RBNode y = x.right;
            x.right = y.left;
            if (y.left != NIL) {
                y.left.parent = x;
            }
            y.parent = x.parent;
            if (x.parent == null) {
                root = y;
            } else if (x == x.parent.left) {
                x.parent.left = y;
            } else {
                x.parent.right = y;
            }
            y.left = x;
            x.parent = y;
        }
        
        private void rotateRight(RBNode x) {
            RBNode y = x.left;
            x.left = y.right;
            if (y.right != NIL) {
                y.right.parent = x;
            }
            y.parent = x.parent;
            if (x.parent == null) {
                root = y;
            } else if (x == x.parent.right) {
                x.parent.right = y;
            } else {
                x.parent.left = y;
            }
            y.right = x;
            x.parent = y;
        }
        
        public List<int[]> inOrderTraversal() {
            List<int[]> result = new ArrayList<>();
            inOrderHelper(root, result);
            return result;
        }
        
        private void inOrderHelper(RBNode node, List<int[]> result) {
            if (node != NIL) {
                inOrderHelper(node.left, result);
                result.add(new int[]{node.key, node.value});
                inOrderHelper(node.right, result);
            }
        }
    }
    
    // Floyd-Warshall Algorithm
    static class FloydWarshall {
        private int[][] dist;
        private int n;
        
        public FloydWarshall(int n) {
            this.n = n;
            this.dist = new int[n][n];
            
            // Initialize with infinity
            for (int i = 0; i < n; i++) {
                Arrays.fill(dist[i], Integer.MAX_VALUE / 2);
                dist[i][i] = 0;
            }
        }
        
        public void addEdge(int u, int v, int weight) {
            dist[u][v] = weight;
        }
        
        public void computeShortestPaths() {
            for (int k = 0; k < n; k++) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (dist[i][k] + dist[k][j] < dist[i][j]) {
                            dist[i][j] = dist[i][k] + dist[k][j];
                        }
                    }
                }
            }
        }
        
        public int getDistance(int u, int v) {
            return dist[u][v] >= Integer.MAX_VALUE / 2 ? -1 : dist[u][v];
        }
        
        public void printMatrix() {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][j] >= Integer.MAX_VALUE / 2) {
                        System.out.print("INF ");
                    } else {
                        System.out.print(dist[i][j] + " ");
                    }
                }
                System.out.println();
            }
        }
    }
    
    public static void main(String[] args) {
        // Example: Use RB Tree to store edges sorted by weight
        RedBlackTree rbTree = new RedBlackTree();
        
        // Insert edges: (weight, encoded_edge)
        rbTree.insert(4, encodeEdge(0, 1));  // Edge 0->1, weight 4
        rbTree.insert(2, encodeEdge(0, 2));  // Edge 0->2, weight 2
        rbTree.insert(3, encodeEdge(1, 2));  // Edge 1->2, weight 3
        rbTree.insert(1, encodeEdge(2, 3));  // Edge 2->3, weight 1
        rbTree.insert(5, encodeEdge(1, 3));  // Edge 1->3, weight 5
        
        // Get sorted edges
        List<int[]> sortedEdges = rbTree.inOrderTraversal();
        
        System.out.println("Sorted edges by weight:");
        for (int[] edge : sortedEdges) {
            int[] decoded = decodeEdge(edge[1]);
            System.out.println("Weight: " + edge[0] + ", Edge: " + decoded[0] + " -> " + decoded[1]);
        }
        
        // Apply Floyd-Warshall algorithm
        int numNodes = 4;
        FloydWarshall fw = new FloydWarshall(numNodes);
        
        // Add edges to graph
        for (int[] edge : sortedEdges) {
            int[] decoded = decodeEdge(edge[1]);
            fw.addEdge(decoded[0], decoded[1], edge[0]);
        }
        
        fw.computeShortestPaths();
        
        System.out.println("\nShortest path matrix:");
        fw.printMatrix();
        
        System.out.println("\nShortest distance from 0 to 3: " + fw.getDistance(0, 3));
    }
    
    private static int encodeEdge(int u, int v) {
        return (u << 16) | v;
    }
    
    private static int[] decodeEdge(int encoded) {
        return new int[]{(encoded >> 16) & 0xFFFF, encoded & 0xFFFF};
    }
}