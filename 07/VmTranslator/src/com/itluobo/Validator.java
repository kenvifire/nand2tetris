package com.itluobo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by kenvi on 17/2/26.
 */
public class Validator {
    private BufferedReader standardReader;
    private BufferedReader inputReader;

    public Validator(String fileName) throws Exception {
        standardReader = new BufferedReader(new FileReader(fileName + ".cmp"));
        inputReader = new BufferedReader(new FileReader(fileName + ".out"));
    }

    private List<Entry> parse(BufferedReader br) throws Exception {
        List<Integer> ramList = new ArrayList<>();
        List<Integer> conentList = new ArrayList<>();

        String line;
        //TODO
        while ((line = br.readLine()) != null) {
            String[] eles = line.split("|");
            for (String ele : eles) {
                ele = ele.trim();
                if(ele.equals("")) {
                    continue;
                }else if(ele.startsWith("RAM")) {

                }else{

                }
            }
        }
        return null;

    }

    static class Entry implements Comparable<Entry> {
        private int addr;
        private int content;

        @Override
        public int compareTo(Entry o) {
            return addr - o.addr;
        }

        @Override
        public boolean equals(Object obj) {
            Entry that = (Entry) obj;
            return this.addr == that.addr
                    && this.content == that.content;
        }
    }


}
