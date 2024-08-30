package ra.project.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtProvider {
    @Value("${jwt.secret_key}")
    private String SECRET_KEY;
    @Value("${jwt.expired}")
    private Long EXPIRED;

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + EXPIRED))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();

    }

    public Boolean validateToken(String token) {
        try {
        Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
        return true;
        } catch (ExpiredJwtException e) {
            log.error("JWT: Thời gian đăng nhập đã hết hạn:", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT: không hỗ trợ mã hóa:", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("JWT: Chuỗi mã hóa không đúng:", e.getMessage());
        } catch (SignatureException e) {
            log.error("JWT: Lỗi không khớp signature:", e.getMessage());
        } catch (IllegalArgumentException e) {//Đối số chuyền vào không hợp lệ
            log.error("JWT: Lỗi đối số không hợp lệ: ", e.getMessage());
        }
        return false;
    }
    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }
}
