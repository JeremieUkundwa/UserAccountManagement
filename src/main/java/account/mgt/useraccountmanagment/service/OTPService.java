package account.mgt.useraccountmanagment.service;

public interface OTPService {
    int generateOTP(String key);
    int getOtp(String key);
    void clearOTP(String key);
}
