package com.movieproject.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.movieproject.Entity.Role;
import com.movieproject.Entity.User;
import com.movieproject.Repositories.RoleRespository;
import com.movieproject.Repositories.UserRepository;

import java.util.Arrays;
import java.util.HashSet;

@Service("userService")
public class UserServiceImpl implements UserService {
 
 @Autowired
 private UserRepository userRepository;
 
 @Autowired
 private RoleRespository roleRespository;
 
 @Autowired
 private BCryptPasswordEncoder bCryptPasswordEncoder;

 @Override
 public User findUserByEmail(String email) {
  return userRepository.findByEmail(email);
 }

 @Override
 public void saveUser(User user) {
  
  user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
  user.setActive(1);
  Role userRole = null;
  if(user.getEmail().equals("root@abv.bg")) 
	   userRole = roleRespository.findByRole("ADMIN");
  else 
	   userRole = roleRespository.findByRole("USER");
  
  user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
  userRepository.save(user);
 }

}
