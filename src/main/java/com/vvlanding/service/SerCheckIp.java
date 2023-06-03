package com.vvlanding.service;

import com.vvlanding.repo.RepoBill;
import com.vvlanding.repo.RepoCheckip;
import com.vvlanding.table.Bill;
import com.vvlanding.table.Checkip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SerCheckIp {

    @Autowired
    RepoCheckip repoCheckip;

    @Autowired
    RepoBill repoBill;

//    public Checkip insertIp(String checkIp){
//        try {
//            Date date = new Date();
//            Checkip checkip = new Checkip();
//            checkip.setIpaddress(checkIp);
//            checkip.setReject(true);
//            checkip.setTimepost(date.getTime());
//            Checkip checkip1 = repoCheckip.save(checkip);
//            return checkip1;
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
    public boolean checkIp(String ipAddress){
        if (checkBill(ipAddress) == false) return false;

        try {
            List<Bill> list = repoBill.findIdAddress(ipAddress);
            if (list.isEmpty()) return true;
            List<Checkip> listIp = repoCheckip.findAllByIpaddress(ipAddress);
            Date date = new Date();
            int index = 1;
            int ind = 0;
            if (checkReject(ipAddress) == false){
                long time = list.get(0).getCreatedDate().getTime();
                System.out.println(list.get(0).getId());
                long times = date.getTime() - time;
                if (times > 86400000){
                    for (Checkip c:
                            listIp) {
                        Checkip checkip = c;
                        checkip.setReject(true);
                        repoCheckip.save(checkip);
                    }
                    return true;
                }else {
                    return false;
                }
            }
            for (Bill c: list) {
                long times = date.getTime() - c.getCreatedDate().getTime();
                if (times < 60000){
                    index++;
                }
            }
            if (index >= 10){
                for (Checkip c:
                        listIp) {
                    Checkip checkip = c;
                    checkip.setReject(false);
                    repoCheckip.save(checkip);
                }
                return false;
            }
            return true;
        }catch (InvalidDataAccessApiUsageException e){
            e.printStackTrace();
            return true;
        }
    }
    public boolean checkBill(String idAddress){
        try {
            List<Checkip> list = repoCheckip.findAllByIpaddress(idAddress);
            if (list.size() > 0){
                return true;
            }else return false;
        }catch (Exception e){
            return false;
        }
    }
    public boolean checkReject(String idAddress){
        try {
            int index = 0;
            List<Checkip> list = repoCheckip.findAllByIpaddress(idAddress);
            for (Checkip c:
                 list) {
                if (c.getReject() == false){
                    index = 1;
                    break;
                }
            }
            if (index == 1){
                return false;
            }else return true;
        }catch (InvalidDataAccessApiUsageException e){
            return false;
        }
    }
}
