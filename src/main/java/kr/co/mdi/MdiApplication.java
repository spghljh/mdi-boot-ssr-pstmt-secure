package kr.co.mdi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

//----------------------------------------------------------------------

//@SpringBootApplication
//public class MdiApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(MdiApplication.class, args);
//	}
//
//}

//@Override
//protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//  return builder.sources(MdiApplication.class);
//}

//----------------------------------------------------------------------

//@SpringBootApplication
//public class MdiApplication extends SpringBootServletInitializer {
//
//	public static void main(String[] args) {
//	    ApplicationContext context = SpringApplication.run(MdiApplication.class, args);
//
//	    String[] beanNames = context.getBeanDefinitionNames();
//	    Arrays.sort(beanNames); // 보기 좋게 정렬 (선택사항)
//
//	    System.out.println("------------- Beans by IoC Container-------------");
//	    for (int i = 0; i < beanNames.length; i++) {
//	        System.out.printf("(%d) %s%n", i + 1, beanNames[i]);
//	    }
//	    System.out.println("-------------------------------------------------");
//	    System.out.printf("총 등록된 Bean 개수: %,d개%n", beanNames.length);
//	}
//
//}

//----------------------------------------------------------------------

@SpringBootApplication
public class MdiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
    	
        ApplicationContext context = SpringApplication.run(MdiApplication.class, args);

        String[] beanNames = context.getBeanDefinitionNames();
        Arrays.sort(beanNames); // 정렬
        
        // application.properties에서 spring.application.name 값 가져오기
        Environment env = context.getEnvironment();
        String appName = env.getProperty("spring.application.name", "application");
        
        // 파일 경로 설정: 예) src/main/resources/mdi-current-beans.txt
        String filePath = String.format("src/main/resources/%s-current-beans.txt", appName);
        File file = new File(filePath);
        file.getParentFile().mkdirs(); // 디렉토리 없으면 생성

        // 파일 출력
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
        	writer.write(String.format("Beans by IoC Container : %,d%n", beanNames.length));
            for (int i = 0; i < beanNames.length; i++) {
                writer.write(String.format("%d-%s%n", i + 1, beanNames[i]));
            }

        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
}