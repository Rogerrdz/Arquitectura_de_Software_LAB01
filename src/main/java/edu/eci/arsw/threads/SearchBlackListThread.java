package edu.eci.arsw.threads;

import edu.eci.arsw.blacklistvalidator.HostBlackListsValidator;
import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.List;

public class SearchBlackListThread extends Thread {

    private int start;
    private int end;
    private String ip;
    private List<Integer> results;
    private HostBlackListsValidator validator = new HostBlackListsValidator();

    public SearchBlackListThread(int start, int end,String ip,List<Integer> results) {
        this.start = start;
        this.end = end;
        this.ip = ip;
        this.results = results;
    }

    @Override
    public void run() {
        HostBlacklistsDataSourceFacade skds =HostBlacklistsDataSourceFacade.getInstance();
        for (int i = start; i < end && !validator.Stop(); i++) {
            if (skds.isInBlackListServer(i, ip)) {
                results.add(i); 
                if (results.size() >= 5) {
                    validator.setStop();
              }
        }
    }
}
}
