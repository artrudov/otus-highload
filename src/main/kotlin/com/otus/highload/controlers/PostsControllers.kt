package com.otus.highload.controlers

import com.otus.highload.domain.CreatedPost
import com.otus.highload.domain.Post
import com.otus.highload.security.CustomUser
import com.otus.highload.services.PostsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/posts")
class PostsControllers {
  @Autowired
  lateinit var postsService: PostsService

  @GetMapping("{postId}")
  fun get(@PathVariable postId: Long): ResponseEntity<CreatedPost> {
    return ResponseEntity.ok(
      postsService.get(postId)
    )
  }

  @PostMapping
  fun add(@RequestBody post: Post, @AuthenticationPrincipal userDetails: CustomUser): ResponseEntity<CreatedPost> {
    return ResponseEntity(
      postsService.add(post, userDetails.id),
      HttpStatus.CREATED
    )
  }

  @DeleteMapping("{postId}")
  fun delete(@PathVariable postId: Long, @AuthenticationPrincipal userDetails: CustomUser): ResponseEntity<String> {
    postsService.delete(postId, userDetails.id)
    return ResponseEntity(HttpStatus.NO_CONTENT)
  }

  @PutMapping
  fun update(@RequestBody post: CreatedPost, @AuthenticationPrincipal userDetails: CustomUser): ResponseEntity<CreatedPost> {
    return ResponseEntity.ok(
      postsService.update(post, userDetails.id)
    )
  }
}