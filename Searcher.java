package com.company;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Searcher {

    static class TrieNode {
        public TrieNode[] children = new TrieNode[26];
        public boolean isWord;
    }

    static class Trie {
        public TrieNode root = new TrieNode();

        public void insert(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                if (node.children[c - 'a'] == null) {
                    node.children[c - 'a'] = new TrieNode();
                }
                node = node.children[c - 'a'];
            }
            node.isWord = true;
        }

        public boolean search(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                if (node.children[c - 'a'] == null)
                    return false;
                node = node.children[c - 'a'];
            }
            return node.isWord;
        }

    }


    public static double calculatePercentage(String givenData, Trie trie) {
        double countExisted = 0;
        String[] data = givenData.split(" ");

        for (String word : data) {
            if (trie.search(word)) {
                countExisted++;
            }

        }
        return ((countExisted / data.length) * 100);
    }

    public static String readFileAsString(String fileName) throws Exception {
        StringBuilder data = new StringBuilder();
        data.append(new String(Files.readAllBytes(Paths.get(fileName))));
        return data.toString();
    }


    public static void main(String[] args) throws Exception {
        HashMap<String, Trie> result = new HashMap<String, Trie>();
        if (args.length == 0) {
            throw new IllegalArgumentException("No directory given to index.");
        }


        final File indexableDirectory = new File(args[0]);

        FilenameFilter textFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".txt");
            }
        };

        File[] files = indexableDirectory.listFiles(textFilter);


        for (File file : files) {
            String[] data = readFileAsString(args[0] + file.getName()).split(" ");
            Trie node = new Trie();
            for (String item : data) {
                node.insert(item);
            }
            result.put(file.getName(), node);
        }


        System.out.println(files.length + " directories read in the directory");

        try (Scanner keyboard = new Scanner(System.in)) {
            while (true) {
                int wordCount = 0;
                System.out.print("search> ");
                final String line = keyboard.nextLine();

                if (line.equals(":quit")) {
                    System.exit(0);
                }


                if (line.isEmpty()) {
                    System.out.println("Please give set of words");
                } else {
                    for (Map.Entry<String, Trie> a : result.entrySet()) {
                        if (Math.ceil(calculatePercentage(line, a.getValue())) > 0) {
                            System.out.println(a.getKey() + ":" + (int) (Math.ceil(calculatePercentage(line, a.getValue()))) + "%");
                        } else {
                            System.out.println("no matches found");
                        }

                    }
                }
            }
        }
    }
}


