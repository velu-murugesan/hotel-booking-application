package com.eastcoast.MannarHotel.service.interfaces;

import com.eastcoast.MannarHotel.dto.LoginRequest;
import com.eastcoast.MannarHotel.dto.Response;
import com.eastcoast.MannarHotel.dto.UserRequest;
import com.eastcoast.MannarHotel.entity.Users;

public interface UserServiceInterface {

    Response register(UserRequest user);
    Response login(LoginRequest loginRequest);
    Response getAllUsers();
    Response getUserBookingHistory(Long userId);
    Response deleteUser(Long userId);
    Response getUserById(Long userId);
    Response getMyInfo(String email);

}
