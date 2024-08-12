package com.blog.app.payloads;


import com.blog.app.entities.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class PostDto {

    private int postId;
    private String postTitle;
    private String postContent;
    private String imageName;
    private Date addedDate;
    private CategoryDto categoryDto;
    private UserDto userDto;
    private List<Comment> comments;

}
