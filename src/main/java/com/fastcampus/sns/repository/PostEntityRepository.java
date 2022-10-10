package com.fastcampus.sns.repository;

import com.fastcampus.sns.model.entity.PostEntity;
import com.fastcampus.sns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity, Integer> {
    //검색을 할때 인덱스가 없으면 굉장히 느린 속도를 가지게 된다.
    Page<PostEntity> findAllByUser(UserEntity entity, Pageable pageable);
}
