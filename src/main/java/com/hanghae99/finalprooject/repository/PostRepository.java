package com.hanghae99.finalprooject.repository;

import com.hanghae99.finalprooject.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {


//    List<Post> findAllByUserOrderByCreatedAtDesc(User user);
}
