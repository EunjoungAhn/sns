package com.fastcampus.sns.service;

import com.fastcampus.sns.exception.ErrorCode;
import com.fastcampus.sns.exception.SnsApplicationException;
import com.fastcampus.sns.model.AlarmArgs;
import com.fastcampus.sns.model.AlarmType;
import com.fastcampus.sns.model.Comment;
import com.fastcampus.sns.model.Post;
import com.fastcampus.sns.model.entity.*;
import com.fastcampus.sns.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final CommentEntityRepository commentEntityRepository;

    private final AlarmEntityRepository alarmEntityRepository;

    @Transactional
    public void create(String title, String body, String userName){
        // user find
        UserEntity userEntity = getUserOrException(userName);
        // post save
        postEntityRepository.save(PostEntity.of(title, body, userEntity));
    }

    @Transactional
    public Post modify(String title, String body, String userName, Integer postId){
        UserEntity userEntity = getUserOrException(userName);
        //post 존재여부
        PostEntity postEntity = getPostEntityOrException(postId);

        //수정하려는 사람이 post 작성자인지 확인
        if(postEntity.getUser() != userEntity){
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName, postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));

    }

    public void delete(String userName, Integer postId){
        UserEntity userEntity = getUserOrException(userName);
        //post 존재여부 확인
        PostEntity postEntity = getPostEntityOrException(postId);
        //post 작성자인지 확인
        if(postEntity.getUser() != userEntity){
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName, postId));
        }

        postEntityRepository.delete(postEntity);
    }

    public Page<Post> list(Pageable pageable){
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String userName, Pageable pageable){
        UserEntity userEntity = getUserOrException(userName);
        return postEntityRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
    }

    @Transactional
    public void like(Integer postId, String userName){
        //post 존재여부 확인
        PostEntity postEntity = getPostEntityOrException(postId);
        UserEntity userEntity = getUserOrException(userName);
        //한 사람이 한번만 like 하도록 체크
        likeEntityRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it -> {
            new SnsApplicationException(ErrorCode.ALREADY_LIKED, String.format("userName %s already like post %d", userName, postId));
        });

        //like save
        likeEntityRepository.save(LikeEntity.of(userEntity, postEntity));

        //alarm save
        alarmEntityRepository.save(AlarmEntity.of(postEntity.getUser(), AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(userEntity.getId(), postEntity.getId())));
    }

    @Transactional
    public int likeCount(Integer postId){
        //post 존재여부 확인
        PostEntity postEntity = getPostEntityOrException(postId);
        //like 갯수 가져오기
        //List<LikeEntity> likeEntities = likeEntityRepository.findAllByPost(postEntity);
        //return likeEntities.size();
        return likeEntityRepository.countByPost(postEntity);
    }

    @Transactional
    public void comment(Integer postId, String userName, String comment){
        PostEntity postEntity = getPostEntityOrException(postId);
        UserEntity userEntity = getUserOrException(userName);

        // comment save
        commentEntityRepository.save(CommentEntity.of(comment, postEntity, userEntity));

        //alarm save
        alarmEntityRepository.save(AlarmEntity.of(postEntity.getUser(), AlarmType.NEW_COMMENT_ON_POST, new AlarmArgs(userEntity.getId(), postEntity.getId())));
    }

    public Page<Comment> getComments(Integer postId, Pageable pageable){
        PostEntity postEntity = getPostEntityOrException(postId);
        return commentEntityRepository.findAllByPost(postEntity, pageable).map(Comment::fromEntity);
    }

    //반복되는 코드는 내부에서만 처리하는 메소드로 만들어서 코드 깔끔하게 정리
    //post 존재여부 확인
    private PostEntity getPostEntityOrException(Integer postId){
        return postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));
    }

    private UserEntity getUserOrException(String userName){
        return userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
    }
}
