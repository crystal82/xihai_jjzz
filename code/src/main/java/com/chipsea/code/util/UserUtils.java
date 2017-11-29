//package com.chipsea.code.util;
//
//import com.accloud.cloudservice.PayloadCallback;
//import com.accloud.cloudservice.VoidCallback;
//import com.accloud.service.ACException;
//import com.accloud.service.ACObject;
//import com.accloud.service.ACThirdPlatform;
//import com.accloud.service.ACUserInfo;
//import com.chipsea.code.listener.WIFICallback;
//import com.chipsea.code.listener.WIFIVoidCallback;
//import com.chipsea.configuration.Config;
//import com.chipsea.mode.entity.AccountInfo;
//
//import chipsea.wifiplug.lib.util.csACUtil;
//
///**
// * Created by lixun on 2016/6/21.
// */
//public class UserUtils {
//    public static void login(final String phoneNumber, String password, final WIFICallback<AccountInfo> callback) {
//        csACUtil.login(phoneNumber, password, new PayloadCallback<ACUserInfo>() {
//            @Override
//            public void success(ACUserInfo acUserInfo) {
//                AccountInfo info = new AccountInfo();
//                //info.setId(acUserInfo.getUserId());
//                //info.setNickName(acUserInfo.getName());
//                //info.setPhone(phoneNumber);
//                //info.setUpdate_ts(System.currentTimeMillis());
//                if (callback != null) {
//                    callback.onSuccess(info);
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void logout() {
//        csACUtil.logout();
//    }
//
//    public static void setAvatar(byte[] data, final WIFICallback<String> callback) {
//        csACUtil.setAvatar(data, new PayloadCallback<String>() {
//            @Override
//            public void success(String s) {
//                if (callback != null) {
//                    callback.onSuccess(s);
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//
//    public static void getUserAvatar(final long accountId, final WIFICallback<String> callback) {
//        csACUtil.getUserAvatar(accountId, new PayloadCallback<String>() {
//            @Override
//            public void success(String s) {
//                if (callback != null) {
//                    callback.onSuccess(s);
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void getAccountInfo(final long accountId, final WIFICallback<AccountInfo> callback) {
//
//        csACUtil.getAccountInfo(accountId, new PayloadCallback<ACObject>() {
//            @Override
//            public void success(ACObject acObject) {
//                AccountInfo info = new AccountInfo();
//                //if(Config.USE_PHONE_FOR_LOGIN){
//                //    info.setPhone( acObject.getString("phone") );
//                //}else{
//                //    info.setPhone( acObject.getString("email") );
//                //}
//
//                //info.setNickName( acObject.getString("nickname") );
//                //info.setQq( acObject.getString("qq") );
//                //info.setWeixin( acObject.getString("wexin") );
//                //info.setSina( acObject.getString("sina") );
//                //info.setId( accountId );
//
//                if (callback != null) {
//                    callback.onSuccess(info);
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    //    public static void RemoveAllAcountf4Test(final WIFIVoidCallback callback){
//    //        csACUtil.csRemoveAllAcountf4Test(new VoidCallback() {
//    //            @Override
//    //            public void success() {
//    //                if(callback!=null){
//    //                    callback.onSuccess();
//    //                }
//    //            }
//    //
//    //            @Override
//    //            public void error(ACException e) {
//    //                if(callback!=null){
//    //                    callback.onFailure(e.getMessage(),e.getErrorCode());
//    //                }
//    //            }
//    //        });
//    //    }
//
//    public static void changePassword(String oldPwd, String newPwd, final WIFIVoidCallback callback) {
//        csACUtil.changePassword(oldPwd, newPwd, new VoidCallback() {
//            @Override
//            public void success() {
//                if (callback != null) {
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void bindWithOpenId(int loginType, String openId, String accessToken, final WIFIVoidCallback
//            callback) {
//        ACThirdPlatform lgType = ACThirdPlatform.QQ;
//        if (loginType == 2) {
//            lgType = ACThirdPlatform.WEIXIN;
//        } else if (loginType == 3) {
//            lgType = ACThirdPlatform.SINA;
//        }
//
//        csACUtil.bindWithOpenId(lgType, openId, accessToken, new VoidCallback() {
//            @Override
//            public void success() {
//                if (callback != null) {
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void bindWithAccount(String email, String phone, String password, String nickName, String
//            verifyCode, final WIFIVoidCallback callback) {
//        csACUtil.bindWithAccount(email, phone, password, nickName, verifyCode, new VoidCallback() {
//            @Override
//            public void success() {
//                if (callback != null) {
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void changeNickName(String newName, final WIFIVoidCallback callback) {
//        csACUtil.changeNickName(newName, new VoidCallback() {
//            @Override
//            public void success() {
//                if (callback != null) {
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    ////loginType: 0--phone 1--qq 2--wechat 3--sina
//    public static void loginWithOpenId(final int loginType, final String openId, String accessToken, final
//    WIFICallback<AccountInfo> callback) {
//        ACThirdPlatform lgType = ACThirdPlatform.QQ;
//        if (loginType == 2) {
//            lgType = ACThirdPlatform.WEIXIN;
//        } else if (loginType == 3) {
//            lgType = ACThirdPlatform.SINA;
//        }
//        csACUtil.loginWithOpenId(lgType, openId, accessToken, new PayloadCallback<ACUserInfo>() {
//            @Override
//            public void success(ACUserInfo acUserInfo) {
//                AccountInfo info = new AccountInfo();
//                //info.setId(acUserInfo.getUserId());
//                //info.setNickName(acUserInfo.getName());
//                //if(loginType==1){
//                //    info.setQq(openId);
//                //}else if(loginType==2){
//                //    info.setWeixin(openId);
//                //}else if(loginType==3){
//                //    info.setSina(openId);
//                //}
//                //info.setUpdate_ts(System.currentTimeMillis());
//                //if(callback!=null){
//                //    callback.onSuccess(info);
//                //}
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static boolean isLogin() {
//        return csACUtil.isLogin();
//    }
//
//    public static void register(String email, final String phone, String password, String name, String verifyCode,
//                                final WIFICallback<AccountInfo> callback) {
//        csACUtil.register(email, phone, password, name, verifyCode, new PayloadCallback<ACUserInfo>() {
//            @Override
//            public void success(ACUserInfo acUserInfo) {
//                AccountInfo info = new AccountInfo();
//                //info.setUpdate_ts(System.currentTimeMillis());
//                //info.setId(acUserInfo.getUserId());
//                //info.setNickName(acUserInfo.getName());
//                //info.setPhone(phone);
//                if (callback != null) {
//                    callback.onSuccess(info);
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void sendVerifyCode(String phoneNumber, int templateId, final WIFIVoidCallback callback) {
//        csACUtil.sendVerifyCode(phoneNumber, templateId, new VoidCallback() {
//            @Override
//            public void success() {
//                if (callback != null) {
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//
//    public static void ResetPassword(String account, String pswd, String verifyCode, final WIFIVoidCallback callback) {
//
//        csACUtil.resetPassword(account, pswd, verifyCode, new PayloadCallback<ACUserInfo>() {
//            @Override
//            public void success(ACUserInfo acUserInfo) {
//                if (callback != null) {
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void submitFeedback(String desc, final WIFIVoidCallback callback) {
//        csACUtil.submitFeedback(desc, new VoidCallback() {
//            @Override
//            public void success() {
//                if (callback != null) {
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//
//    public static void CheckPhoneExist(String phoneNumber, final WIFICallback<Boolean> callback) {
//
//        csACUtil.checkExist(phoneNumber, new PayloadCallback<Boolean>() {
//            @Override
//            public void success(Boolean aBoolean) {
//                if (callback != null) {
//                    callback.onSuccess(aBoolean);
//                }
//            }
//
//            @Override
//            public void error(ACException e) {
//                if (callback != null) {
//                    callback.onFailure(e.getMessage(), e.getErrorCode());
//                }
//            }
//        });
//    }
//}
