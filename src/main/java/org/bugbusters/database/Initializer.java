package org.bugbusters.database;

import org.bugbusters.database.entity.Category;
import org.bugbusters.database.entity.Plate;
import org.bugbusters.database.entity.Vehicle;
import org.bugbusters.database.hibernate.HibernateService;

import java.util.ArrayList;
import java.util.List;

public class Initializer {
    public static void main(String[] args) {


        List<Vehicle> vehicleList = new ArrayList<>();
        List<Plate> plateList = new ArrayList<>();
        List<Category> categoriesList = new ArrayList<>();

        Category category1 = new Category();
        category1.setName("Carro");
        Category category2 = new Category();
        category2.setName("Moto");

        categoriesList.add(category1);
        categoriesList.add(category2);

        Vehicle vehicle1 = new Vehicle();
        vehicle1.setColor("Azul");
        vehicle1.setCategory(category1);

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setColor("Preto");
        vehicle2.setCategory(category2);

        Vehicle vehicle3 = new Vehicle();
        vehicle3.setColor("Vermelho");
        vehicle3.setCategory(category1);

        Vehicle vehicle4 = new Vehicle();
        vehicle4.setColor("Prata");
        vehicle4.setCategory(category1);

        Vehicle vehicle5 = new Vehicle();
        vehicle5.setColor("Branco");
        vehicle5.setCategory(category2);

        vehicleList.add(vehicle1);
        vehicleList.add(vehicle2);
        vehicleList.add(vehicle3);
        vehicleList.add(vehicle4);
        vehicleList.add(vehicle5);

        Plate plate1 = new Plate();
        plate1.setIdentification("ABC1234");
        plate1.setLocation("São Paulo");
        plate1.setColor("Azul");
        plate1.setVehicle(vehicle1);

        Plate plate2 = new Plate();
        plate2.setIdentification("XYZ5678");
        plate2.setLocation("Rio de Janeiro");
        plate2.setColor("Preto");
        plate2.setVehicle(vehicle2);

        Plate plate3 = new Plate();
        plate3.setIdentification("LMN9012");
        plate3.setLocation("Belo Horizonte");
        plate3.setColor("Vermelho");
        plate3.setVehicle(vehicle3);

        Plate plate4 = new Plate();
        plate4.setIdentification("QWE3456");
        plate4.setLocation("Curitiba");
        plate4.setColor("Prata");
        plate4.setVehicle(vehicle4);

        Plate plate5 = new Plate();
        plate5.setIdentification("RTY7890");
        plate5.setLocation("Porto Alegre");
        plate5.setColor("Branco");
        plate5.setVehicle(vehicle5);

        plateList.add(plate1);
        plateList.add(plate2);
        plateList.add(plate3);
        plateList.add(plate4);
        plateList.add(plate5);

        HibernateService.openSession();

        categoriesList.forEach(HibernateService::insertValue);
        vehicleList.forEach(HibernateService::insertValue);
        plateList.forEach(HibernateService::insertValue);

        HibernateService.closeSession();
    }

}
