package cn.chl.robClass.controller;

import cn.chl.robClass.controller.entity.ResponseData;
import cn.chl.robClass.controller.entity.UserLoginRequest;
import cn.chl.robClass.entity.vo.UserVO;
import cn.chl.robClass.service.IUserService;
import cn.chl.robClass.utils.JwtUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private IUserService service;

    @PostMapping("/login")
    public ResponseData<UserVO> login(@RequestBody UserLoginRequest loginRequest) {

        log.info("login request entry, id:{}, pw:{}", loginRequest.getId(), loginRequest.getPw());

        String id = loginRequest.getId();
        String pw = loginRequest.getPw();

        if (StringUtils.isEmpty(id)) {
            return ResponseData.fail(null);
        }

        if (StringUtils.isEmpty(pw)) {
            return ResponseData.fail(null);
        }

        UserVO user = service.login(id, pw);

        if(user == null)
            return ResponseData.fail(null);

        Map<String, Object> jwtHeader = new HashMap<>();
        jwtHeader.put("typ", "jwt");
        jwtHeader.put("alg", "sha256");

        String jwt = JWT.create().withHeader(jwtHeader)
                .withClaim("id", user.getId())
                .withClaim("name", user.getName())
                .withClaim("clsNo", user.getClsNo())
                .withClaim("gender", user.getGender())
                .sign(Algorithm.HMAC256(JwtUtils.SECRET.getBytes()));


        return ResponseData.ok(user).auth(jwt);
    }
}
