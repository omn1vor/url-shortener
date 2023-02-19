package com.example.urlshortener.dto;

import com.example.urlshortener.model.UrlStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
public class CreateUrlRequest {
    @NotNull
    String url;
    String code;
    @NotNull  @Email(message = "{errors.invalidEmail}")
    String email;
    LocalDateTime created;
    UrlStatus status = UrlStatus.ACTIVE;

    public CreateUrlRequest(CreateUrlRequest request) {
        this.url = request.getUrl();
        this.code = request.getCode();
        this.email = request.getEmail();
        this.created = request.getCreated();
        this.status = request.getStatus();
    }
}
