package com.otus.highload.services

import com.otus.highload.domain.CreatedPost
import com.otus.highload.domain.Post
import com.otus.highload.exceptions.AccessEditPostDenied
import com.otus.highload.repositories.PostsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PostsService {
  @Autowired
  lateinit var postsRepository: PostsRepository

  fun get(postId: Long): CreatedPost {
    return postsRepository.get(postId)
  }

  fun add(post: Post, userId: Long): CreatedPost {
    return postsRepository.save(post, userId)
  }

  fun delete(postId: Long, userId: Long) {
    postsRepository.delete(postId, userId)
  }

  fun update(post: CreatedPost, userId: Long): CreatedPost {
    if (post.userId == userId)
      return postsRepository.update(post)
    else throw AccessEditPostDenied("User $userId can not update this post ${post.postId}")
  }
}