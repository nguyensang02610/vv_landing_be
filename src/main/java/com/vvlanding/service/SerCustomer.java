package com.vvlanding.service;

import com.vvlanding.dto.DtoBill;
import com.vvlanding.dto.DtoCustomer;
import com.vvlanding.dto.DtoOrderDetail;
import com.vvlanding.mapper.MapperCustomer;
import com.vvlanding.table.Bill;
import com.vvlanding.table.BillDetail;
import com.vvlanding.table.Customer;
import com.vvlanding.repo.RepoCustomer;
import com.vvlanding.table.ShopInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SerCustomer {
    @Autowired
    RepoCustomer repoCustomer;

    @Autowired
    MapperCustomer mapperCustomer;

    public List<DtoCustomer> getCustomerOfShopAndPage(ShopInfo shopInfo, Pageable pageable) {
        List<DtoCustomer> listCustomer = new ArrayList<>();
        List<Customer> customers = repoCustomer.findAllByShopId(shopInfo.getId(), pageable);
        return getDtoCustomers(shopInfo, listCustomer, customers);
    }

    public List<DtoCustomer> findCustomerOfShop(ShopInfo shopInfo, String s, String query) {
        List<DtoCustomer> listCustomer = new ArrayList<>();
        List<Customer> customerFind = repoCustomer.findByShopIdAndPhoneOrTitle(shopInfo.getId(), s, query);
        return getDtoCustomers(shopInfo, listCustomer, customerFind);
    }

    public List<DtoCustomer> getCustomerOfShop(ShopInfo shopInfo) {
        List<DtoCustomer> listCustomer = new ArrayList<>();
        List<Customer> repoListCustomers = repoCustomer.findAllByShopId(shopInfo.getId());
        return getDtoCustomers(shopInfo, listCustomer, repoListCustomers);
    }

    private List<DtoCustomer> getDtoCustomers(ShopInfo shopInfo, List<DtoCustomer> listCustomer, List<Customer> repoListCustomers) {
        for (Customer c : repoListCustomers) {
            DtoCustomer customer = mapperCustomer.toDto(c);
            customer.setShopTitle(shopInfo.getTitle());
            customer.setShopId(shopInfo.getId());
            listCustomer.add(customer);
        }
        return listCustomer.stream()
                .sorted(Comparator.comparing(DtoCustomer::getId).reversed())
                .collect(Collectors.toList());
    }

    public Optional<Customer> FindById(Long id) {
        return repoCustomer.findById(id);
    }

    public void Delete(Long id) {
        repoCustomer.deleteById(id);
    }

    // Sửa thông tin khách hàng
    public ResponseEntity updateCustomer(DtoCustomer dtoCustomer, Long shopId) {
        Map<String, Object> response = new HashMap<>();

        // check customer
        Optional<Customer> opCustomer = repoCustomer.findByIdAndShopId(dtoCustomer.getId(), shopId);
        if (!opCustomer.isPresent()) {
            response.put("mesager", "không tìm thấy thông khách hàng  !!!!");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Customer customer = opCustomer.get();
        customer.setTitle(dtoCustomer.getTitle());
        customer.setPhone(dtoCustomer.getPhone());
        customer.setAddress(dtoCustomer.getAddress());
        customer.setProvince(dtoCustomer.getProvince());
        customer.setDistrict(dtoCustomer.getDistrict());
        customer.setWard(dtoCustomer.getWard());
        customer.setActive(true);
        Customer updateCustomer = repoCustomer.save(customer);
        response.put("data", updateCustomer);
        response.put("success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public ResponseEntity getDetail(ShopInfo shopInfo, Long id) {
        Optional<Customer> opCustomer = repoCustomer.findByIdAndShopId(id, shopInfo.getId());
        Map<String, Object> response = new HashMap<>();
        Customer customer = opCustomer.get();
        if (!opCustomer.isPresent()) {
            response.put("message", "Không tìm thấy thông tin khách hàng");
            response.put("success", "false");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } else {
            DtoCustomer dtoCustomer = new DtoCustomer();
            dtoCustomer.setId(customer.getId());
            dtoCustomer.setTitle(customer.getTitle());
            dtoCustomer.setPhone(customer.getPhone());
            dtoCustomer.setAddress(customer.getAddress());
            dtoCustomer.setProvince(customer.getProvince());
            dtoCustomer.setDistrict(customer.getDistrict());
            dtoCustomer.setWard(customer.getWard());
            dtoCustomer.setShopId(customer.getShopId());

            response.put("data", dtoCustomer);
            response.put("message", "Thông tin khách hàng");
            response.put("success", "true");
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }

}