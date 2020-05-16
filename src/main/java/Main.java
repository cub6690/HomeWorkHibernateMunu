import com.gmail.sergick6690.Dish;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAMenu");
        EntityManager em = emf.createEntityManager();
        try {
            Dish menu = new Dish("1", 80, 58, 0);
            Dish menu1 = new Dish("2", 250, 55.7, 20);
            Dish menu2 = new Dish("3", 250, 20, 20);
           Dish menu3 = new Dish("4", 450, 25, 10);
            Dish menu4 = new Dish("5", 300, 10, 20);

            em.getTransaction().begin();
            em.persist(menu);
            em.persist(menu1);
            em.persist(menu2);
            em.persist(menu3);
            em.persist(menu4);
            em.getTransaction().commit();
            boolean end=false;
            Scanner sc = new Scanner(System.in);
            for(;end==false;){
                System.out.println("To add dish press 1");
                System.out.println("To search dishes with a price press 2");
                System.out.println("To search dishes with a discount press 3");
                System.out.println("To make an order weighing no more than one kg press 4");
                System.out.println("Press 0 for exit ");
                System.out.print("->");
                int num = sc.nextInt();
                if(num==1){
                     Dish.addDish(em);
                }else if (num==2) {
                    List<Dish> dishList= Dish.getPrice(em);
                }else if(num==3){

                }else if(num==4){
                    List<Dish> dishes= Dish.chooseOneKg(em);
                    for(Dish dish:dishes){
                        System.out.println(dish);

                    }
                }
                else if(num==0){
                    end=true;
                    System.out.println("Good bye");
                }
            }
        }finally {
            em.close();
            emf.close();}



    }}
