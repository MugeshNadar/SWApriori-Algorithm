package sample1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

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
        HashSet<Item> tis = generate2LArgeItemsets(object, minSup);

        ArrayList<HashSet<Item>> fis = new ArrayList<>();
        for (Item i : tis) {
            HashSet<Item> t = (new HashSet<Item>());
            t.add(i);
            fis.add(t);
        }

        HashSet<HashSet<Item>> allfis = new HashSet<>(fis);

        System.out.println(fis);
        int l = 1;
        do {
            l++;
            ArrayList<HashSet<Item>> candidates = new ArrayList<>();

            for (int i = 0; i < fis.size(); i++) {
                for (int j = i + 1; j < fis.size(); j++) {
                    HashSet<Item> is1 = new HashSet<>(fis.get(i));
                    HashSet<Item> is2 = new HashSet<>(fis.get(j));

                    HashSet<Item> is3 = new HashSet<>();
                    if (is1.size() <= l && is2.size() <= l) {
                        for (Item x : is1) {
                            is3.add(x);
                        }
                        for (Item x : is2) {
                            is3.add(x);
                        }
                    }
                    candidates.add(new HashSet<>(is3));
                }
            }
            System.out.println(candidates);

            fis = null;
            for (HashSet<Item> x : candidates) {

                if (Support(x) > minSup && true) {// fill in the condition appropriately  instead of just true
                    fis.add(new HashSet<>(x));
                }
            }

            for (HashSet<Item> x : fis) {
                if (!allfis.contains(x)) {
                    allfis.add(x);
                }
            }

        } while (fis.size() > 0);

        // rules = generateRules(parameter1, parameter2);
    }

    private HashSet<Item> generate2LArgeItemsets(ObjectInfo object, int minSup) {

        //Reading Content
        for (ObjectNode a : object.root.childList) {
            System.out.print(a.content + ": ");
            for (ObjectNode b : a.childList) {
                System.out.print(b.content + ": ");
                for (ObjectNode c : b.childList) {
                    System.out.println(c.content);
                }
            }
        }

        // Collection<Set<ObjectNode>> a= new ObjectNode(())
        HashSet<Item> lis = new HashSet<>();
        for (int i = 0; i < object.root.childList.size(); i++) {
            for (int j = i + 1; j < object.root.childList.size(); j++) {
                ObjectNode ob1 = object.root.childList.get(i);
                ObjectNode ob2 = object.root.childList.get(j);

                System.out.println(ob1.content);
                System.out.println(ob2.content);

                for (int a = 0; a < ob1.childList.size(); a++) {
                    for (int b = 0; b < ob2.childList.size(); b++) {
                        ObjectNode r1 = ob1.childList.get(a);
                        ObjectNode r2 = ob2.childList.get(b);

                        System.out.println("\t" + r1.content);
                        System.out.println("\t" + r2.content);

                        int count = 0;
                        for (ObjectNode s1 : r1.childList) {
                            for (ObjectNode s2 : r2.childList) {
                                if (s1.content.equals(s2.content)) {
                                    count++;
                                }
                            }
                        }
                        System.out.println("\t\t" + count);
                        if (count >= minSup) {
                            lis.add(new Item(ob1.content, r1.content));
                            lis.add(new Item(ob2.content, r2.content));
                        }
                    }
                }
            }
        }

        System.out.println(lis);
        return lis;
    }

    private int Support(HashSet<Item> x) {
        return 3;
    }

    class Item {

        String object, predicate;

        public Item() {
            object = "";
            predicate = "";
        }

        public Item(Item i) {
            object = i.object;
            predicate = i.predicate;
        }

        public Item(String object, String predicate) {
            this.object = object;
            this.predicate = predicate;
        }

        @Override
        public boolean equals(Object obj) {

            if (obj instanceof Item) {
                if (this.object.equals(((Item) obj).object) && this.predicate.equals(((Item) obj).predicate)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return object + ":" + predicate;
        }

        @Override
        public int hashCode() {
            return (object + ":" + predicate).hashCode(); //To change body of generated methods, choose Tools | Templates.
        }

    }

    class ObjectInfo {

        private ObjectNode root;

        public ObjectInfo() {
            root = new ObjectNode("");
        }

        public void insert(String entry) {

            ObjectNode current = root;
            String s[] = entry.split(":");
            String p = s[0];
            s[0] = s[2];
            s[2] = p;
            for (String ch : s) {
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

/*
        for (ObjectNode a : object.root.childList) {
            System.out.println(a.content + ": ");

            for (int i = 0; i < a.childList.size(); i++) {
                for (int j = i + 1; j < a.childList.size(); j++) {
                    HashSet<String> p = new HashSet<>();
                    HashSet<String> q = new HashSet<>();
                    System.out.print(a.childList.get(i).content+","+a.childList.get(j).content+" : ");
                    for(ObjectNode x:a.childList.get(i).childList){
                        p.add(x.content);
                    }
                    for(ObjectNode x:a.childList.get(j).childList){
                        q.add(x.content);
                    }
                    p.retainAll(q);
                    System.out.println(p.size());
                }
            }
            /*
            for (ObjectNode b : a.childList) {
                System.out.print(b.content + ": ");
                int count = 0;
                // comparing all pairs of subjects
               /* for (int i = 0; i < b.childList.size(); i++) {
                    for (int j = i + 1; j < b.childList.size(); j++) {
                        if (b.childList.get(i).content.equals(b.childList.get(j).content)) {
                            count++;
                        }
                    }
                    
                }
                
                System.out.println(count);
            }*
        }*/
