package cn.chl.robClass.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * <h1>JWT工具类</h1>
 * <p>
 * 这个类主要提供一系列的JWT操作，如下：
 *     <ul>
 *         <li>验证JWT是否合法: verify(String jwt[, String SECRET]):boolean</li>
 *         <li>解析jwt同时封装到对象中: getBean(Class< ?> clazz, String jwt[, String SECRET]): T</li>
 *     </ul>
 * </p><br/>
 * <div>日期: 2021/09/21</div>
 *
 * @author 陈HL_pthef
 */
public abstract class JwtUtils {

    static class ConstructorMethodException extends RuntimeException {
        public ConstructorMethodException(String msg) {
            super(msg);
        }
    }

    static class MethodException extends RuntimeException {
        public MethodException(String msg) {
            super(msg);
        }
    }

    public static final String SECRET = "ThisIsASecret";

    private static DecodedJWT getDecodedJWT(String jwt) {
        return JWT.decode(jwt);
    }

    public static Claim get(String jwt, String claim) {
        final DecodedJWT decodedJWT = getDecodedJWT(jwt);
        return decodedJWT.getClaim(claim);
    }

    public static String getAsString(String jwt, String claim) {
        return get(jwt, claim).asString();
    }

    public static boolean verify(String jwt){
        return verify(jwt, SECRET);
    }

    public static boolean verify(String jwt, String secret) {
        final JWTVerifier build = JWT.require(Algorithm.HMAC256(secret)).build();
        try{
            build.verify(jwt);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /**
     * <h2>根据传入的反射返回相应的 bean</h2>
     * <p>
     * 下面有一个这个方法的重载，很简单，不用提供 secret了，要么
     * 使用工具里自带的，要么你set一下。
     * </p>
     *
     * @param clazz  反射
     * @param jwt    jwt字符串
     * @param <T>    不用管，你传入 反射的时候会自动指定泛型
     * @return 封装好的对象
     */
    public static <T> T getBean(Class<T> clazz, String jwt) {
        DecodedJWT decodedJWT = getDecodedJWT(jwt);

        return getBean(clazz, decodedJWT);
    }

    private static String getSetMethodName(String property) {
        return "set" + property.substring(0, 1).toUpperCase() + property.substring(1);
    }

    /**
     * <h2>getBean的具体实现</h2>
     *
     * @param clazz      还是反射
     * @param decodedJWT 验证好的 DecodedJWT
     */
    private static <T> T getBean(Class<T> clazz, DecodedJWT decodedJWT) {
        Map<String, Claim> claims = decodedJWT.getClaims();

        T bean = null;

        // 创建一个空对象
        try {
            bean = clazz.getConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new ConstructorMethodException("被需求类没有找到无参构造方法");
        } catch (IllegalAccessException e) {
            throw new ConstructorMethodException("构造方法不是public");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConstructorMethodException("未知错误");
        }

        //  获取 decodedJWT 里面的所有 claims，遍历，赋值。
        for (Map.Entry<String, Claim> entry : claims.entrySet()) {
            String propertyName = entry.getKey();
            Claim claim = entry.getValue();

            try {
                //  首先得到相应的 Field，后面会用，如果没有进入catch，不管。
                Field declaredField = clazz.getDeclaredField(propertyName);

                //  得到这个字段相应的setter 方法，注意这里 getDeclaredMethod(String, Class)
                //  第二个参数一定是 Field.getType()，没有这个找不到方法
                //  这个参数就是方法要接受的参数的Class，这个Class肯定和Field 的类型一样，
                //  所以直接 Field.getType(); 就可以了
                Method declaredMethod = clazz.getDeclaredMethod(
                        getSetMethodName(propertyName), declaredField.getType()
                );

                //  吧属性封装到里面，claim 的类型应该和字段类型一样，所以as(Field.getType())
                declaredMethod.invoke(bean, claim.as(declaredField.getType()));
            } catch (NoSuchFieldException e) {

            } catch (NoSuchMethodException e) {
                throw new MethodException("没有对应的 setter 方法");
            } catch (IllegalAccessException e) {
                throw new MethodException("所以setter为什么设置成私有？");
            } catch (Exception e) {
            }

        }

        return bean;
    }
}
