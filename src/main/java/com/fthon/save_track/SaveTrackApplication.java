package com.fthon.save_track;

import com.fthon.save_track.user.persistence.User;
import com.fthon.save_track.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class SaveTrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaveTrackApplication.class, args);
	}


	@Component
	@RequiredArgsConstructor
	protected class InitCommandLineRunner implements CommandLineRunner{

		private final UserRepository userRepository;

		@Override
		public void run(String... args) throws Exception {
			User user = new User("테스트 유저", 12312421L, "test@email.com");
			userRepository.save(user);
		}
	}

}