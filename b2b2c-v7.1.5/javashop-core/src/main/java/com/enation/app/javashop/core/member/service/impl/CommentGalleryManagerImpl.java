package com.enation.app.javashop.core.member.service.impl;

import com.enation.app.javashop.core.member.model.dos.CommentGallery;
import com.enation.app.javashop.core.member.service.CommentGalleryManager;
import com.enation.app.javashop.framework.database.DaoSupport;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论图片业务类
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-05-03 14:11:46
 */
@Service
public class CommentGalleryManagerImpl implements CommentGalleryManager {

    @Autowired
    @Qualifier("memberDaoSupport")
    private DaoSupport daoSupport;

    @Override
    @Transactional(value = "memberTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void add(Integer commentId, List<String> list) {

        if (StringUtil.isNotEmpty(list)) {
            int i = 0;
            for (String image : list) {
                CommentGallery commentGallery = new CommentGallery();
                commentGallery.setCommentId(commentId);
                commentGallery.setOriginal(image);
                commentGallery.setSort(i);
                this.daoSupport.insert(commentGallery);
                i++;
            }
        }
    }

    @Override
    public Map<Integer, List<String>> getGalleryByCommentIds(Integer commentIds) {


        String sql = "select * from es_comment_gallery where comment_id = ?  ";
        List<CommentGallery> resList = this.daoSupport.queryForList(sql,CommentGallery.class, commentIds);

        Map<Integer, List<String>> resMap = new HashMap<>(resList.size());

        for(CommentGallery image : resList){
            Integer commentId = image.getCommentId();
            List<String> imageList = resMap.get(commentId);
            if(imageList == null){
                imageList = new ArrayList<>();
            }
            imageList.add(image.getOriginal());
            resMap.put(commentId,imageList);
        }
        return resMap;
    }
}
