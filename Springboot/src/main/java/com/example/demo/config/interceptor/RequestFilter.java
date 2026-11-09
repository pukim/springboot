package com.example.demo.config.interceptor;




@Slf4j
@Component
@Order(1) // 인터셉터보다 먼저 실행되도록 최우선 순위 설정
public class RequestBodyCachingFilter extends OncePerRequestFilter {
  @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
//    	logger.info("filter start!!!!!");
    	
        // [수정] startsWith -> contains 로 변경하여 컨텍스트 경로(/hmp-mportal-api-core)가 있어도 작동하게 함
        if (request.getRequestURI().contains("/api/")) { 
            try {
                log.info("Wrapping request for URI: {}", request.getRequestURI());
                CachedBodyHttpServletRequest  wrappedRequest  = new CachedBodyHttpServletRequest(request);
                ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
                
                filterChain.doFilter(wrappedRequest, wrappedResponse);
                wrappedResponse.copyBodyToResponse(); 
            } catch (IOException e) {
                log.error("Request body caching failed", e);
                
                filterChain.doFilter(request, response);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
    
}
