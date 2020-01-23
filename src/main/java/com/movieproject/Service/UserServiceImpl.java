package com.movieproject.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.movieproject.Entity.Role;
import com.movieproject.Entity.User;
import com.movieproject.Repositories.RoleRespository;
import com.movieproject.Repositories.UserRepository;

import java.util.Arrays;
import java.util.HashSet;

import static java.util.Collections.emptyList;

@Service("userService")
public class UserServiceImpl implements UserDetailsService {
 
 @Autowired
 private UserRepository userRepository;
 
 @Autowired
 private RoleRespository roleRespository;
 
 @Autowired
 private BCryptPasswordEncoder bCryptPasswordEncoder;

 public User findUserByEmail(String email) {
  return userRepository.findByEmail(email);
 }

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

 @Override
 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
  User user = findUserByEmail(username);
  if (user == null) {
   throw new UsernameNotFoundException(username);
  }
  return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), emptyList());
 }
}
