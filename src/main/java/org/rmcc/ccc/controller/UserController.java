package org.rmcc.ccc.controller;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.rmcc.ccc.annotations.Loggable;
import org.rmcc.ccc.model.Role;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	//TODO: should be an app email sender address TBD
	private static final String EMAIL_SENDER = "catconservancy@gmail.com";
	//TODO: should be a list of admin users or TBD
	private static final String ADMIN_EMAIL = "catconservancy@gmail.com,michaeizl@gmail.com";
    private final UserService userService;
    private final UserCreateFormValidator userCreateFormValidator;
    private UserRepository userRepository;
    private JavaMailSender javaMailSender;

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
      	return userRepository.findByEnabled(Boolean.valueOf(params.get("enabled")));
      } else if(params.get("admin") != null) {
				return userService.getAllAdmins();
			} else {
				return (List<User>) userRepository.findAll();
			}
    }

    @Loggable
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public User getById(UsernamePasswordAuthenticationToken token, @PathVariable Long userId) {
        if (userId == 0) {
            return userService.getUserByEmail(token.getName()).get();
        } else {
            return userService.getUserById(userId).get();
        }
    }

//  @PreAuthorize("hasAuthority('ADMIN')")
	@Loggable
    @RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
    public User updateEnabled(@RequestBody User user) {
        User dbUser = userRepository.findOne(user.getId());
        if (!dbUser.isEnabled() && user.isEnabled()) {
        	dbUser.setEnabled(user.isEnabled());
        	sendUserActivatedEmail(dbUser);
        }
        dbUser.setFullName(user.getFullName());
        return userRepository.save(dbUser);
    }

//  @PreAuthorize("hasAuthority('ADMIN')")
	@Loggable
	@RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Long userId) {
		userRepository.delete(userId);
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
        	sendUserCreationEmail(form);
            return userService.create(form);
        } catch (DataIntegrityViolationException e) {
            // probably email already exists - very rare case when multiple admins are adding same user
            // at the same time and form validation has passed for more than one of them.
            LOGGER.warn("Exception occurred when trying to save the user, assuming duplicate email", e);
            bindingResult.rejectValue("email", "email.exists", "Email already exists");
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }

	// send a user activated email to user
    private void sendUserActivatedEmail(User user) {
//        MimeMessage mail = javaMailSender.createMimeMessage();
//        try {
//            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
//            helper.setTo(user.getEmail());
//            helper.setReplyTo(ADMIN_EMAIL);
//            helper.setFrom(ADMIN_EMAIL);
//            helper.setSubject("CCC User Access Granted");
//            helper.setText("Your access request has been granted.  You may now login to the application.");
//        } catch (MessagingException e) {
//            LOGGER.error("MessagingException occurred sending user activated email.", e);
//        } finally {}
//        javaMailSender.send(mail);

        Email from = new Email(EMAIL_SENDER);
        String subject = "CCC User Access Granted";
        Email to = new Email(user.getEmail());
        Content content = new Content("text/plain", "Your access request has been granted.  You may now login to the application.");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        sendMail(mail, sg, request);
    }

    // send a user creation email to admin
    private void sendUserCreationEmail(UserCreateForm user) {
//        MimeMessage mail = javaMailSender.createMimeMessage();
//        try {
//            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
//            helper.setTo(ADMIN_EMAIL);
//            helper.setReplyTo(ADMIN_EMAIL);
//            helper.setFrom(EMAIL_SENDER);
//            helper.setSubject("CCC User Created");
//            helper.setText("A new user has registered and requested access to the CCC Database: " + user.getFullName() + ": " + user.getEmail());
//        } catch (MessagingException e) {
//            LOGGER.error("MessagingException occurred sending user registration email.", e);
//        } finally {}
//        javaMailSender.send(mail);


        Email from = new Email(EMAIL_SENDER);
        String subject = "CCC User Created";
        Email to = new Email(ADMIN_EMAIL);
        Content content = new Content("text/plain", "A new user has registered and requested access to the CCC Database: " + user.getFullName() + ": " + user.getEmail());
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        sendMail(mail, sg, request);
    }

    private void sendMail(Mail mail, SendGrid sg, Request request) {
        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = mail.build();
            Response response = sg.api(request);
            LOGGER.debug("Email resp statusCode: " + response.statusCode);
            LOGGER.debug("Email resp body: " + response.body);
            LOGGER.debug("Email resp headers: " + response.headers);
        } catch (IOException ex) {
            LOGGER.error("MessagingException occurred sending user activated email.", ex);
        }
    }
}
