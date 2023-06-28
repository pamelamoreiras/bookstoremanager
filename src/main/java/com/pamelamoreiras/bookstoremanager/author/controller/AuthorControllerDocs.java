package com.pamelamoreiras.bookstoremanager.author.controller;

import com.pamelamoreiras.bookstoremanager.author.dto.AuthorDTO;
import com.sun.xml.bind.annotation.XmlIsSet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Api("Authors menagement")
public interface AuthorControllerDocs {

    @ApiOperation(value = "Author creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success author creation"),
            @ApiResponse(code = 400, message = "Missing required field, wrong field range value or author already registered")
    })
    AuthorDTO create(AuthorDTO authorDTO);
}
