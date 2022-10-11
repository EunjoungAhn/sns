package com.fastcampus.sns.repository;

import com.fastcampus.sns.model.entity.LikeEntity;
import com.fastcampus.sns.model.entity.PostEntity;
import com.fastcampus.sns.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeEntityRepository extends JpaRepository<LikeEntity, Integer> {
    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);
  /*
    List<LikeEntity> findAllByPost(PostEntity post); // 이 코드는 모든 row를 가지고 오게 된다.
    그래서 퀴리로 row 갯수만 검색해서 가져오도록 바꾸려고한다.
  */

    // select count(*) from "like" where post_id = 2 아래의 쿼리문을 해석했을때
    // 아래의 작업이 쿼리가 훨신 가볍게 동작한다.
//    @Query(value = "SELECT COUNT(*) FROM LikeEntity entity WHERE entity.post =:post")
//    Integer countByPost(@Param("post") PostEntity post);

    long countByPost(PostEntity post);

    @Transactional
    @Modifying
    @Query("UPDATE LikeEntity entity SET removed_at = NOW() where entity.post = :post")
    void deleteAllByPost(@Param("post") PostEntity postEntity);

}
