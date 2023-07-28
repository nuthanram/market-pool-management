package com.techno.mpm.auth.filter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import com.techno.mpm.auth.dto.AuthenticationResponse;
import com.techno.mpm.auth.exception.CustomAccessDeniedException;
import com.techno.mpm.auth.exception.DataNotFoundException;
import com.techno.mpm.pojo.candidate.OnboardDetailsRequest;
import com.techno.mpm.response.SuccessResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final CustomAccessDeniedException accessDenied;
	private final RestTemplate restTemplate;
	private final ModelMapper modelMapper;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		try {
			String url = "http://10.10.20.22:9092/api/v1/auth/varify-token";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("terminalId", request.getHeader("terminalId"));
			HttpEntity<OnboardDetailsRequest> requestEntity = new HttpEntity<>(
					OnboardDetailsRequest.builder().token(authHeader).build(), headers);
			ResponseEntity<SuccessResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					SuccessResponse.class);
			String valid = null;
			SuccessResponse successResponse = responseEntity.getBody();
			if (successResponse != null && successResponse.getData() != null) {
				AuthenticationResponse build = AuthenticationResponse.builder().build();
				modelMapper.map(successResponse.getData(), build);
				valid = Optional.ofNullable(build).filter(AuthenticationResponse::isVarifyToken).map(x -> "valid")
						.orElseThrow(() -> new DataNotFoundException("Candidate Not Found"));
			}
			if (valid != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = null;
				List<SimpleGrantedAuthority> collect = List.of("role").stream().map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, collect);
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
				filterChain.doFilter(request, response);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			try {
				accessDenied.handle(request, response, new AccessDeniedException(exception.getMessage()));
			} catch (Exception exception2) {
				log.error(exception2.getMessage());
			}
		}
	}
}
