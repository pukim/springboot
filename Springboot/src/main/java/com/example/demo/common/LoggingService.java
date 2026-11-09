
	// 응답 로깅
	@Async
	public void excuteAccessLog(HttpServletRequest request, int responseCode, String  responseBody, String trace) {
		try {
			if (request.getAttribute("ACCESS_LOG") != null) {
				
				JsonObject logObj = (JsonObject) request.getAttribute("ACCESS_LOG");

				if(responseCode != 200)	{
					if(trace!= null) {
						JsonStringEncoder encoder = JsonStringEncoder.getInstance();
						char[] escapedJson = encoder.quoteAsString(trace);
						trace = String.valueOf(escapedJson);				
					}
					else {
						trace = "null";
					}
					logObj.put("isError", 1);
					logObj.put("errorStack", trace);
				}
				
				long time = System.currentTimeMillis();
				
				logObj.remove("request");  // 요청 파라미터 제거
				
				// MDC Put
				logObj.put("log_type", "api_response");
				logObj.put("taskId", System.getenv().get("HOSTNAME"));
				logObj.put("service_id", hcpConfiguration.getRscId());
				logObj.put("env", hcpConfiguration.getEnv());
				logObj.put("prjId", hcpConfiguration.getPrjId());
				logObj.put("responseTime", time - logObj.getLong("START_TIME", time));  // API 처리 시간
				//"START_TIME 필드 삭제"
				logObj.remove("START_TIME");
				logObj.put("service_full_id", String.format("/hcp/%s/%s/%s", hcpConfiguration.getPrjId(), hcpConfiguration.getEnv(), hcpConfiguration.getRscId()));

				logObj.put("responseCode", responseCode);
				
			/**
				// Filter에서 감싸둔 Wrapper 클래스를 가져옵니다.
		        ContentCachingResponseWrapper responseWrapper = 
		                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);

		        if (responseWrapper != null) {
		            // 캐시된 바이트 배열을 꺼내어 문자열로 변환합니다.
		            byte[] buf = responseWrapper.getContentAsByteArray();
		            String responseBody = new String(buf, StandardCharsets.UTF_8);
		            
		            // 응답 값 활용 (로그 출력 등)
		            log.info("Response Body: " + responseBody);
			        logObj.put("response", responseBody);
		        } else {
		        	logObj.put("response", "");
		        }
			**/
				
				try {
				    JsonObject logVal = logObj.getJsonObject("log");
	 	            
					if("".equals(responseBody)) {
						logVal.put("val", "");
					} else {
						//logObj.put("response", new JsonObject(responseBody));
						if(responseBody.length() > 60000) {
							logVal.put("val", String.valueOf(responseBody.substring(0, 60000)));
						} else {
							logVal.put("val", String.valueOf(responseBody));
						}
					}
					
					logObj.put("log", logVal);
					
				} catch(Exception e) {
					e.printStackTrace();
				}
				
		        if(logObj.containsKey("req_res_key")) {
		        	log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		        	log.info(">> api_response stash send ::: {}", (String)logObj.getValue("req_res_key"));
		        	log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
					
		        	webClient.sendUdpStream(logObj);
					
					//request.setAttribute("ACCESS_LOG", null);					
				} else {
					log.info(">> api_response stash skip");
				}
			} else {
				log.info(">> api_response ==> request.getAttribute(\"ACCESS_LOG\") is null !!!!! ");
			}

		} catch (Exception e) {
            // 에러 발생 시 로그
            log.error("[ERROR] {}.{}() | Exception: {}", e.getMessage());
		}
	}
	

	// 요철 로깅
	public void initAccessLog(HttpServletRequest request, JsonObject sessionObj) {

		try {
			JsonObject logObj = (JsonObject) request.getAttribute("ACCESS_LOG");

			log.info("################################################################################################################");
			log.info(logObj.toString());
			if(logObj.containsKey("host")) {
				log.info(" HOST :::  {} ", logObj.getValue("host"));
			}
			log.info("################################################################################################################");
			
			long time = System.currentTimeMillis();
			String now = DateUtil.now("yyyyMMddHHmmssSSS");
			String host = request.getHeader("host");
			String ip = request.getHeader("X-Real-IP");
			String userAgentString = request.getHeader("User-Agent");
			String uri = request.getRequestURI();

			String referer = request.getHeader("referer");
			String rscId = hcpConfiguration.getRscId();

			if (ip == null && host != null)
				ip = host.split("\\:")[0];
			if (ip != null && ip.contains(",")) {
				logObj.put("IPS", ip);
				ip = ip.split("\\,")[0].trim();
			}

			String empNo = sessionObj.getString("empId",
					SSOUtil.getSMSESSIONUserFromCookie(request.getHeader("Cookie")));
			if (empNo == null || empNo.equals("")) {
				empNo = ip;
			}
			
			logObj.put("log_type", "api_request");
			logObj.put("clientIp", ip);
			logObj.put("rscId", rscId);
			//logObj.put("sessionId", sessionObj.getString("token", ""));
			logObj.put("client", sessionObj.getValue("client", 0));
			logObj.put("api", uri);
			logObj.put("referer", referer);
			logObj.put("isError", 0);
			logObj.put("errorStack", "");
			//logObj.put("timestamp", now);
			logObj.put("log_date", DateTimeFormatter.ISO_INSTANT.format(ZonedDateTime.now()));
			logObj.put("START_TIME", time);
			logObj.put("traceId", uri + ":" + time + ":" + ip);
			logObj.put("system_id", host);
			logObj.put("userAgentString", userAgentString);
			logObj.put("req_res_key", String.format("%s:%s.%s", ip, now, genUuid()));
			
//			log.info("set_req_res_eky ::: {}", logObj.getValue("req_res_key"));
			// hcp 설정 정보
			logObj.put("locId", hcpConfiguration.getLocId());
			logObj.put("app", hcpConfiguration.getApp());
			logObj.put("target", hcpConfiguration.getTarget());

		
			log.info("new req_res_key ===> {}", logObj.getValue("req_res_key"));
			request.setAttribute("ACCESS_LOG", logObj);
		
			String body = "";
	        if (request instanceof CachedBodyHttpServletRequest) {
	            // Wrapper가 적용된 경우, 전용 메소드로 바디를 가져옴 (표준 메소드 호출 X)
	            body = ((CachedBodyHttpServletRequest) request).getBody();
	        } else {
	            // Wrapper가 없는 경우 (예외 상황)
	            log.warn("Request is not wrapped with CachedBodyHttpServletRequest");
	            // 필요 시 여기서 getReader()를 쓰거나 에러 처리
	        }
	        
	        if("".equals(body) || body == null || body.trim().isEmpty()) {
	        	logObj.put("request", "");                    // request parameters
	        } else {
	        	//logObj.put("request", new JsonObject(body));  // request parameters
	        	JsonObject reqParams = new JsonObject(body);
	        	if(reqParams.containsKey("accessToken")) {
	        		reqParams.remove("accessToken"); 
	        	}
	        	
	        	JsonObject logVal = new JsonObject();
				try {
					logVal.put("traceId", request.getParameterMap().containsKey("traceId") ? request.getParameter("traceId")
							: uri + ":" + time + ":" + ip);
					logVal.put("trxId", request.getParameterMap().containsKey("trxId") ? request.getParameter("trxId")
							: uri + ":" + time);
					logVal.put("traceId",
							request.getParameterMap().containsKey("SYSTEM_ID") ? request.getParameter("SYSTEM_ID") : host);
					logVal.put("val"  , String.valueOf(reqParams));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
	        	logObj.put("log", logVal);  // request parameters
	        }
	        
	        if(logObj.containsKey("req_res_key")) {
	        	log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	        	log.info(">> api_request stash send ::: {}", (String)logObj.getValue("req_res_key"));
	        	log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
				
	        	webClient.sendUdpStream(logObj);
			} else {
				log.info(">> api_request stash skip");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
