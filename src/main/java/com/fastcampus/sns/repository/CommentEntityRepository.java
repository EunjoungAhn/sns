package com.fastcampus.sns.repository;

import com.fastcampus.sns.model.entity.CommentEntity;
import com.fastcampus.sns.model.entity.LikeEntity;
import com.fastcampus.sns.model.entity.PostEntity;
import com.fastcampus.sns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer> {
    Page<CommentEntity> findAllByPost(PostEntity post, Pageable pageable);

    // JPA는 영속성을 관리한다.(데이블의 데이터를 가져왔을때 어플리케이션이 데이터의 라이프 사이클을 관리한다.)
    // 우리는 '삭제'를 했을때 바로바로 삭제되는 것을 원하지만, JPA에서는 영속성 때문에 데이터를 가져와서
    // 그중 삭제할 데이터를 삭제를 하는 비효율적인 방법을 취한다.

    @Transactional
    @Modifying
    @Query("UPDATE CommentEntity entity SET removed_at = NOW() where entity.post = :post")
    void deleteAllByPost(@Param("post") PostEntity postEntity);
}
