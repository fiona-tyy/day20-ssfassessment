package ibf2022.batch2.ssf.frontcontroller.respositories;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import ibf2022.batch2.ssf.frontcontroller.model.User;

@Repository
public class AuthenticationRepository {

	@Autowired @Qualifier("polar")
    RedisTemplate<String, Object> redisTemplate;

	// TODO Task 5
	// Use this class to implement CRUD operations on Redis

	public void saveDisabledUser(String username){
		redisTemplate.opsForValue().set(username, username, 30l, TimeUnit.MINUTES);
	}

	public boolean retrieveDisabledUser(String username){
		String user =  (String) redisTemplate.opsForValue().get(username);
		if(null == user){
			return false;
		}
		return true;
	}

}
