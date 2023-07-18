package com.pamelamoreiras.bookstoremanager.books.controller;

import com.pamelamoreiras.bookstoremanager.books.dto.BookRequestDTO;
import com.pamelamoreiras.bookstoremanager.books.dto.BookResponseDTO;
import com.pamelamoreiras.bookstoremanager.users.dto.AuthenticatedUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Books module management")
public interface BookControllerDocs {

    @ApiOperation(value = "Book creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success book creation"),
            @ApiResponse(code = 201, message = "Missing required fields, wrong field range value ok book already registered on system")
    })
    BookResponseDTO create(AuthenticatedUser authenticatedUser, BookRequestDTO bookRequestDTO);
}
