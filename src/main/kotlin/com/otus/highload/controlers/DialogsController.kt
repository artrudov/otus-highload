package com.otus.highload.controlers

import com.otus.highload.domain.Message
import com.otus.highload.domain.NewMessage
import com.otus.highload.security.CustomUser
import com.otus.highload.services.DialogsService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/dialogs")
class DialogsController {
  @Autowired
  lateinit var dialogsService: DialogsService

  @PostMapping("{recipientID}")
  fun save(
    @RequestBody message: NewMessage,
    @PathVariable recipientID: Long,
    @AuthenticationPrincipal userDetails: CustomUser,
    request: HttpServletRequest
  ): ResponseEntity<String> {
    println(">>> Request header: ${ request.getAttribute("X-Request-ID")}")
    val result = dialogsService.save(
      Message(
        userTo = recipientID,
        userFrom = userDetails.id,
        author = userDetails.id,
        text = message.text
      ),
      request.getAttribute("X-Request-ID").toString()
    )

    return ResponseEntity(result, HttpStatus.CREATED)
  }

  @GetMapping("{recipientID}")
  fun finAllMessages(
    @PathVariable recipientID: Long,
    @AuthenticationPrincipal userDetails: CustomUser,
    @PageableDefault(size = 10, page = 1) pageable: Pageable,
    request: HttpServletRequest
  ): ResponseEntity<List<Message>> {
    return ResponseEntity.ok(
      dialogsService.findAllMessages(
        recipientID,
        userDetails.id,
        pageable,
        request.getAttribute("X-Request-ID").toString()
    ))
  }
}