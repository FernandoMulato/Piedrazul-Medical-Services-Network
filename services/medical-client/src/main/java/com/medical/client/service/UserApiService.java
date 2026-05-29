package com.medical.client.service;

import com.medical.client.config.ApiConfig;
import com.medical.client.model.CreateUserRequest;
import com.medical.client.model.UpdateUserRequest;
import com.medical.client.model.UserResponse;
import com.medical.client.model.UserRole;

import java.util.List;

public class UserApiService {

    private final HttpClientService httpClient = HttpClientService.getInstance();

    public List<UserResponse> getAllUsers() throws Exception {
        return httpClient.getList(ApiConfig.USERS_BASE_URL, UserResponse.class);
    }

    public UserResponse getUserById(Long id) throws Exception {
        return httpClient.get(ApiConfig.USERS_BASE_URL + "/" + id, UserResponse.class);
    }

    public UserResponse createUser(CreateUserRequest request) throws Exception {
        return httpClient.post(ApiConfig.USERS_BASE_URL, request, UserResponse.class);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request) throws Exception {
        return httpClient.put(ApiConfig.USERS_BASE_URL + "/" + id, request, UserResponse.class);
    }

    public void deactivateUser(Long id) throws Exception {
        httpClient.patch(ApiConfig.USERS_BASE_URL + "/" + id + "/deactivate");
    }
}
