package com.vvlanding.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vvlanding.table.AddressProvince;
import com.vvlanding.table.AddressDistrict;
import com.vvlanding.table.AddressWard;
import com.vvlanding.repo.DistrictRepository;
import com.vvlanding.repo.ProvinceRepository;
import com.vvlanding.repo.WardRepository;

@Service
public class AddressService {

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private WardRepository wardRepository;

    public Optional<AddressProvince> findProvince(String province){
        return provinceRepository.findByNameIgnoreCase(province);
    }

    public Optional<AddressProvince> getProvince(String provinceID) {
        return provinceRepository.findById(provinceID);
    }

    public Optional<AddressDistrict> findDistrict(String district){
        return districtRepository.findByNameIgnoreCase(district);
    }

    public Optional<AddressDistrict> getDistrict(String districtID) {
        return districtRepository.findById(districtID);
    }


    public Optional<AddressWard> findWard(String ward){
        return wardRepository.findByNameIgnoreCase(ward);
    }

    public Optional<AddressWard> getWard(String wardID) {
        return wardRepository.findById(wardID);
    }

    public void saveAll(List<AddressProvince> provinces) {
        for (AddressProvince province : provinces) {
            save(province);
        }
    }

    public void save(AddressProvince province) {
        List<AddressDistrict> districts = province.getDistricts();
        for (AddressDistrict district : districts) {
            List<AddressWard> wards = district.getWards();
            if (wards != null) {
                wardRepository.saveAll(wards);
            } else {
                System.out.println(district.getName() + " no wards");
            }
            districtRepository.save(district);
        }
        provinceRepository.save(province);
    }
    public List<AddressProvince> getAll() {
        return (List<AddressProvince>) provinceRepository.findAll();
    }

    public List<AddressProvince> getProvinces() {
        return provinceRepository.findAll();
    }
}
