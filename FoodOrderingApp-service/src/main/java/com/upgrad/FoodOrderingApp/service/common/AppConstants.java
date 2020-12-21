package com.upgrad.FoodOrderingApp.service.common;

public class AppConstants {

  // Regular Expression to check if password has at least 1 uppercase letter
  public static final String REG_EXP_PASSWD_UPPER_CASE_CHAR = "^.*[A-Z].*$";
  // Regular Expression to check  if password has at least 1 digit
  public static final String REG_EXP_PASSWD_DIGIT = "^.*[0-9].*$";
  // Regular Expression to check  if password has at least 1 special character.
  public static final String REG_EXP_PASSWD_SPECIAL_CHAR = "^.*[\\#\\@\\$\\%\\&\\*\\!\\^].*$";
  // Regular Expression to check  if email is valid
  public static final String REG_EXP_VALID_EMAIL =
      "^[a-zA-Z0-9]*[\\@]{1,1}[a-zA-Z0-9]*[\\.]{1,1}[a-zA-Z0-9]*$";
  // Regular Expression to check  if basic auth token is valid
  public static final String REG_EXP_BASIC_AUTH = "^.+[\\:]{1,1}.+$";

  // Number 1
  public static final Integer ONE_1 = 1;

  // Number '7'
  public static final Integer SEVEN_7 = 7;
  // Number '8'
  public static final Integer EIGHT_8 = 8;
  // Number '10'
  public static final Integer NUMBER_10 = 10;
  // Character ':'
  public static final String COLON = ":";
  // Text 'access-token'
  public static final String HTTP_ACCESS_TOKEN_HEADER = "access-token";
  // Text 'Basic '
  public static final String PREFIX_BASIC = "Basic ";
  // Text 'Bearer '
  public static final String PREFIX_BEARER = "Bearer ";
}
