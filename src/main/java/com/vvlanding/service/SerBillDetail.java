package com.vvlanding.service;

import com.vvlanding.dto.DtoBillDetail;

import com.vvlanding.mapper.MapperBillDetail;
import com.vvlanding.table.Bill;
import com.vvlanding.table.BillDetail;
import com.vvlanding.repo.RepoBillDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SerBillDetail {
    @Autowired
    RepoBillDetail repoBillDetail;

    @Autowired
    MapperBillDetail mapperBillDetail;



//    public List<DtoBillDetail> getAllBillDetail(Pageable pageable) {
//        List<BillDetail> billDetails = repoBillDetail.findAllBy(pageable);
//        List<DtoBillDetail> dtoBillDetails = new ArrayList<>();
//        for (BillDetail billDetail : billDetails){
////                DtoBillDetail dtoBillDetail = mapperBillDetail.toDto(billDetail);
////                dtoBillDetail.setProductTitle(billDetail.getProducts().getTitle());
////                dtoBillDetail.setProductId(billDetail.getProducts().getId());
//////                dtoBillDetail.setProductSaleprice(billDetail.getProductVariations().getSaleprice());
////                dtoBillDetail.setVariantId(billDetail.getProductVariations().getId());
////                dtoBillDetail.setPropertiesId(billDetail.getProperties().getId());
////                dtoBillDetail.setPropertiesTitle(billDetail.getProperties().getKeyname());
////                dtoBillDetail.setPropertiesValue(billDetail.getProperties().getValue());
//            dtoBillDetails.add(dtoBillDetail);
//        }
//        return dtoBillDetails;
//    }

//    public List<DtoBillDetail> getAllBillDetailByBillId(Bill bill) {
//        List<BillDetail> billDetails = repoBillDetail.findAllByBill(bill);
//        List<DtoBillDetail> dtoBillDetails = new ArrayList<>();
//        for (BillDetail billDetail : billDetails){
//            DtoBillDetail dtoBillDetail = mapperBillDetail.toDto(billDetail);
////                dtoBillDetail.setProductTitle(billDetail.getProducts().getTitle());
////                dtoBillDetail.setProductSaleprice(billDetail.getProductVariations().getSaleprice());
////            dtoBillDetail.setVariantId(billDetail.getProductVariations().getId());
////                dtoBillDetail.setProductId(billDetail.getProducts().getId());
////                dtoBillDetail.setPropertiesId(billDetail.getProperties().getId());
////                dtoBillDetail.setPropertiesTitle(billDetail.getProperties().getKeyname());
////                dtoBillDetail.setPropertiesValue(billDetail.getProperties().getValue());
//            dtoBillDetails.add(dtoBillDetail);
//        }
//        return dtoBillDetails;
//    }

    public Optional<BillDetail> FindById(Long id) {
        return repoBillDetail.findById(id);
    }


}
