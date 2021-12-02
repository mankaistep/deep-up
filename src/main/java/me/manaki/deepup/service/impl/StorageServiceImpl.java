package me.manaki.deepup.service.impl;

import lombok.RequiredArgsConstructor;
import me.manaki.deepup.service.StorageService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final Environment environment;

    @Override
    public String saveAvatar(String username, MultipartFile mfile, HttpServletRequest request) {
        String newName = checkFile(username, mfile);
        var dest = environment.getProperty("storage.folder") + environment.getProperty("storage.avatars") + "/" + newName;

        try {
            var file = new File(dest);
            if (!file.exists()) file.createNewFile();

            mfile.transferTo(file);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }

        return newName;
    }

    @Override
    public String saveWallpaper(String username, MultipartFile mfile) {
        String newName = checkFile(username, mfile);
        var dest = environment.getProperty("storage.folder") + environment.getProperty("storage.wallpapers") + "/" + newName;

        try {
            var file = new File(dest);
            if (!file.exists()) file.createNewFile();

            mfile.transferTo(file);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }

        return newName;
    }

    @Override
    public String savePostImage(String postId, MultipartFile mfile) {
        String newName = checkFile(postId, mfile);
        var dest = environment.getProperty("storage.folder") + environment.getProperty("storage.post-images") + "/" + newName;

        try {
            var file = new File(dest);
            if (!file.exists()) file.createNewFile();

            mfile.transferTo(file);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }

        return newName;
    }

    private String checkFile(String prefix, MultipartFile mfile) {
        var fileName = mfile.getOriginalFilename();
        assert fileName != null;

        var suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!List.of("png", "jpg", "jpeg").contains(suffix)) throw new MultipartException("Must be a image");

        return prefix + "_" + System.currentTimeMillis() + "." + suffix;
    }

}
