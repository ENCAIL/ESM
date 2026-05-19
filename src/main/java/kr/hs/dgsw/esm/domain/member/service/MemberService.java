package kr.hs.dgsw.esm.domain.member.service;

import kr.hs.dgsw.esm.domain.member.dto.request.LoginRequest;
import kr.hs.dgsw.esm.domain.member.dto.request.SignupRequest;
import kr.hs.dgsw.esm.domain.member.dto.response.MemberResponse;
import kr.hs.dgsw.esm.domain.member.dto.response.TokenResponse;
import kr.hs.dgsw.esm.domain.member.entity.Member;
import kr.hs.dgsw.esm.domain.member.entity.MemberRole;
import kr.hs.dgsw.esm.domain.member.repository.MemberRepository;
import kr.hs.dgsw.esm.global.exception.BusinessException;
import kr.hs.dgsw.esm.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signup(SignupRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(409, "이미 존재하는 이메일입니다.", "ALREADY_EXISTS_EMAIL");
        }

        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(MemberRole.USER)
                .build();

        memberRepository.save(member);
    }

    public TokenResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(401, "아이디 또는 비밀번호가 일치하지 않습니다.", "LOGIN_FAILED"));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BusinessException(401, "아이디 또는 비밀번호가 일치하지 않습니다.", "LOGIN_FAILED");
        }

        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public MemberResponse getMyInfo(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(404, "사용자를 찾을 수 없습니다.", "MEMBER_NOT_FOUND"));
        return MemberResponse.of(member);
    }
}
