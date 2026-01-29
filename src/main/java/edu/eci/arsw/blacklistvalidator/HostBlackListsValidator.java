/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.eci.arsw.threads.SearchBlackListThread;


/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT = 5;
    
    public boolean stop=false;

    public synchronized boolean Stop() {
    return stop;
    }

    public synchronized void setStop() {
        stop = true;
    }

    public List<Integer> checkHost(String ipaddress, int N) {

        LinkedList<Integer> blackListOcurrences = new LinkedList<>();
        List<SearchBlackListThread> threads = new LinkedList<>();

        HostBlacklistsDataSourceFacade skds =HostBlacklistsDataSourceFacade.getInstance();

        int totalServers = skds.getRegisteredServersCount();
        int divisions = totalServers / N;

        for (int i = 0; i < N; i++) {
            int start = i * divisions;
            int end = 0;
            if (i == N - 1) {
                end = totalServers;
            }else {
                end = (i + 1) * divisions;
            }
            SearchBlackListThread t = new SearchBlackListThread(start, end, ipaddress, blackListOcurrences);
            threads.add(t);
            t.start();
        }

        for (SearchBlackListThread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Logger.getLogger(HostBlackListsValidator.class.getName())
                        .log(Level.SEVERE, null, e);
            }
        }

        if (blackListOcurrences.size() >= BLACK_LIST_ALARM_COUNT) {
            skds.reportAsNotTrustworthy(ipaddress);
        } else {
            skds.reportAsTrustworthy(ipaddress);
        }

        LOG.log(Level.INFO,"Checked Black Lists:{0} of {1}",new Object[]{totalServers, totalServers});
        return blackListOcurrences;
    }

    private static final Logger LOG =
            Logger.getLogger(HostBlackListsValidator.class.getName());
}
    

