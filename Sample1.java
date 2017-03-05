package sample1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class Sample1 {

    private void solve() throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader("sample_data_1.csv"));
            
        // initializing variables
        String s = br.readLine();
        int minSup = 1;
        HashSet<Entry> set = new HashSet<>();
        HashMap<Entry, Integer> freq = new HashMap<>();
        int counter = 1;

        // reading csv file
        while (s != null && s.length() != 0) {
            String entry[] = s.split(",");
            Entry e = new Entry(entry[0], entry[1], entry[2]);

            // counting frequency
            if (set.add(e)) {
                System.out.println((counter++) + " Unique");
                freq.put(e, 1);
            } else {
                int x = freq.get(e);
                System.out.println((counter++) + " Varient");
                freq.replace(e, x + 1);
            }
            s = br.readLine();
            //System.out.println(counter++);
        }
        // iterating through list
        for (Map.Entry x : freq.entrySet()) {
            System.out.println(x.getKey() + " " + x.getValue());
        }

        // deleting entries where freq < minSup
        ArrayList<Entry> a = new ArrayList<>();
        for (Map.Entry y : freq.entrySet()) {
            if ((int) y.getValue() < minSup) {
                a.add((Entry) y.getKey());
            }
        }
        for (Entry x : a) {
            freq.remove(x);
        }
        System.out.println("After deleting  ........................ ");

        ObjectInfo object = new ObjectInfo();
        for (Map.Entry x : freq.entrySet()) {
            object.insert(((Entry) x.getKey()).toString());
        }

    }

    class ObjectInfo {

        private ObjectNode root;

        public ObjectInfo() {
            root = new ObjectNode("");
        }

        public void insert(String entry) {

            ObjectNode current = root;
            for (String ch : entry.split(":")) {
                ObjectNode child = current.subNode(ch);
                if (child != null) {
                    current = child;
                } else {
                    current.childList.add(new ObjectNode(ch));
                    current = current.subNode(ch);
                }
                current.count++;
            }
            current.isEnd = true;
        }

        public boolean search(String entry) {
            ObjectNode current = root;
            for (String ch : entry.split(":")) {
                if (current.subNode(ch) == null) {
                    return false;
                } else {
                    current = current.subNode(ch);
                }
            }
            if (current.isEnd == true) {
                return true;
            }
            return false;
        }
    }

    class ObjectNode {

        String content;
        boolean isEnd;
        int count;
        LinkedList<ObjectNode> childList;

        /* Constructor */
        public ObjectNode(String c) {
            childList = new LinkedList<ObjectNode>();
            isEnd = false;
            content = c;
            count = 0;
        }

        public ObjectNode subNode(String c) {
            if (childList != null) {
                for (ObjectNode eachChild : childList) {
                    //System.out.println(eachChild.content);
                    if (eachChild.content.equals(c)) {
                        return eachChild;
                    }
                }
            }
            return null;
        }
    }

    class Entry {

        String subject, predicate, object;

        Entry() {
            subject = "";
            predicate = "";
            object = "";
        }

        public Entry(String subject, String predicate, String object) {
            this.subject = subject;
            this.predicate = predicate;
            this.object = object;
        }

        @Override
        public boolean equals(Object obj) {
            // System.out.println((Entry) obj);
            if (obj instanceof Entry) {
                if (this.object.equals(((Entry) obj).object) && this.predicate.equals(((Entry) obj).predicate) && this.subject.equals(((Entry) obj).subject)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return subject + ":" + predicate + ":" + object;
        }

        @Override
        public int hashCode() {
            return (subject + ":" + predicate + ":" + object).hashCode(); //To change body of generated methods, choose Tools | Templates.
        }

    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        (new Sample1()).solve();
    }
}
