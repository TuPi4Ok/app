package ivan.prh.app.service.admin;

import ivan.prh.app.dto.admin.AdminRequest;
import ivan.prh.app.model.Role;
import ivan.prh.app.model.Transport;
import ivan.prh.app.model.User;
import ivan.prh.app.repository.AccountRepository;
import ivan.prh.app.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    AccountRepository accountRepository;
    @Lazy
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleService roleService;
    @Autowired
    AdminTransportService transportService;

     public List<User> getAllUsersWithParam(int start, int count) {
         var size = count + start - 1;
         Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Order.asc("id")));
         List<User> userList = accountRepository.findAll(pageable);

         List<User> resultUser = new ArrayList<>();
         int offset = 0;
         for(User user : userList) {
             if(offset >= start - 1 && offset < size)
                 resultUser.add(user);
             offset++;
         }

         if (resultUser.isEmpty())
             throw new ResponseStatusException(HttpStatus.valueOf(404), "Пользователи не найдены");
         else
             return resultUser;
     }

    public User getUser(long id) {
         Optional<User> findUser = accountRepository.findUserById(id);
         if (findUser.isPresent())
            return findUser.get();
         else
             throw new ResponseStatusException(HttpStatus.valueOf(404), "Пользователь не найден");
    }

    public User createUser(AdminRequest adminRequest) {
         if(accountRepository.findUserByUserName(adminRequest.getUsername()).isPresent())
             throw new ResponseStatusException(HttpStatus.valueOf(400), "Имя пользователя уже занято");

         User user = new User();
         user.setUserName(adminRequest.getUsername());
         user.setPassword(passwordEncoder.encode(adminRequest.getPassword()));
         user.setBalance(adminRequest.getBalance());
         if(adminRequest.isAdmin())
             user.setRoles(List.of(roleService.findRoleByName("ROLE_ADMIN")));
         else
             user.setRoles(List.of(roleService.findRoleByName("ROLE_USER")));
         accountRepository.save(user);
         return user;
    }

    public void deleteUser(long id) {
         if (accountRepository.findUserById(id).isPresent()) {
             User user = accountRepository.findUserById(id).get();
             for(Transport transport : user.getTransports()) {
                 transportService.deleteTransport(transport.getId());
             }
             accountRepository.delete(user);
         }
         else {
             throw new ResponseStatusException(HttpStatus.valueOf(404), "Пользователь не найден");
         }
    }

    public User updateUser(long id, AdminRequest adminRequest) {
         if(accountRepository.findUserByUserName(adminRequest.getUsername()).isPresent())
             throw new ResponseStatusException(HttpStatus.valueOf(400), "Имя пользователя уже занято");

         if(accountRepository.findUserById(id).isEmpty())
             throw new ResponseStatusException(HttpStatus.valueOf(404), "Пользователи не найдены");

         User currentUser = accountRepository.findUserById(id).get();
         currentUser.setUserName(adminRequest.getUsername());
         currentUser.setPassword(passwordEncoder.encode(adminRequest.getPassword()));
         currentUser.setBalance(adminRequest.getBalance());
         List<Role> userRoles = new ArrayList<>();
         if (adminRequest.isAdmin()) {
             userRoles.add(roleService.findRoleByName("ROLE_ADMIN"));
         } else {
             userRoles.add(roleService.findRoleByName("ROLE_USER"));
         }
         currentUser.setRoles(userRoles);
         accountRepository.save(currentUser);
         return currentUser;
    }
}
