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


    //Инициализируем некоторые начальные значения в БД
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
        User old = repo.findByNameLike(name).get(0);// mns: возможно NPE


        //Так как при попытке реализовать обработку OptimisticLockexception
        // хибернейт выбрасывает org.hibernate.StaleObjectStateException, который невозможно отловить в данном участке кода
        // по этой причине я решил сэмулировать определение Optimistic Locking и обработать его самостоятельно
        long version;
        long id;
        try {
            System.out.println("Transaction begin");

            if (old != null) {


                //Сохраним version
                version = old.getVersion();
                id = old.getId();
                System.out.println("Version:" + version + ", id:" + id);

                //Эмулируем задержку между чтением сущности и попыткой её записи
                Thread.sleep(5000);

                //Оффтопик: логгером пользоваться умею, но при определении его spring boot продолжает использовать собственный
                //решил не переопределять, и писать все в консоль
                System.out.println("User exist:" + old.getName() + ":" + old.getParam());


                //Читаем version напрямую
                System.out.println("read version:" + repo.getVersionId(id));
                if(version == repo.getVersionId(id)) {
                    // mns: А если тут другой поток поменял значение?
                    old.setParam(param);
                    repo.save(old);
                    transactionManager.commit(status);
                    System.out.println("Transaction commit");
                } else {
                    System.out.println("Optimistic locking detected - rollback");
                    status.setRollbackOnly();
                }
            } else {
                //В случае новой сущности - Optimistic locking не возникает
                repo.save(new User(name, param));
            }

        } catch (InterruptedException e) {
            System.out.println(String.format("Interrupted exception:%s", e));
        }
        //Первоначальная моя попытка отловить Optimistic lock exception
        catch (StaleObjectStateException e1) {
            System.out.println(String.format("Optimistic lock exception:%s", e1));
            status.setRollbackOnly();
        }
        //Покажем итоговое значение
        return repo.findByNameLike(name);
    }
}
