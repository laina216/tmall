package com.xq.tmall.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xq.tmall.controller.BaseController;
import com.xq.tmall.entity.Review;
import com.xq.tmall.service.ReviewService;
import com.xq.tmall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;


/**
 * 后台管理-评论页
 */
@Controller
public class ReviewController extends BaseController {
    @Autowired
    private ReviewService reviewService;


    //转到后台管理-评论页-ajax
    @RequestMapping(value = "admin/review", method = RequestMethod.GET)
    public String goToPage(HttpSession session, Map<String, Object> map) {
        logger.info("检查管理员权限");
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "admin/include/loginMessage";
        }

        logger.info("获取前10条评论列表");
        PageUtil pageUtil = new PageUtil(0, 10);
        List<Review> reviewList = reviewService.getList(null, pageUtil);
        map.put("reviewList", reviewList);
        logger.info("获取评论总数量");
        Integer reviewCount = reviewService.getTotal(null);
        map.put("reviewCount", reviewCount);
        logger.info("获取分页信息");
        pageUtil.setTotal(reviewCount);
        map.put("pageUtil", pageUtil);

        logger.info("转到后台管理-评论页-ajax方式");
        return "admin/reviewManagePage";
    }

    //转到后台管理-评论详情页-ajax
    @RequestMapping(value = "admin/review/{cid}", method = RequestMethod.GET)
    public String goToDetailsPage(HttpSession session, Map<String, Object> map, @PathVariable Integer cid/* 评论ID */) {
        logger.info("检查管理员权限");
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "admin/include/loginMessage";
        }
        logger.info("获取review_id为{}的评论信息", cid);
        Review review = reviewService.get(cid);
        map.put("review", review);

        logger.info("转到后台管理-评论详情页-ajax方式");
        return "admin/include/reviewDetails";
    }


    //按条件查询评论-ajax
    @ResponseBody
    @RequestMapping(value = "admin/review/{index}/{count}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String getreviewBySearch(@RequestParam(required = false) String review_content/* 评论名称 */,
                                    @RequestParam(required = false) String review_createDate/* 评论时间 */,
                                    @PathVariable Integer index/* 页数 */,
                                    @PathVariable Integer count/* 行数 */) throws UnsupportedEncodingException {
        //移除不必要条件
        if (review_content != null) {
            //如果为非空字符串则解决中文乱码：URLDecoder.decode(String,"UTF-8");
            review_content = "".equals(review_content) ? null : URLDecoder.decode(review_content, "UTF-8");
        }
        JSONObject object = new JSONObject();
        Review review = new Review();
        review.setReview_content(review_content);
        review.setReview_createDate(review_createDate);
        logger.info("按条件获取第{}页的{}条评论", index + 1, count);
        PageUtil pageUtil = new PageUtil(index, count);
        List<Review> reviewList = reviewService.getList(review, pageUtil);
        object.put("reviewList", JSONArray.parseArray(JSON.toJSONString(reviewList)));
        logger.info("按条件获取评论总数量");
        Integer reviewCount = reviewService.getTotal(review);
        object.put("reviewCount", reviewCount);
        logger.info("获取分页信息");
        pageUtil.setTotal(reviewCount);
        object.put("totalPage", pageUtil.getTotalPage());
        object.put("pageUtil", pageUtil);

        return object.toJSONString();
    }

    //按ID删除评论并返回最新结果-ajax
    @ResponseBody
    @RequestMapping(value = "admin/review/del/{id}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String deleteProductById(@PathVariable Integer id) {
        JSONObject object = new JSONObject();
        boolean yn = reviewService.deleteData(id);
        if (yn) {
            logger.info("删除成功！");
            object.put("success", true);
        } else {
            logger.warn("删除失败！事务回滚");
            object.put("success", false);
            throw new RuntimeException();
        }
        return object.toJSONString();
    }
}