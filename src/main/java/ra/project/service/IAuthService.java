package ra.project.service;

import ra.project.exception.CustomException;
import ra.project.model.dto.req.FormLogin;
import ra.project.model.dto.req.FormRegister;
import ra.project.model.dto.resp.JwtResponse;



public interface IAuthService {
    void register(FormRegister formRegister) throws CustomException;
    JwtResponse login(FormLogin formLogin) throws CustomException;
}
