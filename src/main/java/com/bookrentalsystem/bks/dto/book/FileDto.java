package com.bookrentalsystem.bks.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.nio.channels.MulticastChannel;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {

    private MultipartFile multipartFile;
}
