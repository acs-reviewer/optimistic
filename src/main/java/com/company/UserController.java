package com.company;

import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;



/**
 * Created by lukoyanov on 8/17/15.
 */
@RestController
@Transactional
public class UserController {

    @Autowired
    private PlatformTransactionManager transactionManager;


    @PostConstruct
    private void init(){
        repo.save(new User("Ivan", 1));
        repo.save(new User("Anatoly", 2));
        repo.save(new User("Sergei", 3));

    }

    @Autowired
    private UserRepository repo;

    @RequestMapping("findUser")
    public List<User> findUser(@RequestParam(value = "name", defaultValue = "Ivan") String name){
        return repo.findByNameLike(name);
    }

    @RequestMapping("saveUserOrUpdateUser")
    public Object saveUserOrUpdateUser(@RequestParam(value = "name") String name, @RequestParam(value = "param") long param){
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        User old = repo.findByNameLike(name).get(0);

        try {
            Thread.sleep(3000);
            if (old != null) {
                System.out.println("User is exist:" + old.getName() + ":" + old.getParam());
                old.setParam(param);
                repo.save(old);
            } else {
                repo.save(new User(name, param));
            }
            transactionManager.commit(status);
        } catch (InterruptedException e) {
            System.out.println(String.format("Interrupted exception:%s", e));
        }
        catch (StaleObjectStateException e1) {
            System.out.println(String.format("Optimistic lock exception:%s", e1));
            transactionManager.rollback(status);
        }

        return repo.findByNameLike(name);
    }
}
