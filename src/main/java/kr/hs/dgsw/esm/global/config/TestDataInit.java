package kr.hs.dgsw.esm.global.config;

import kr.hs.dgsw.esm.domain.category.entity.Category;
import kr.hs.dgsw.esm.domain.category.repository.CategoryRepository;
import kr.hs.dgsw.esm.domain.member.entity.Member;
import kr.hs.dgsw.esm.domain.member.entity.MemberRole;
import kr.hs.dgsw.esm.domain.member.repository.MemberRepository;
import kr.hs.dgsw.esm.domain.product.entity.Product;
import kr.hs.dgsw.esm.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("local")
@RequiredArgsConstructor
public class TestDataInit implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Init Member
        if (!memberRepository.existsByEmail("test@test.com")) {
            memberRepository.save(Member.builder()
                    .email("test@test.com")
                    .password(passwordEncoder.encode("1234"))
                    .name("테스트유저")
                    .role(MemberRole.USER)
                    .build());
        }

        // Init Categories
        Category electronics = categoryRepository.save(Category.builder().name("가전").build());
        Category clothing = categoryRepository.save(Category.builder().name("의류").build());

        // Init Products
        productRepository.save(Product.builder()
                .name("맥북 에어")
                .price(1500000)
                .stock(10)
                .description("M3 칩셋 탑재")
                .category(electronics)
                .build());

        productRepository.save(Product.builder()
                .name("아이폰 15")
                .price(1200000)
                .stock(20)
                .description("티타늄 바디")
                .category(electronics)
                .build());

        productRepository.save(Product.builder()
                .name("기본 티셔츠")
                .price(20000)
                .stock(100)
                .description("편안한 면 소재")
                .category(clothing)
                .build());
    }
}
