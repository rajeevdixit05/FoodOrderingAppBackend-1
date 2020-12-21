package com.upgrad.FoodOrderingApp.service.common;

import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import org.apache.commons.lang3.StringUtils;

import java.util.Base64;

import static com.upgrad.FoodOrderingApp.service.common.GenericErrorCode.*;

public class AppUtils {

  /**
   * Takes a basic authorization token removes prefix
   *
   * @param headerParam Basic Authorization Token
   * @return Basic Authorization Token with Prefix Removed
   */
  public static String getBearerAuthToken(String headerParam) {
    if (!headerParam.startsWith(AppConstants.PREFIX_BEARER)) {
      throw new UnexpectedException(ATHR_005);
    } else {
      String bearerToken = StringUtils.substringAfter(headerParam, AppConstants.PREFIX_BEARER);
      if (bearerToken == null || bearerToken.isEmpty()) {
        throw new UnexpectedException(GEN_001);
      } else {
        return bearerToken;
      }
    }
  }

  /**
   * Takes a bearer authorization token removes prefix
   *
   * @param headerParam Bearer Authorization Token
   * @return Bearer Authorization Token with Prefix Removed
   */
  public static String getBasicAuthToken(String headerParam) throws AuthenticationFailedException {
    if (!headerParam.startsWith(AppConstants.PREFIX_BASIC)) {
      throw new UnexpectedException(ATH_004);
    } else {
      String basicToken = StringUtils.substringAfter(headerParam, AppConstants.PREFIX_BASIC);
      if (basicToken == null || basicToken.isEmpty()) {
        throw new UnexpectedException(GEN_001);
      } else {
        try {
          return new String(Base64.getDecoder().decode(basicToken));
        } catch (Exception e) {
          throw new UnexpectedException(GEN_001, e.getCause());
        }
      }
    }
  }
}
