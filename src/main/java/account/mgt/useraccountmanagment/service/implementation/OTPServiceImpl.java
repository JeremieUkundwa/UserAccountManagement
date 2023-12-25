package account.mgt.useraccountmanagment.service.implementation;

import account.mgt.useraccountmanagment.service.OTPService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service("otpService")
public class OTPServiceImpl implements OTPService {

    private static final Integer EXPIRE_MINS = 4;
    private LoadingCache<String, Integer> otpCache;
    public OTPServiceImpl(){
        super();
        otpCache = CacheBuilder.newBuilder().
                expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }
    @Override
    public int generateOTP(String key) {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        otpCache.put(key, otp);
        System.out.println("OTP Generated: "+otp);
        try{
            System.out.println("OTP Reserverd in cashe: "+otpCache.get(key));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return otp;
    }

    @Override
    public int getOtp(String key) {
        try{
            System.out.println("OTP Username is: "+key);
            System.out.println("OTP Reserved: "+otpCache.get(key));
            return otpCache.get(key);
        }catch (Exception e){
            return 0;
        }
    }
    @Override
    public void clearOTP(String key) {
        otpCache.invalidate(key);
    }
}
