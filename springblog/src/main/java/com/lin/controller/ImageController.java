package com.lin.controller;

import com.lin.entity.UserProfile;
import com.lin.repository.UserProfileRepository;
import com.lin.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    UploadService uploadService;

    @Value("${img.imgURL}")
    private String imgURL;

    //Upload picture
    //Parameters include user mailbox and file
    @PostMapping(value="/update")
    public String update(@RequestParam("file") MultipartFile file,@RequestParam("email") String email) {
        UserProfile userProfile = userProfileRepository.findUserProfileByEmail(email);
        if(file==null) return "empty file";
        //If the user already exists, update
        if(userProfile!=null){
            String indexPath=Long.toString(userProfile.getProfileId());
            //Process file name
            int lastIndexOf = file.getOriginalFilename().lastIndexOf(".");
            //Get the file extension
            String suffix = file.getOriginalFilename().substring(lastIndexOf);
            String fileName=indexPath + suffix;
            //Upload
            uploadService.upload(file,fileName);
            //Save database mapping
            userProfile.setPath(imgURL+fileName);
            //JPA
            userProfileRepository.deleteUserProfileByEmail(email);
            userProfileRepository.save(userProfile);
            return "sucess";
        }
        else
            return "Email error";
    }
}
