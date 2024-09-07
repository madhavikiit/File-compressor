import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// HuffmanNode Class Definition
class HuffmanNode {
    int data;  // Frequency of the character
    char c;    // The character

    HuffmanNode left;  // Left child
    HuffmanNode right; // Right child
}

public class HuffmanCoding {

    private static Map<Character, String> huffmanCodeMap = new HashMap<>();
    private static HuffmanNode huffmanTreeRoot = null; // Save the root of the Huffman Tree

    // Function to build the Huffman tree
    public static HuffmanNode buildHuffmanTree(char[] charArray, int[] charFreq, int n) {
        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>(n, new MyComparator());

        for (int i = 0; i < n; i++) {
            HuffmanNode hn = new HuffmanNode();
            hn.c = charArray[i];
            hn.data = charFreq[i];
            hn.left = null;
            hn.right = null;
            queue.add(hn);
        }

        HuffmanNode root = null;

        while (queue.size() > 1) {
            HuffmanNode x = queue.poll();
            HuffmanNode y = queue.poll();

            HuffmanNode f = new HuffmanNode();
            f.data = x.data + y.data;
            f.c = '-';
            f.left = x;
            f.right = y;

            root = f;
            queue.add(f);
        }

        return root; // Return the root of the Huffman tree
    }

    // Function to compress the input string using Huffman coding
    public static String compress(String input) {
        StringBuilder encodedString = new StringBuilder();
        for (char c : input.toCharArray()) {
            encodedString.append(huffmanCodeMap.get(c));
        }
        return encodedString.toString();
    }

    // Function to decompress the compressed string
    public static String decompress(HuffmanNode root, String compressedData) {
        StringBuilder result = new StringBuilder();
        HuffmanNode currentNode = root;
        for (int i = 0; i < compressedData.length(); i++) {
            char bit = compressedData.charAt(i);
            currentNode = (bit == '0') ? currentNode.left : currentNode.right;

            if (currentNode.left == null && currentNode.right == null) {
                result.append(currentNode.c);
                currentNode = root;
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the text to compress: ");
        String input = sc.nextLine();

        // Calculate frequencies
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : input.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        int n = freqMap.size();
        char[] charArray = new char[n];
        int[] charFreq = new int[n];

        int index = 0;
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            charArray[index] = entry.getKey();
            charFreq[index] = entry.getValue();
            index++;
        }

        // Build Huffman tree once and save its root
        huffmanTreeRoot = buildHuffmanTree(charArray, charFreq, n);
        generateCodes(huffmanTreeRoot, "");

        String compressedData = compress(input);
        System.out.println("Compressed Data: " + compressedData);

        // Decompress using the same Huffman tree
        String decompressedData = decompress(huffmanTreeRoot, compressedData);

        System.out.println("Decompressed Data: " + decompressedData);
    }

    // Function to generate Huffman codes
    public static void generateCodes(HuffmanNode root, String s) {
        // If the current node is null, return (base case)
        if (root == null) {
            return;
        }
    
        // If we reach a leaf node (node with no children)
        if (root.left == null && root.right == null) {
            huffmanCodeMap.put(root.c, s);
            return;
        }
    
        // Traverse the left and right subtree
        generateCodes(root.left, s + "0");
        generateCodes(root.right, s + "1");
    }
    
}

// Comparator for HuffmanNode
class MyComparator implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode x, HuffmanNode y) {
        return x.data - y.data;
    }
}
