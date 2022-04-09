package com.xq.tmall.service.impl;

import com.xq.tmall.dao.ProductMapper;
import com.xq.tmall.dao.ReviewMapper;
import com.xq.tmall.dao.UserMapper;
import com.xq.tmall.entity.Product;
import com.xq.tmall.entity.Review;
import com.xq.tmall.entity.User;
import com.xq.tmall.service.ReviewService;
import com.xq.tmall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductMapper productMapper;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean add(Review review) {
        return reviewMapper.insertOne(review) > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean update(Review review) {
        return reviewMapper.updateOne(review) > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean deleteList(Integer[] review_id_list) {
        return reviewMapper.deleteList(review_id_list) > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean deleteData(Integer id) {
        return reviewMapper.deleteData(id) > 0;
    }

    @Override
    public List<Review> getList(Review review, PageUtil pageUtil) {
        List<Review> reviewList = reviewMapper.select(review, pageUtil);
        List<User> userList = userMapper.select(new User(), null, null);
        List<Product> productList = productMapper.select(new Product(), null, null, null);
        Map<Integer, String> userMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(userList)) {
            userMap = userList.stream().collect(Collectors.toMap(User::getUser_id, User::getUser_nickname, (key1, key2) -> key2));
        }
        Map<Integer, String> productMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(productList)) {
            productMap = productList.stream().collect(Collectors.toMap(Product::getProduct_id, Product::getProduct_name, (key1, key2) -> key2));
        }
        if (!CollectionUtils.isEmpty(reviewList)) {
            for (Review re : reviewList) {
                User review_user = re.getReview_user();
                review_user.setUser_name(userMap.get(review_user.getUser_id()));
                Product review_product = re.getReview_product();
                review_product.setProduct_name(productMap.get(review_product.getProduct_id()));
            }
        }
        return reviewList;
    }

    @Override
    public List<Review> getListByUserId(Integer user_id, PageUtil pageUtil) {
        return reviewMapper.selectByUserId(user_id, pageUtil);
    }

    @Override
    public List<Review> getListByProductId(Integer product_id, PageUtil pageUtil) {
        return reviewMapper.selectByProductId(product_id, pageUtil);
    }

    @Override
    public Review get(Integer review_id) {
        return reviewMapper.selectOne(review_id);
    }

    @Override
    public Integer getTotal(Review review) {
        return reviewMapper.selectTotal(review);
    }

    @Override
    public Integer getTotalByUserId(Integer user_id) {
        return reviewMapper.selectTotalByUserId(user_id);
    }

    @Override
    public Integer getTotalByProductId(Integer product_id) {
        return reviewMapper.selectTotalByProductId(product_id);
    }

    @Override
    public Integer getTotalByOrderItemId(Integer productOrderItem_id) {
        return reviewMapper.selectTotalByOrderItemId(productOrderItem_id);
    }
}
