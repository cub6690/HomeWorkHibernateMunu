package com.gmail.sergick6690;

import org.hibernate.HibernateException;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Entity
@Table(name="Menu")
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    @Column(nullable = false)
    private String dishName;
    @Column (nullable = false)
    private double weight;
   @Column(nullable = false)
    private double price;
    private Integer discount;


    public Dish() {
    }

    public Dish(String dishName, double weight, double price, Integer discount) {
        this.dishName = dishName;
        this.weight = weight;
        this.price = price;
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public static void addDish(EntityManager entityManager){
        Scanner sc = new Scanner(System.in);
        System.out.println("Write name");
        String name = sc.nextLine();
        System.out.println("Imput weight");
        double weight = sc.nextDouble();
        System.out.println("Imput price");
        double price = sc.nextDouble();
        System.out.println("Imput dicount");
        int discount = sc.nextInt();
        Dish dish = new Dish(name,weight,price, discount);
       try {
           entityManager.getTransaction().begin();
           entityManager.persist(dish);
           entityManager.getTransaction().commit();
           System.out.println("Dish is add");
       }catch (HibernateException e){
           e.printStackTrace();
           entityManager.getTransaction().rollback();

       }


    }

    public static List<Dish> getPrice(EntityManager em){
        Scanner sc= new Scanner(System.in);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Dish> cq = cb.createQuery(Dish.class);
        Root <Dish> dishRoot= cq.from(Dish.class);
        System.out.println("Imput from");
        int from= sc.nextInt();
        System.out.println("Imput to");
        int to= sc.nextInt();
        cq.select(dishRoot).where(cb.between(dishRoot.get("price"),from,to));
        List<Dish> dishes =em.createQuery(cq).getResultList();
        if(!dishes.isEmpty()) {
            System.out.println("Your dishes with pricees from - "+from+" to - "+to);
            for (Dish dish : dishes) {
                System.out.println(dish);
                System.out.println();
            }
        }else {
            System.out.println("No dishes found with prices from - "+from+" to - "+to);
    }
        return dishes;
    }
    public  static void getDiscount(EntityManager entityManager){
        Dish dish=null;
        Query query = entityManager.createQuery("SELECT dish from Dish dish where dish.discount>0",Dish.class );
        query.getResultList().forEach(System.out::println);
    }

        public static List<Dish> chooseOneKg(EntityManager entityManager){
        List<Dish> dishes = new ArrayList<>();
        Dish dish=null;
        double weightSum =0;
        double checkWeight =1000;

            System.out.println("----------  MENU  ----------");
        Query query = entityManager.createQuery("select dish from Dish dish", Dish.class);
        query.getResultList().forEach(System.out::println);
        Scanner sc = new Scanner(System.in);
        boolean check = true;
        for (;check;){
            if(weightSum==1000){
                System.out.println("You have reached your order limit1");
                break;
            }
            System.out.println("Press 1 to choose dish / Press 0 to finish");
            int exit=sc.nextInt();
            if(exit==0){
                System.out.println("Your total order - "+weightSum+"gr");
                break;
            }
            else if(exit==1) {
                Scanner sc1 = new Scanner(System.in);
                System.out.println("choose dish");
                String name = sc1.nextLine();
                try {
                    Query nameQuery = entityManager.createQuery("select  dish from Dish  dish where dish.dishName=:dishName", Dish.class);
                    nameQuery.setParameter("dishName", name);
                    Dish d = (Dish) nameQuery.getSingleResult();
                    double weight = d.getWeight();
                    if (checkWeight >= weight) {
                        weightSum += weight;
                        checkWeight -= weight;
                        dishes.add(d);
                        System.out.println("Your order = " + weightSum + "gr");
                    } else if (checkWeight < weight) {
                        System.out.println("Choose a dish less than - " + checkWeight + " gr.");
                        continue;

                    } else {
                        check = false;
                        System.out.println("Your total order = " + weightSum + "gr");
                    }
                } catch (NoResultException e) {
                    System.out.println("Dish not found");
                    continue;
                }catch (org.hibernate.NonUniqueResultException e){
                    System.out.println("Non unique dish found");
                    continue;
                }
            }


        }
            return dishes;
        }


    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", dishName='" + dishName + '\'' +
                ", weight=" + weight +
                ", price=" + price +
                ", discount=" + discount +
                '}';
    }
}
