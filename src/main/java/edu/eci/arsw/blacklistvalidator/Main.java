/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import java.util.List;

/**
 *
 * @author hcadavid
 */
public class Main {
    
    public static void main(String a[]){
        HostBlackListsValidator validator = new HostBlackListsValidator();

        /**List<Integer> blackListOcurrences=vhblv.checkHost("202.24.34.55",4);
        System.out.println("The host was found in the following blacklists:"+blackListOcurrences);
        */

        String ip = "202.24.34.55";

        int cores = Runtime.getRuntime().availableProcessors();

        int[] tests = {
            /**1,
            cores,
            cores * 2,
            100*/1000
        
        };

        for (int nThreads : tests) {

            long start = System.currentTimeMillis();

            validator.checkHost(ip, nThreads);

            long end = System.currentTimeMillis();

            System.out.println(
                "Hilos: " + nThreads +
                " | Tiempo: " + (end - start) + " ms"
            );
        }
    }
}
    

