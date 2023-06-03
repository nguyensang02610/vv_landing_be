package com.vvlanding.service;

import com.vvlanding.dto.DtoBill;
import com.vvlanding.dto.DtoReview;
import com.vvlanding.repo.RepoReview;
import com.vvlanding.repo.RepoShopInfo;
import com.vvlanding.table.Bill;
import com.vvlanding.table.Review;
import com.vvlanding.table.ShopInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SerReview {

    @Autowired
    RepoReview repoReview;

    @Autowired
    RepoShopInfo repoShopInfo;
    private List<DtoReview> getDtoBills(List<DtoReview> dtoReviews, List<Review> reviews) {
        for(Review r : reviews){
            DtoReview dtoReview = new DtoReview();
            dtoReview.Review(r.getId(),r.getName(),r.getContent(),r.getImg(),r.getShopInfo().getId(),r.getView());
            dtoReviews.add(dtoReview);
        }
        return dtoReviews.stream()
                .sorted(Comparator.comparing(DtoReview::getId).reversed())
                .collect(Collectors.toList());
    }

    public List<DtoReview> getAll(Long id){
        List<DtoReview> dtoReviews = new ArrayList<>();
        return getDtoBills(dtoReviews,repoReview.findAllByShopInfoId(id));
    }

    public Object insert(DtoReview dtoReview){
        Map<String, Object> response = new HashMap<>();
        Optional<ShopInfo> opShopInfo = repoShopInfo.findById(dtoReview.getShopId());
        if (!opShopInfo.isPresent()) {
            response.put("message", "Không tìm thấy thông tin shop !!");
            response.put("success", false);
            return response;
        }
        else {
            Review review = new Review();
            review.setName(dtoReview.getName());
            review.setContent(dtoReview.getContent());
            review.setImg(dtoReview.getImg());
            review.setShopInfo(opShopInfo.get());
            repoReview.save(review);
            response.put("success", true);
            return response;
        }
    }
    public Object update(DtoReview dtoReview){
        Map<String, Object> response = new HashMap<>();
        Optional<Review> optionalReview = repoReview.findByIdAndAndShopInfoId(dtoReview.getId(),dtoReview.getShopId());
        if (!optionalReview.isPresent()) {
            response.put("message", "Không tìm thấy thông tin shop !!");
            response.put("success", false);
            return response;
        }
        else {
            Review review = repoReview.getOne(dtoReview.getId());
            review.setName(dtoReview.getName());
            review.setContent(dtoReview.getContent());
            review.setImg(dtoReview.getImg());
            repoReview.save(review);
            response.put("success", true);
            return response;
        }
    }
    public Object delete(Long id){
        Map<String,Object> response = new HashMap<>();
        Optional<Review> optionalReview = repoReview.findById(id);
        if (!optionalReview.isPresent()){
            response.put("message","Không tìm thấy review");
            response.put("success",false);
            return response;
        }
        else {
            repoReview.deleteById(id);
            return response;
        }
    }

}
