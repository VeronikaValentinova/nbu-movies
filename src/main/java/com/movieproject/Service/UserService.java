package com.movieproject.Service;

import com.movieproject.Entity.User;;

public interface UserService {
  
 public User findUserByEmail(String email);
 
 public void saveUser(User user);
}
