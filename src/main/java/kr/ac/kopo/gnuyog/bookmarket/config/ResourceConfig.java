package kr.ac.kopo.gnuyog.bookmarket.config;

// Spring MVC 설정에 필요한 여러 클래스를 임포트
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration: 이 클래스가 Spring 설정 클래스임을 선언 (Bean 등록 대상)
@Configuration
// WebMvcConfigurer 인터페이스를 구현하여 Spring MVC 설정을 커스터마이징
public class ResourceConfig implements WebMvcConfigurer
{
    // @Value: application.properties(또는 yml)에서 'file.uploadDir' 키의 값을 주입받음
    // 예) file.uploadDir=D:/upload/
    @Value("${file.uploadDir}")
    String fileDir; // 파일이 실제로 저장된 서버의 디렉토리 경로를 담는 변수

    // 정적 리소스 핸들러를 등록하는 메서드 오버라이드
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        // 부모 인터페이스의 기본 동작을 먼저 수행 (기본 정적 리소스 설정 유지)
        WebMvcConfigurer.super.addResourceHandlers(registry);
        // URL 패턴 "/images/**" 로 들어오는 요청을 처리할 핸들러 등록
        registry.addResourceHandler("/images/**")
                // 실제 파일을 찾을 서버의 물리적 경로를 지정
                // "file:///" + fileDir 형식으로 로컬 파일 시스템 경로를 가리킴
                // 예) file:///D:/upload/
                .addResourceLocations("file:///" + fileDir);
        // 아래는 캐시 설정 (현재 주석 처리됨)
//                .setCachePeriod(60 * 60 * 24 * 365);
//                → 1년(초 단위)간 브라우저 캐시 유지
    }
}