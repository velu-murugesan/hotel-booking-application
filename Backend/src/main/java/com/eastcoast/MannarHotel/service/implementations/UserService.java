package com.eastcoast.MannarHotel.service.implementations;
import com.eastcoast.MannarHotel.dto.LoginRequest;
import com.eastcoast.MannarHotel.dto.Response;
import com.eastcoast.MannarHotel.dto.UserRequest;
import com.eastcoast.MannarHotel.dto.UserResponse;
import com.eastcoast.MannarHotel.entity.Users;
import com.eastcoast.MannarHotel.exception.OurException;
import com.eastcoast.MannarHotel.repository.UserRepository;
import com.eastcoast.MannarHotel.service.CustomUserDetailsService;
import com.eastcoast.MannarHotel.service.interfaces.UserServiceInterface;
import com.eastcoast.MannarHotel.utils.JWTUtils;
import com.eastcoast.MannarHotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public Response register(UserRequest userRequest) {

        Response response = new Response();
        try{



            if(userRepository.existsByEmail(userRequest.getEmail())){
                throw new OurException(userRequest.getEmail() + "Already Exist");
            }

            Users user = new Users();



            if(userRequest.getRole() == null || userRequest.getRole().isBlank()){
                user.setRole("USER");
            }else{
                user.setRole(userRequest.getRole());
            }

            System.out.println(user.getRole() + " inside service");

            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            user.setName(userRequest.getName());
            user.setEmail(userRequest.getEmail());
            user.setPhoneNumber(userRequest.getPhoneNumber());
            Users savedUser = userRepository.save(user);
            UserResponse userDto = Utils.convertUserToUserDto(savedUser);
            response.setStatusCode(201);
            response.setUser(userDto);


        }catch (OurException e){
           response.setStatusCode(409);
           response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occured During User registration" + e.getMessage());
            throw new RuntimeException(e);
        }

        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {

        Response response = new Response();

        try{

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));

            Users user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new OurException("UserName or Password is wrong"));
            var token = jwtUtils.generateToken(user);

            response.setRole(user.getRole());
            response.setStatusCode(200);
            response.setToken(token);
            response.setExpirationTime("7 Days");
            response.setMessage("User logged in SuccessFully");


        }catch (OurException e){
          response.setStatusCode(404);
          response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Occured During User login" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllUsers() {



      Response response = new Response();
      try{
          List<Users> users = userRepository.findAll();
          List<UserResponse> userDtos = users.stream().map(Utils::convertUserToUserDto).toList();
          response.setUserList(userDtos);
          response.setStatusCode(200);
          response.setMessage("SuccessFul");
      }
      catch (Exception e) {
          response.setStatusCode(500);
          response.setMessage("Error getting all users" + e.getMessage());
      }

        return response;
    }

    @Override
    public Response getUserBookingHistory(Long userId) {

        Response response = new Response();

        try{

          Users user =   userRepository.findById(userId).orElseThrow(() -> new OurException("User is not Exist"));
          response.setBookingList(user.getBookingList().stream().map(Utils::convertBookingToBookingDto).toList());
          response.setStatusCode(200);
          response.setMessage("Successful");


        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting Users Booking History" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteUser(Long userId) {

        Response response = new Response();


        try{
            userRepository.deleteById(userId);
            response.setStatusCode(200);
            response.setMessage("Successful");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage("User is not Exist" + e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting to delete a User" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getUserById(Long userId) {

        Response response = new Response();

        try{

       Users user =   userRepository.findById(userId).orElseThrow(() -> new OurException("User is not Exist"));

       response.setUser(Utils.convertUserToUserDto(user));
       response.setStatusCode(200);
       response.setMessage("SuccessFul");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting to fetch a User" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response = new Response();

        try{

            Users user = userRepository.findByEmail(email).orElseThrow(() -> new OurException("User Not Found"));
            response.setUser(Utils.convertUserToUserInfo(user));
            response.setStatusCode(200);
            response.setMessage("SuccessFul");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting to fetch a User" + e.getMessage());
        }

        return response;
    }
}
