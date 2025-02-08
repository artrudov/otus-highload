package com.otus.highload.controlers

import com.otus.highload.security.CustomUser
import com.otus.highload.services.FriendService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("friends")
class FriendsController {
  @Autowired
  lateinit var friendService: FriendService

  @PutMapping("{friendId}")
  fun add(@PathVariable friendId: Long, @AuthenticationPrincipal userDetails: CustomUser): ResponseEntity<Any> {
    friendService.add(friendId, userDetails.id)

    return ResponseEntity(HttpStatus.CREATED)
  }

  @DeleteMapping("{friendId}")
  fun delete(@PathVariable friendId: Long, @AuthenticationPrincipal userDetails: CustomUser): ResponseEntity<Any> {
    friendService.delete(friendId, userDetails.id)

    return ResponseEntity(HttpStatus.NO_CONTENT)
  }
}