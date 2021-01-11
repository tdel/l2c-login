package main;

import kernel.Kernel;

public class Main {

    public static void main(String[] args) throws Exception {
         Kernel kernel = new Kernel();
         kernel.boot();
    }
}
