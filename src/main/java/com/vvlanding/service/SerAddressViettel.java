package com.vvlanding.service;

import com.vvlanding.repo.RepoDistrictViettel;
import com.vvlanding.repo.RepoProvinceViettel;
import com.vvlanding.repo.RepoWardViettel;
import com.vvlanding.table.ViettelAddressDistrict;
import com.vvlanding.table.ViettelAddressProvince;
import com.vvlanding.table.ViettelAddressWard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SerAddressViettel {

    @Autowired
    private RepoWardViettel repoWardViettel;

    @Autowired
    private RepoDistrictViettel repoDistrictViettel;

    @Autowired
    private RepoProvinceViettel repoProvinceViettel;

    public List<ViettelAddressProvince> insertAllProvince(List<ViettelAddressProvince> provinces){
        for (ViettelAddressProvince p: provinces) {
            repoProvinceViettel.save(p);
        }
        return null;
    }
    public List<ViettelAddressDistrict> insertAllDistrict(List<ViettelAddressDistrict> districts){

        for (ViettelAddressDistrict d: districts) {
            repoDistrictViettel.save(d);
        }
        return null;
    }
    public List<ViettelAddressWard> insertAllWard(List<ViettelAddressWard> wards){
        for (ViettelAddressWard w: wards) {
            repoWardViettel.save(w);
        }
        return null;
    }
}
