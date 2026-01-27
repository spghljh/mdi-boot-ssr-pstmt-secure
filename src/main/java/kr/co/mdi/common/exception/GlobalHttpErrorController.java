package kr.co.mdi.common.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class GlobalHttpErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//        Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        int status = statusCode != null ? Integer.parseInt(statusCode.toString()) : 0;
        String message;

        switch (status) {
            case 400:
                message = "400 : 잘못된 요청입니다. 입력값을 확인해주세요.";
                break;
            case 404:
                message = "404 : 페이지를 찾을 수 없습니다.";
                break;
            case 500:
                message = "500 : 서버 내부 오류가 발생했습니다.";
                break;
            default:
                message = "요청 처리 중 오류가 발생했습니다.";
        }

        model.addAttribute("errorMessage", message);
        return "error/db-error";
    }
}