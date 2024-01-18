package com.douunderstandapi;

import com.douunderstandapi.post.dto.request.PostAddRequest;
import com.douunderstandapi.post.repository.PostRepository;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.dto.request.UserAddRequest;
import com.douunderstandapi.user.repository.UserRepository;
import com.douunderstandapi.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DoUUnderstandApiApplication implements CommandLineRunner {

    @Autowired
    UserService userService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        UserAddRequest userAddRequest = new UserAddRequest("soonable@gmail.com", "1", "1", true);
        userService.addUser(userAddRequest);

        for (int i = 1; i < 50; i++) {
            PostAddRequest request = new PostAddRequest("title" + i,
                    "content" + i,
                    "link" + i);
            User user = userRepository.findByEmail("soonable@gmail.com")
                    .orElseThrow();
            postRepository.save(request.toEntity(user));
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(DoUUnderstandApiApplication.class, args);
    }
}
