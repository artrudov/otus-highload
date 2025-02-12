package com.otus.highload.services

import com.otus.highload.configurations.RabbitMQConfig
import com.otus.highload.domain.CreatedPost
import com.otus.highload.domain.Post
import com.otus.highload.exceptions.AccessEditPostDenied
import com.otus.highload.repositories.FriendsRepository
import com.otus.highload.repositories.PostsRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service


@Service
class PostsService {
  val log: Logger = LoggerFactory.getLogger(PostsService::class.java)

  @Autowired
  lateinit var rabbitTemplate: RabbitTemplate

  @Autowired
  lateinit var postsRepository: PostsRepository

  @Autowired
  lateinit var friendsRepository: FriendsRepository

  fun get(postId: Long): CreatedPost {
    return postsRepository.get(postId)
  }

  fun add(post: Post, userId: Long): CreatedPost {
    val createdPost = postsRepository.save(post, userId)
    revalidateFriendsCache(userId)
    sendNewPost(createdPost)

    return createdPost
  }

  fun delete(postId: Long, userId: Long) {
    postsRepository.delete(postId, userId)
    revalidateFriendsCache(userId)
  }

  fun update(post: CreatedPost, userId: Long): CreatedPost {
    if (post.userId == userId) {
      val updatePost = postsRepository.update(post)
      revalidateFriendsCache(userId)

      return updatePost
      } else {
        throw AccessEditPostDenied("User $userId can not update this post ${post.postId}")
    }
  }

  fun getFeed(pageable: Pageable, userId: Long): List<CreatedPost> {
    val posts = postsRepository.getFeed(userId)

    val startIndex = pageable.pageSize * pageable.pageNumber
    val endIndex = startIndex + pageable.pageSize

    return posts.subList(startIndex, endIndex)
  }

  fun revalidateFriendsCache (userId: Long) {
    val users = friendsRepository.getAllFriendsId(userId)

    for (user in users) {
      revalidateUserCache(user)
    }
  }

  fun sendNewPost(post: CreatedPost) {
    log.info("Sending post ${post.postId} to rabbit")

    rabbitTemplate.convertAndSend(
      RabbitMQConfig.EXCHANGE_POSTS,
      RabbitMQConfig.toPostAuthorRoutingKey(post.userId),
      post
    )
  }

  @CacheEvict(cacheNames = ["feed", "#userId"])
  fun revalidateUserCache(userId: Long) {
    log.info("User $userId feed cache evicted")
  }
}