package com.otus.highload.services

import com.otus.highload.domain.RegisteredUser
import com.otus.highload.repositories.FriendsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class FriendService {
  @Autowired
  private lateinit var friendsRepository: FriendsRepository

  fun add(friendId: Long, userId: Long) {
    friendsRepository.add(friendId, userId)
  }

  fun delete(friendId: Long, userId: Long) {
    friendsRepository.delete(friendId, userId)
  }
}