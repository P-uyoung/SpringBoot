package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {

		SpringApplication.run(JpashopApplication.class, args);

	}

	@Bean
	Hibernate5JakartaModule hibernate5Module() {
		Hibernate5JakartaModule hibernate5Module = new Hibernate5JakartaModule();

//		/*  프록시 객체 null 값 해결
//		* 옵션을 통해서 json 생성하는 시점에, 강제로 lazay 초기화하기
//		* 하지만, 원하지 않는 객체(OrderItem) 노출됨...
//		*/
//		hibernate5Module.configure(Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING, true);
//		// 그러면, cotroller에서 for문에서 강제하자.!

		return hibernate5Module;
	}
}
