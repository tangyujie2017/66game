package cn.game.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cn.game.core.repository.user.UserRepository;
import cn.game.core.table.Authority;
import cn.game.core.table.Role;
import cn.game.core.table.User;
@Service
public class SecurityUserDetailsService implements UserDetailsService{

	  @Autowired
	  private UserRepository userRepository;

	  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         System.out.println("username:"+username);
	    Optional<User> userOpt = userRepository.findByLogin(username);

	    User user = userOpt.orElseThrow(() -> new UsernameNotFoundException("用户名不存在: " + username));

	    List<GrantedAuthority> auth = getGrantedAuthorities(user.getRoles());

	    String password = user.getPassword();

	    return new org.springframework.security.core.userdetails.User(username, password, auth);
	  }


	  public static List<GrantedAuthority> getGrantedAuthorities(List<Role> roles) {
	    List<GrantedAuthority> authorities = new ArrayList<>();
	    roles.forEach(r -> {
	    	List<Authority> auths = r.getAuthoritys();
	    	for(Authority auth : auths){
	    		authorities.add(new SimpleGrantedAuthority(auth.getCode()));
	    	}
	    	authorities.add(new SimpleGrantedAuthority("ROLE_"+r.getName()));
	    });
	    return authorities;
	  }
}
