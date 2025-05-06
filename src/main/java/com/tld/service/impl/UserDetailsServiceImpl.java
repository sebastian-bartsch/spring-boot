package com.tld.service.impl;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tld.entity.Permission;
import com.tld.entity.Role;
import com.tld.entity.Users;
import com.tld.jpa.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Users> user = userRepository.findByUserName(username);
		if (user.isEmpty()) {
			throw new UsernameNotFoundException("Usuario no encontrado: " + username);
		}
		
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.get().getRole()) {
//            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        	grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
            for (Permission permission : role.getPermissions()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(permission.getPermissionName()));
            }
        }

        return new org.springframework.security.core.userdetails.User(
        		user.get().getUserName(), user.get().getUserPassword(), 
        		user.get().isUserEnabled(), user.get().isAccountNonExpired(), 
        		user.get().isCredentialsNonExpired(), user.get().isAccountNonLocked(), grantedAuthorities);
	}
}
