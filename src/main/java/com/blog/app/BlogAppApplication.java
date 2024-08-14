package com.blog.app;

import com.blog.app.config.AppConstants;
import com.blog.app.entities.Role;
import com.blog.app.repositories.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class BlogAppApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(this.passwordEncoder.encode("123dfdg"));

		try{
			Role adminRole=new Role();
			adminRole.setRoleId(AppConstants.ADMIN);
			adminRole.setName("ADMIN_USER");

			Role userRole=new Role();
			userRole.setRoleId(AppConstants.USER);
			userRole.setName("NORMAL_USER");

			List<Role> roles=List.of(adminRole,userRole);
			List<Role> result=this.roleRepository.saveAll(roles);

			result.forEach(r-> System.out.println(r.getName()));

		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
