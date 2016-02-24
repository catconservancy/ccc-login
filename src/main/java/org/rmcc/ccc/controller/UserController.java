package org.rmcc.ccc.controller;

import org.rmcc.ccc.annotations.Loggable;
import org.rmcc.ccc.model.User;
import org.rmcc.ccc.model.UserCreateForm;
import org.rmcc.ccc.model.validator.UserCreateFormValidator;
import org.rmcc.ccc.repository.UserRepository;
import org.rmcc.ccc.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	private UserRepository userRepository;
    private JavaMailSender javaMailSender;
    private final UserService userService;
    private final UserCreateFormValidator userCreateFormValidator;
	
	@Autowired
	public UserController(UserRepository userRepository,
			JavaMailSender javaMailSender,
			UserService userService, 
			UserCreateFormValidator userCreateFormValidator) {
		this.userRepository = userRepository;
		this.javaMailSender = javaMailSender;
		this.userService = userService;
        this.userCreateFormValidator = userCreateFormValidator;
	}	

	@RequestMapping(method = RequestMethod.GET)
    public List<User> search(@RequestParam Map<String,String> params) throws Exception {
		if (params.get("enabled") != null) {
			return (List<User>) userRepository.findByEnabled(Boolean.valueOf(params.get("enabled")));
		}
        return (List<User>) userRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public User update(@RequestBody User user) {
        User dbUser = userRepository.findOne(user.getId());
        if (!dbUser.isEnabled() && user.isEnabled()) {
            sendEmail(user);
        }
        return userRepository.save(user);
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
	@Loggable
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
    public User handleUserCreateForm(@Valid @RequestBody UserCreateForm form, BindingResult bindingResult) throws MethodArgumentNotValidException {
        LOGGER.debug("Processing user create form={}, bindingResult={}", form, bindingResult);
        userCreateFormValidator.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
        	throw new MethodArgumentNotValidException(null, bindingResult);
        }
        try {
            sendEmail(form);
            return userService.create(form);
        } catch (DataIntegrityViolationException e) {
            // probably email already exists - very rare case when multiple admins are adding same user
            // at the same time and form validation has passed for more than one of them.
            LOGGER.warn("Exception occurred when trying to save the user, assuming duplicate email", e);
            bindingResult.rejectValue("email", "email.exists", "Email already exists");
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }

    private void sendEmail(User user) {
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo("aaron.g.jones@gmail.com");
            helper.setReplyTo("aaron.g.jones@gmail.com");
            helper.setFrom("aaron.g.jones@gmail.com");
            helper.setSubject("CCC User Created");
            helper.setText("A new user has registered and requested access to CCC Databse: " + user.getFullName() + ": " + user.getEmail());
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {}
        javaMailSender.send(mail);
    }

    private void sendEmail(UserCreateForm user) {
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo("aaron.g.jones@gmail.com");
            helper.setReplyTo("aaron.g.jones@gmail.com");
            helper.setFrom("aaron.g.jones@gmail.com");
            helper.setSubject("CCC User Created");
            helper.setText("A new user has registered and requested access to CCC Databse: " + user.getFullName() + ": " + user.getEmail());
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {}
        javaMailSender.send(mail);
    }
}
