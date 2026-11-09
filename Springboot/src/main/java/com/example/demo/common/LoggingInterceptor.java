
@Slf4j
@Component
@Qualifier("loggingInterceptor")


  public class LoggingInterceptor extends HandlerInterceptorAdapter implements  AsyncHandlerInterceptor { /// HandlerInterceptor  {
public LoggingInterceptor() {
		super();
	}

	//private static final String START_TIME_ATTR = "startTime";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 비동기 처리 후 재호출(ASYNC Dispatch)인 경우 시작 로그 중복 출력 방지
//		WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
//		if(asyncManager.isConcurrentHandlingStarted() || CorsUtils.isPreFlightRequest(request)) {
//			log.info("Async manager check !!!!");
//			return true;
//		}
		
		// Async 처리 흐름상 두 번째 호출 되는 경우  처리 Skip
		if(request.getAttribute("ACCESS_LOG") != null) {
			log.info("###################################  ACCESS_LOG is not empty ################################");
			JsonObject logObj = (JsonObject) request.getAttribute("ACCESS_LOG");
			if(logObj.containsKey("req_res_key")) {
				log.info("pre handle ");
 				log.info("req_res_key ==> {}" , logObj.getValue("req_res_key"));
 				
 				return true; 
 			}
		}

		
		try {
			
			JsonObject sessionObj = new JsonObject();
			JsonObject logObj = new JsonObject();
			request.setAttribute("SESSION_INFO", sessionObj);
			request.setAttribute("ACCESS_LOG"  , logObj);

	        if (request instanceof CachedBodyHttpServletRequest) {
	            // Wrapper가 적용된 경우, 전용 메소드로 바디를 가져옴 (표준 메소드 호출 X)
	            String reqBody = ((CachedBodyHttpServletRequest) request).getBody();
	            
		        if (reqBody != null && !reqBody.isEmpty()) {
		        	ObjectMapper objectMapper = new ObjectMapper();
		        	SearchParams  params = objectMapper.readValue(reqBody, SearchParams.class);
		            
		        	JsonObject user = new JsonObject();
		        	try {
			        	user.put("empId", params.getEmpId());
			        	user.put("companyCd", params.getCompanyCd());
			        	user.put("name" , getUserInfo(params.getEmpId(), "userNm"));
			        	user.put("mdn"  , getUserInfo(params.getEmpId(), "mobile"));
		        	} catch(Exception e) {
		        		log.error(e.getMessage());
		        	}
		        	logObj.put("user", user);
		        }
	        } else {
	            // Wrapper가 없는 경우 (예외 상황)
	            log.warn("Request is not wrapped with CachedBodyHttpServletRequest");
	            
	            // 필요 시 여기서 getReader()를 쓰거나 에러 처리
	        }
	        
			/* SETTING SESSION 
			String metaString = request.getHeader("META");
			if (metaString != null && !metaString.equals("") && metaString.startsWith("{")) {
				JsonObject META = new JsonObject(metaString);
				if (META != null) {
					String uuid = META.getString("uuid", "");
//					String sessionStr = RedisApiClient.getSessionUserInfo(uuid);
					// 캐쉬 사용으로 변경
					String sessionStr = sessionService.getSessionUserInfo(uuid);
					if (sessionStr != null && !sessionStr.equals("")) {
						sessionObj = new JsonObject(sessionStr);
						
						log.info("sessionObj => {}", sessionObj);

						request.setAttribute("sessionInfo", sessionStr);
					}
				}
			} else {
//				log.info("[META]:" + request.getPathInfo());
//				log.info("[META]:" + metaString);
			}
		    */

			loggingService.initAccessLog(request, sessionObj);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	 
		int responseCode = response.getStatus();
		String trace = MDC.get("trace");
		
		if(!request.getMethod().equals("OPTIONS")) {
			
			// Async 처리 특성상  한 번 더 호출 됨
//			if(request.getDispatcherType() == DispatcherType.ASYNC && exception == null) {
//				return;
//			}
			log.info("POST ###########################################################################################");
			if(request.getDispatcherType() == DispatcherType.ASYNC) {
				log.info("ASYNC");
			} else if(request.getDispatcherType() == DispatcherType.ERROR) {
				log.info("ERROR");
			} else if(request.getDispatcherType() == DispatcherType.INCLUDE) {
				log.info("INCLUDE");
			} else if(request.getDispatcherType() == DispatcherType.FORWARD) {
				log.info("FORWARD");
			} else if(request.getDispatcherType() == DispatcherType.REQUEST) {
				log.info("REQUEST");
			} else {
				log.info("OTHER");
			}
			log.info("#################################################################################################");
			
			 ContentCachingResponseWrapper responseWrapper = 
		                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
//			ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
			
		 	String responseBody = "";
	        if (responseWrapper != null) {
	            // 캐시된 바이트 배열을 꺼내어 문자열로 변환합니다.
	            byte[] buf = responseWrapper.getContentAsByteArray();
	            responseBody = new String(buf, StandardCharsets.UTF_8);
            } 
	        
	        responseWrapper.copyBodyToResponse();	
	        log.info("post handler");
	        
	        loggingService.excuteAccessLog(request, responseCode, responseBody, trace);
		}
		//super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
		/***
		int responseCode = response.getStatus();
		String trace = MDC.get("trace");
		
		if(!request.getMethod().equals("OPTIONS")) {
			
			// Async 처리 특성상  한 번 더 호출 됨
//			if(request.getDispatcherType() == DispatcherType.ASYNC && exception == null) {
//				return;
//			}
			 ContentCachingResponseWrapper responseWrapper = 
		                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
//			ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
			
		 	String responseBody = "";
	        if (responseWrapper != null) {
	            // 캐시된 바이트 배열을 꺼내어 문자열로 변환합니다.
	            byte[] buf = responseWrapper.getContentAsByteArray();
	            responseBody = new String(buf, StandardCharsets.UTF_8);
            } 
	        
	        responseWrapper.copyBodyToResponse();	
	        log.info("aftercomplete handler");
	        loggingService.excuteAccessLog(request, responseCode, responseBody, trace);
		}
		super.afterCompletion(request, response, handler, exception);
		***/
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
//	    log.info("ASYNC handling :::: ");
//	    int responseCode = response.getStatus();
//		String trace = MDC.get("trace");
//		
//		ContentCachingResponseWrapper responseWrapper = 
//                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
////	ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
//	
//		String responseBody = "";
//	    if (responseWrapper != null) {
//	        // 캐시된 바이트 배열을 꺼내어 문자열로 변환합니다.
//	        byte[] buf = responseWrapper.getContentAsByteArray();
//	        responseBody = new String(buf, StandardCharsets.UTF_8);
//	        
//	        // 응답 값 활용 (로그 출력 등)
//	        log.info("Response Body: " + responseBody);
//	    }
//	    
//	    responseWrapper.copyBodyToResponse();	        
//    	
//	    if(responseBody != null || !"".equals(responseBody)) {
//	    	loggingService.excuteAccessLog(request, responseCode, responseBody, trace);
//	    }
//	    
//		super.afterConcurrentHandlingStarted(request, response, handler);
	}
	
	// cache의 사용자 정보 조회
	private String getUserInfo(String empId, String fld) {
		
		String rtnVal = "";
		Cache cache = cacheManager.getCache(CacheConfig.CACHE_MYINFO);
		Map<String,Object> cacheVal = cache.get(empId, Map.class);
		
		if(cacheVal != null) {
//			log.info("cache ::: {}", cacheVal.toString());
			if(cacheVal.containsKey(fld)) {
				rtnVal = (String)cacheVal.get(fld);
			}
		} 
//		log.info("user info {} ==> {}", fld, rtnVal);
		return rtnVal;
	}
}
