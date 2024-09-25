package samples;

import org.bugbusters.database.entity.Category;
import org.bugbusters.database.entity.Vehicle;
import org.bugbusters.database.hibernate.HibernateService;

public class Database {

    // Note: Before running this program, ensure that the Hibernate configuration file
    // 'hibernate.cfg.xml' is properly set up in the 'resources' directory to configure
    // the database connection and other Hibernate settings.

    public static void main(String[] args) {

        // Open session
        HibernateService.openSession();

        try {
            // Create a new category
            Category category = new Category();
            category.setName("Motorcycle");

            // Insert the category into the database
            HibernateService.insertValue(category);
            System.out.println("Category inserted successfully!");

            // Fetch the category by condition (name = 'Motorcycle')
            Category foundCategory = HibernateService.findByConditionObject(
                "categories",
                "name = 'Motorcycle'",
                Category.class
            );  // Returns only a single object

            // Fetch the category by condition (name = 'Motorcycle')
            //Category foundCategory = HibernateService.findByConditionObject(
            //  "categories",
            //  "name = 'Motorcycle'",
            //  Category.class
            // );  // Returns an object list

            // Create a new vehicle
            Vehicle vehicle = new Vehicle();
            vehicle.setColor("Blue");
            vehicle.setCategory(foundCategory);  // Associate the found category with the vehicle

            // Insert the vehicle into the database
            HibernateService.insertValue(vehicle);
            System.out.println("Vehicle inserted successfully!");

            // Update the vehicle
            vehicle.setId(1);
            vehicle.setColor("Red");  // Change the vehicle color
            HibernateService.updateValue(vehicle);
            System.out.println("Vehicle updated successfully!");

            // Read the updated vehicle
            Vehicle updatedVehicle = HibernateService.findById(vehicle.getId(), Vehicle.class);
            System.out.println("Updated vehicle: " + updatedVehicle.getColor());

            // Delete the vehicle
            HibernateService.deleteValue(updatedVehicle);
            System.out.println("Vehicle deleted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the session
            HibernateService.closeSession();
        }
    }
}
