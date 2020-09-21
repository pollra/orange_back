package com.pollra.web.post.mapper;

import com.pollra.web.post.entity.Post;
import com.pollra.web.post.form.PostForm.*;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy=ReportingPolicy.IGNORE)
public interface PostMapper {
    PostMapper mapper = Mappers.getMapper(PostMapper.class);

    Post toPost(Long id);
    Post toPost(Request.Add form);
    Post toPost(Long id, Request.Modify form);

    Response.FindAll toFindAll(Post entity);
    Response.FindOne toFindOne(Post entity);
}
