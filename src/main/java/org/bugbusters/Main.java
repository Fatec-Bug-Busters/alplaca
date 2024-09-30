package org.bugbusters;

import org.bugbusters.database.hibernate.HibernateService;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world from BugBusters!");
        System.out.println("xxx No bugs here.");

        HibernateService.openSession();
        HibernateService.closeSession();
    }
}
