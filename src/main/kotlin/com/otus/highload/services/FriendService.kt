package com.otus.highload.services

import com.otus.highload.domain.RegisteredUser
import com.otus.highload.repositories.FriendsRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class FriendService {
  val log: Logger = LoggerFactory.getLogger(FriendService::class.java)

  @Autowired
  private lateinit var friendsRepository: FriendsRepository

  fun add(friendId: Long, userId: Long) {
    friendsRepository.add(friendId, userId)
    revalidateUserCache(userId)
  }

  fun delete(friendId: Long, userId: Long) {
    friendsRepository.delete(friendId, userId)
    revalidateUserCache(userId)
  }

  @CacheEvict(cacheNames = ["feed", "#userId"])
  fun revalidateUserCache(userId: Long) {
    log.info("User $userId feed cache evicted")
  }
}