package ge.tvera.service;


import ge.tvera.dao.UserDAO;
import ge.tvera.dto.UsersDTO;
import ge.tvera.dto.UsersTypeDTO;
import ge.tvera.model.UserTypes;
import ge.tvera.model.Users;
import ge.tvera.request.AddUserRequest;
import ge.tvera.utils.MD5Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * @author ucha
 */
@Service
public class UsersService {

    @Autowired
    private UserDAO userDAO;

    public List<UsersDTO> getUsers() {
        return UsersDTO.parseToList(userDAO.getAll(Users.class));
    }

    @Transactional(rollbackFor = Throwable.class)
    public Users saveUser(AddUserRequest request) throws Exception {

        Users user = new Users();

        user.setUserDesc(request.getUserDesc());
        user.setUserName(request.getUserName());
        if (request.getUserId() == null) {
            user.setUserPassword(MD5Provider.doubleMd5(request.getUserPassword()));
        }
        user.setType((UserTypes) userDAO.find(UserTypes.class, request.getTypeId()));
        user.setDeleted(request.getDeleted());

        if (request.getUserId() != null) {
            user.setUserId(request.getUserId());
            Users tmp = (Users) userDAO.find(Users.class, request.getUserId());
            if (!request.getUserPassword().equals(tmp.getUserPassword())) {
                user.setUserPassword(MD5Provider.doubleMd5(request.getUserPassword()));
            } else {
                user.setUserPassword(request.getUserPassword());
            }
            user = (Users) userDAO.update(user);
        } else {
            user = (Users) userDAO.create(user);
        }
        return user;
    }

    @Transactional(rollbackFor = Throwable.class)
    public UsersDTO changePassword(Integer userId, String pass, String newpass) throws IOException {

        Users user = userDAO.getEntityManager().find(Users.class, userId);
        // ვამოწმებთ შეცვლისას მითითებულ ძველ პაროლს, ცვლილებამდე თუ ემთხვევა არსებულს
        if (user.getUserPassword().equals(MD5Provider.doubleMd5(pass))) {

            if (user.getUserId() != null) {
                user.setUserPassword(MD5Provider.doubleMd5(newpass));
                user = (Users) userDAO.update(user);
            }

            return UsersDTO.parse(user);

        } else {
            return null;
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void delete(int id) {
        Users user = (Users) userDAO.find(Users.class, id);
        if (user != null) {
            userDAO.delete(user);
        }
    }

    public UsersDTO login(String username, String password) {
        return UsersDTO.parse(userDAO.login(username, MD5Provider.doubleMd5(password)));
    }

    public List<UsersTypeDTO> getUserTypes() {
        return UsersTypeDTO.parseToList(userDAO.getAll(UserTypes.class));
    }
}
