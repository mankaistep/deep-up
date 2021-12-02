package me.manaki.deepup.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface StorageService {

    String saveAvatar(String username, MultipartFile file, HttpServletRequest request);
    String saveWallpaper(String username, MultipartFile file);
    String savePostImage(String postId, MultipartFile file);

}
