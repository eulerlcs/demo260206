package com.example.demo.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import com.example.demo.service.AddService;


@ApiResponses(
		   {
		    @ApiResponse(
		       responseCode = "500",
		       description = "Internal server error"),

		    @ApiResponse(
		       responseCode = "400",
		       description = "Bad request")
		   }
		)
@Validated
@RestController
public class HelloController {

	private final AddService addService;

	public HelloController(AddService addService) {
		this.addService = addService;
	}

	@GetMapping("/hello")
	public String hello(String name) {
		return "hello " + name;
	}

	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Successful operation",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),
		@ApiResponse(responseCode = "400", description = "Bad Request - validation failed",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
		@ApiResponse(responseCode = "500", description = "Internal Server Error",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
	})
	@Operation(summary = "Add two integers", description = "Returns the sum of two integers a and b")
	@GetMapping("/add")
	public int add(
			@Parameter(description = "First integer to add", required = true)
			@RequestParam @NotNull @Min(0) @Max(1000) Integer a,
			@Parameter(description = "Second integer to add", required = true)
			@RequestParam @NotNull @Min(0) @Max(1000) Integer b) {
		return addService.add(a, b);
	}
}