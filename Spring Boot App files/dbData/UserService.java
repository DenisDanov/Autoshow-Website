package com.example.demo.dbData;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> userData() {
        return userRepository.findAll();
    }

    public void loadDataFromJson(UserRepository userRepository) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            Resource resource = new ClassPathResource("BOOT-INF/classes/website-databaseCSV.json");
            InputStream inputStream = resource.getInputStream();

            List<User> users = mapper.readValue(inputStream, new TypeReference<List<User>>() {});

            // Save users to the repository
            userRepository.saveAll(users);
            System.out.println("Data loaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
