//package com.ecommerce.admin.config;
//
//import com.ecommerce.library.model.Admin;
//import com.ecommerce.library.model.Role;
//import com.ecommerce.library.repository.AdminRepository;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.stream.Collectors;
//
//public class AdminServiceConfig implements UserDetailsService {
//
//    private AdminRepository adminRepository;
//
////    @Override
////    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
////        Admin admin = adminRepository.findByUserName(username);
////        if (admin == null) {
////            throw new UsernameNotFoundException("Can not find username");
////        }
////
////        return new User(
////                admin.getUserName(),
////                admin.getPassword(),
////                admin.getRoles()
////                .stream()
////                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
////    }
//
//    @Override
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        Admin admin = adminRepository.findByUserName(userName);
//
//        if (admin == null) {
//            throw new UsernameNotFoundException("Invalid username or password.");
//        }
//
//        Collection<SimpleGrantedAuthority> authorities = mapRolesToAuthorities(admin.getRoles());
//
//        return new org.springframework.security.core.userdetails.User(admin.getUserName(), admin.getPassword(),
//                authorities);
//    }
//
//    private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
//        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//
//        for (Role tempRole : roles) {
//            SimpleGrantedAuthority tempAuthority = new SimpleGrantedAuthority(tempRole.getName());
//            authorities.add(tempAuthority);
//        }
//
//        return authorities;
//    }
//}
