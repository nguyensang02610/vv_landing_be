package com.vvlanding.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvlanding.dto.DtoProduct;
import com.vvlanding.dto.DtoProductVariations;
import com.vvlanding.mapper.MapperProduct;
import com.vvlanding.mapper.MapperProductVariations;
import com.vvlanding.repo.*;
import com.vvlanding.security.UserPrincipal;
import com.vvlanding.table.*;
import com.vvlanding.table.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SerProduct {
    @Autowired
    RepoProduct repoProduct;

    @Autowired
    RepoUnit repoUnit;

    @Autowired
    RepoShopInfo repoShopInfo;

    @Autowired
    RepoShopUserRole repoShopUserRole;

    @Autowired
    RepoProductCategorie repoProductCategorie;

    @Autowired
    MapperProduct mapperProduct;

    @Autowired
    SerProductVariations serProductVariations;

    @Autowired
    RepoProductVariations repoProductVariations;

    @Autowired
    MapperProductVariations mapperProductVariations;

    @Autowired
    RepoProductTag repoProductTag;

    @Autowired
    RepoProperties repoProperties;

    @Autowired
    RepoProductShopee repoProductShopee;


    public Optional<Product> find(long id) {
        return repoProduct.findById(id);
    }

    public Optional<Product> findByShopToken(long id, String shopToken) {
        return repoProduct.findAllByIdAndShopInfoShopToken(id, shopToken);
    }

    public List<DtoProduct> findProductByQuery(ShopInfo shopInfo, String s, String query) {
        List<DtoProduct> listDtoProduct = new ArrayList<>();
        List<Product> listProduct = repoProduct.findAllByShopInfo_IdAndTitleOrSku(shopInfo.getId(), s, query);
        for (Product product : listProduct) {
            if (product.getIsActive()) {
                List<ProductVariations> productVariants = serProductVariations.findByProductId(product.getId());
                DtoProduct dtoProduct = producVariants2Json(product.getShopInfo(), product, productVariants);
                listDtoProduct.add(dtoProduct);
            }
        }
        return listDtoProduct.stream()
                .sorted(Comparator.comparing(DtoProduct::getId).reversed())
                .collect(Collectors.toList());
    }

    public List<DtoProduct> findProductByQueryPage(ShopInfo shopInfo, String s, String query, Pageable pageable) {
        List<DtoProduct> listDtoProduct = new ArrayList<>();
        List<Product> listProduct = repoProduct.findAllByShopInfo_IdAndTitleOrSku(shopInfo.getId(), s, query, pageable);
        for (Product product : listProduct) {
            if (product.getIsActive()) {
                List<ProductVariations> productVariants = serProductVariations.findByProductId(product.getId());
                DtoProduct dtoProduct = producVariants2Json(product.getShopInfo(), product, productVariants);
                listDtoProduct.add(dtoProduct);
            }
        }
        return listDtoProduct.stream()
                .sorted(Comparator.comparing(DtoProduct::getId).reversed())
                .collect(Collectors.toList());
    }

    public List<DtoProduct> getProductOfShopId(ShopInfo shop) {
        List<DtoProduct> listDtoProduct = new ArrayList<>();
        List<Product> product = repoProduct.findAllByShopInfoIdAndIsActive(shop.getId(), true);
        return getDtoProducts(shop, listDtoProduct, product);
    }

    public List<DtoProduct> getProductOfShopPage(ShopInfo shop, Pageable pageable) {
        List<DtoProduct> listDtoProduct = new ArrayList<>();
        List<Product> listData = repoProduct.findAllByShopInfoIdAndIsActive(shop.getId(), true, pageable);
        return getDtoProducts(shop, listDtoProduct, listData);
    }

    private List<DtoProduct> getDtoProducts(ShopInfo shop, List<DtoProduct> listDtoProduct, List<Product> product) {
        for (Product p : product) {
            List<ProductVariations> productVariants = serProductVariations.findByProductId(p.getId());
            DtoProduct dtoProduct = producVariants2Json(shop, p, productVariants);
            if (dtoProduct.getIsActive()) {
                listDtoProduct.add(dtoProduct);
            }
        }
        return listDtoProduct.stream()
                .sorted(Comparator.comparing(DtoProduct::getId).reversed())
                .collect(Collectors.toList());
    }

    // check san pham
    public Product checkProduct(Long id, Long shopID) {
        Optional<Product> opProduct = repoProduct.findByIdAndShopInfo_Id(id, shopID);
        if (!opProduct.isPresent()) {
            return null;
        }
        return opProduct.get();
    }

    // check don vị tính nếu có thì trả ra nếu không thì thêm mới ròi trả ra
    public Units checkUnit(String unit) {
        Units units = checkProductUnit(unit);
        if (units == null) {
            Units newUnits = new Units();
            newUnits.setName(unit);
            newUnits.setActive(true);
            units = repoUnit.save(newUnits);
        }
        return units;
    }

    // check  list tag nếu có thì trả ra nếu không thì thêm mới rồi trả ra
    private void checkListTags(List<String> tags, List<ProductTag> productTags) {
        for (String item : tags) {
            ProductTag productTag = checkProductTag(item);
            if (productTag == null) {
                ProductTag newProducttag = new ProductTag();
                newProducttag.setActive(true);
                newProducttag.setTitle(item);
                productTag = repoProductTag.save(newProducttag);
            }
            productTags.add(productTag);
        }
    }


    // cập nhật thông tin sản phẩm
    public ResponseEntity updateProduct(DtoProduct dtoProduct, Long shopId) {
        Map<String, Object> response = new HashMap<>();
        // tìm tên đơn vị tính nêu có thì trả ra
        String unit = dtoProduct.getUnit();
        Units units = checkUnit(unit);
        // duyệt và check thẻ tag
        List<String> _tags = dtoProduct.getTags();
        List<ProductTag> productTags = new ArrayList<>();
        if (_tags.size() > 0) {
            checkListTags(_tags, productTags);
        }

        // check san pham
        Optional<Product> opProduct = repoProduct.findByIdAndShopInfo_Id(dtoProduct.getId(), shopId);
        if (!opProduct.isPresent()) {
            response.put("mesager", "Không tìm thấy thông tin sản phẩm!!!!");
            response.put("success", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Product product = opProduct.get();

        Double price = dtoProduct.getPrice();
        Double reprice = dtoProduct.getSaleprice();
        product.setPrice(price);
        product.setSaleprice(reprice);
        product.setUnits(units);
        product.setProductTag(productTags);
        product.setTitle(dtoProduct.getTitle());
        product.setContent(dtoProduct.getContent());
        product.setImages(dtoProduct.getImages());
        product.setSku(dtoProduct.getSku());
        Product updateProduct = repoProduct.save(product);
        if (dtoProduct.getProductIdShopee() != null) {
            ProductShopee shopee = repoProductShopee.findByProductId(updateProduct.getId());
            ProductShopee productShopee = new ProductShopee();
            productShopee.setId(shopee.getId());
            productShopee.setItemId(dtoProduct.getProductIdShopee());
            productShopee.setShopId(dtoProduct.getShopinfoId());
            productShopee.setProduct(updateProduct);
            productShopee.setShopeeId(dtoProduct.getShopeeId());
            repoProductShopee.save(productShopee);
        }
        List<DtoProductVariations> dataUpdate = updateListVariations(dtoProduct.getVariations(), updateProduct, dtoProduct);
        Double totalQuantity = 0.0;
        for (DtoProductVariations item : dataUpdate) {
            Double quantity = formatStringToDouble(item.getQuantity());
            totalQuantity = totalQuantity + quantity;
        }
        dtoProduct.setVariations(dataUpdate);
        dtoProduct.setQuantity(String.valueOf(totalQuantity));
        response.put("data", dtoProduct);
        response.put("success", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // cập nhật biến thể nếu biến thể nòa chưa có thì thành thêm mới
    public List<DtoProductVariations> updateListVariations(List<DtoProductVariations> variationsList, Product product, DtoProduct dtoProduct) {
        List<DtoProductVariations> listData = new ArrayList<>();
        for (DtoProductVariations item : variationsList) {
            Optional<ProductVariations> opProVariations = repoProductVariations.findById(item.getId());
            if (!opProVariations.isPresent()) {
                createProductVariations(item, product);
            } else {
                ProductVariations proVariations = opProVariations.get();
                List<Properties> newProperties = createProperties(item.getProperties());
                Double quantity = formatStringToDouble(item.getQuantity());
                proVariations.setQuantity(quantity);
                Double price = item.getPrice();
                proVariations.setPrice(price);
                proVariations.setSku(item.getSku());
                proVariations.setImage(item.getImage());
                proVariations.setWeight(item.getWeight());
                proVariations.setBarcode(item.getBarcode());
                Double Overprice = item.getSaleprice();
                proVariations.setSaleprice(Overprice);
                ProductVariations Variation = repoProductVariations.save(proVariations);
                DtoProductVariations upDtoProduct = mapperProductVariations.toDto(Variation);
                listData.add(upDtoProduct);
            }
        }
        return listData;
    }

    // tao mới hoặc  thuộc tính produc properties
    public List<Properties> createProperties(List<Properties> listProperties) {
        List<Properties> newProperties = new ArrayList<>();
        if (listProperties.size() > 0 && listProperties != null) {
            for (Properties properties : listProperties) {
                List<Properties> properties1 = repoProperties.findAllByKeynameAndValue(properties.getKeyname(), properties.getValue());
                if (properties1.size() > 0) {
                    newProperties.add(properties1.get(0));
                } else {
                    properties.setActive(true);
                    Properties newItem = repoProperties.save(properties);
                    newProperties.add(newItem);
                }
            }
        }
        return newProperties;
    }

    // tạo các sản phẩm biến thể to
    public DtoProductVariations createProductVariations(DtoProductVariations variations, Product product) {
        ProductVariations productVariation = new ProductVariations();
        try {
            productVariation = toEntity(variations);
        }catch (Exception e){
            e.printStackTrace();
        }
        productVariation.setIsActive(true);

        double quantityVariable = 0;
        double weight = 0;
        try {
            quantityVariable = Double.parseDouble(String.valueOf(productVariation.getQuantity()));
            weight = (double) productVariation.getWeight();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        productVariation.setWeight(weight);
        productVariation.setPrice(variations.getPrice());
        productVariation.setBarcode(variations.getBarcode());
        productVariation.setSku(variations.getSku());
        productVariation.setQuantity(quantityVariable);
        productVariation.setProducts(product);
        productVariation.setSaleprice(variations.getSaleprice());
        List<Properties> newProperties = createProperties(variations.getProperties());
        ProductVariations Variation = repoProductVariations.save(productVariation);
        variations.setId(Variation.getId());
        variations.setProperties(newProperties);
        return variations;
    }

    public ProductVariations toEntity(DtoProductVariations source) {
        if ( source == null ) {
            return null;
        }
        ProductVariations productVariations = new ProductVariations();
        productVariations.setId( source.getId() );
        productVariations.setImage( source.getImage() );
        if ( source.getQuantity() != null ) {
            productVariations.setQuantity( Double.parseDouble( source.getQuantity() ) );
        }
        productVariations.setWeight( source.getWeight() );
        productVariations.setIsActive( source.getIsActive() );
        productVariations.setSku( source.getSku() );
        productVariations.setBarcode( source.getBarcode() );
        productVariations.setPrice( source.getPrice() );
        productVariations.setSaleprice( source.getSaleprice() );
        List<Properties> list = source.getProperties();
        if ( list != null ) {
            productVariations.setProperties( new ArrayList<Properties>( list ) );
        }

        return productVariations;
    }

    public Optional<Product> findByIdAndShopInfo_Id(Long id, Long shopId) {
        return repoProduct.findByIdAndShopInfo_Id(id, shopId);
    }

    public List<DtoProduct> getDtoProductPage(Pageable pageable) {
        List<Product> products = repoProduct.findAllBy(pageable);
        List<DtoProduct> dtoProducts = new ArrayList<>();
        for (Product product : products) {
            DtoProduct dtoProduct = mapperProduct.toDto(product);
            dtoProduct.setUnit(product.getUnits().getName());
            dtoProduct.setShopinfoId(product.getShopInfo().getId());
            dtoProduct.setVariations(new ArrayList<>(serProductVariations.getAllProductVariantionByProductId(product)));
            dtoProducts.add(dtoProduct);
        }
        return dtoProducts;
    }

    public Object FindBySKU(Long id, String query) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (query.trim().isEmpty() || query.trim().equals("null")) {
                response.put("data", 0);
                response.put("message", "mời nhập mã sku");
            } else {
                Optional<Product> optionalProduct = repoProduct.findBySkuAndShopInfo_Id(query, id);
                Product product = optionalProduct.get();
                if (product.equals(null)) {
                    Optional<ProductVariations> optional = repoProductVariations.findBySkuContaining(query);
                    ProductVariations productVariations = optional.get();
                    if (productVariations.equals(null)) {
                        response.put("data", 0);
                        response.put("message", "sku");
                    } else {
                        response.put("data", 1);
                        response.put("message", "đã tồn tại ma sku");
                    }
                } else {
                    response.put("data", 1);
                    response.put("message", "đã tồn tại ma sku");
                }
                response.put("success", true);

            }
            return response;
        } catch (Exception e) {
            response.put("data", 0);
            response.put("message", e.getMessage());
            return response;
        }
    }

    // check xem thẻ tag của sản phẩm có tồn tại hay không
    private ProductTag checkProductTag(String tag) {
        List<ProductTag> tags = repoProductTag.findByTitle(tag);
        if (tags.size() > 0) {
            return tags.get(0);
        }
        return null;
    }

    // check xem don vi tinh co ton tai hay khong
    private Units checkProductUnit(String unit) {
        List<Units> productUnitOpt = repoUnit.findByName(unit);
        if (productUnitOpt.size() > 0) {
            return productUnitOpt.get(0);
        }
        return null;
    }

    // check sku khi them san pham moi
    private boolean isSkuExist(String sku) {
        List<Product> ListSku = repoProduct.findBySku(sku);

        if (ListSku != null && ListSku.size() > 0) {
            return true;
        }
        return false;
    }

    // form mat string to double các trường quatity, price,saleprice,
    public Double formatStringToDouble(String data) {
        Double price = 0.0;
        String originPrice = data;
        if (originPrice != null && originPrice.length() > 0) {
            price = Double.parseDouble(originPrice);
        }
        return price;
    }

    // thêm sản phẩm biến thể sản phẩm, ...
    public Object InsSent(DtoProduct dtoProduct, UserPrincipal currentUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product product = new Product();
            // check sku
            if (isSkuExist(dtoProduct.getSku())) {
                response.put("message", "Mã SKU đã tồn tại!!!");
                response.put("success", false);
                return response;
            }

            // check thong tin shop qua id shop filter
            Optional<ShopUserRole> opShopInfo = repoShopUserRole.findByShopInfoIdAndUserId(dtoProduct.getShopinfoId(), currentUser.getId());
            if (!opShopInfo.isPresent()) {
                response.put("message", "Không tìm thấy thông tin shop !!");
                response.put("success", false);
                return response;
            }
            // tìm tên đơn vị tính nêu có thì trả ra
            String unit = dtoProduct.getUnit();
            Units units = checkUnit(unit);
            // duyệt và check thẻ tag
            List<String> _tags = dtoProduct.getTags();
            List<ProductTag> productTags = new ArrayList<>();
            if (_tags.size() > 0) {
                checkListTags(_tags, productTags);
            }
            Double price = dtoProduct.getPrice();
            Double reprice = dtoProduct.getSaleprice();
            product.setPrice(price);
            product.setSaleprice(reprice);
            product.setProductTag(productTags);
            product.setUnits(units);
            product.setShopInfo(opShopInfo.get().getShopInfo());
            product.setSku(dtoProduct.getSku());
            product.setImages(dtoProduct.getImages());
            product.setContent(dtoProduct.getContent());
            product.setTitle(dtoProduct.getTitle());
            product.setIsActive(true);
            product.setChannel(dtoProduct.getChannel());
            Product coproduct = repoProduct.save(product);
            if (dtoProduct.getProductIdShopee() != null) {
                ProductShopee productShopee = new ProductShopee();
                productShopee.setItemId(dtoProduct.getProductIdShopee());
                productShopee.setShopId(dtoProduct.getShopinfoId());
                productShopee.setProduct(coproduct);
                productShopee.setShopeeId(dtoProduct.getShopeeId());
                repoProductShopee.save(productShopee);
                // call API PHP
            }

            List<DtoProductVariations> variationsList = dtoProduct.getVariations();
            List<DtoProductVariations> dataVariations = createListVariation(variationsList, coproduct, dtoProduct);
            Double totalQuantity = 0.0;
            if (dataVariations.size() > 1) {
                for (DtoProductVariations item : dataVariations) {
                    Double quantity = formatStringToDouble(item.getQuantity());
                    totalQuantity = totalQuantity + quantity;
                }
            } else {
                totalQuantity = formatStringToDouble(dataVariations.get(0).getQuantity());
            }
            dtoProduct.setVariations(dataVariations);
            dtoProduct.setId(coproduct.getId());
            dtoProduct.setQuantity(String.valueOf(totalQuantity));
            response.put("data", dtoProduct);
            response.put("product", coproduct);
            response.put("success", true);
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            response.put("data", ex);
            response.put("success", false);
            return response;
        }
    }

    // tạo sản phẩm phụ, biến thể của sản
    public List<DtoProductVariations> createListVariation(List<DtoProductVariations> data, Product product, DtoProduct dtoProduct) {
        List<DtoProductVariations> variationsList = new ArrayList<>();
        ProductVariations productVariation = new ProductVariations();
        if (data != null && data.size() > 0) {
            for (DtoProductVariations variations : data) {
                DtoProductVariations Variation = createProductVariations(variations, product);
                variationsList.add(Variation);
            }
        } else {
            if (dtoProduct.getShopeeId() != 0L) {
                productVariation.setBarcode(String.valueOf(dtoProduct.getProductIdShopee()));
            } else {
                productVariation.setBarcode("");
            }
            Double price = dtoProduct.getPrice();
            Double reprice = dtoProduct.getSaleprice();
            productVariation.setPrice(price);
            productVariation.setSaleprice(reprice);
            productVariation.setIsActive(true);
            productVariation.setSku(dtoProduct.getSku());
            productVariation.setWeight(0.0);
            productVariation.setQuantity(0.0);
            if (dtoProduct.getImages() != null) {
                productVariation.setImage(dtoProduct.getImages().get(0));
            }
            productVariation.setProducts(product);
            ProductVariations Variation = repoProductVariations.save(productVariation);
            productVariation.setId(Variation.getId());
            DtoProductVariations newVariation = mapperProductVariations.toDto(productVariation);
            variationsList.add(newVariation);
        }
        return variationsList;
    }

    // @author hung pham
    // get product, product variations, unit buy sku
    public Product getProductBySku(String sku, Long id) {
        Optional<Product> opProduct = repoProduct.findBySkuAndShopInfo_Id(sku, id);
        Product productFind = opProduct.get();
        return productFind;
    }

    // @author the cong
    public static DtoProduct producVariants2Json(ShopInfo shopInfo, Product product,
                                                 List<ProductVariations> variations) {

        long id = product.getId();
        Boolean isActive = product.getIsActive();
        String sku = product.getSku();
        String name = product.getTitle();
        String channel = product.getChannel();
        long totalVari = variations.size();

//        List<String> categories = product.getCategories().stream().map(e -> e.getName()).collect(Collectors.toList());

        String unit = product.getUnits().getName();

        String quantity = variations.stream().map(v -> v.getQuantity()).reduce(0.0, (a, b) -> a + b).toString();

        List<String> tags = product.getProductTag().stream().map(t -> t.getTitle()).collect(Collectors.toList());

        List<DtoProductVariations> variants = new ArrayList<DtoProductVariations>();

        for (ProductVariations variation : variations) {
            long _id = variation.getId();

            List<Properties> properties = variation.getProperties().stream()
                    .collect(Collectors.mapping(p -> new Properties(p.getKeyname(), p.getValue(), p.getCreatedDate(), p.getId(), p.getModifiedDate()), Collectors.toList()));
            String _quantity = String.valueOf(variation.getQuantity());
            Double price = variation.getPrice();
            Double saleprice = variation.getSaleprice();
            String barcode = String.valueOf((variation.getBarcode()));
            Double weight = (double) variation.getWeight();
            String _sku = variation.getSku();
            DtoProductVariations variant = new DtoProductVariations(_id, _sku, properties, _quantity, weight, price, barcode, saleprice, variation.getImage(), variation.getIsActive());
            variants.add(variant);
        }
        String content = product.getContent();
        long shopid = shopInfo.getId();
        Double originPrice = product.getPrice();
        Double salePrice = product.getSaleprice();
        DtoProduct doNewProduct = new DtoProduct(isActive, id, sku, name, originPrice, unit, tags, salePrice, variants, content, shopid, product.getImages(), totalVari, quantity, channel);
        return doNewProduct;
    }

    public Optional<Product> FindById(Long id) {
        return repoProduct.findById(id);
    }

}


