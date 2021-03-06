package Jobi.app.src.main.java.edu.umd.cs.jobi;

import java.util.*;

public class Position {

    private String id;
    private String title;
    private Status status = Status.TODO;
    private String location;
    private String description;
    private boolean favorite;
    private String type;
    private List<Contact> contacts;
    private Company company;

    public Story(String title, Status status, String location, String description,
                 boolean favorite, String type, List<Contact> contacts, Company company) {
        id = UUID.randomUUID().toString();
        this.title = title;
        this.status = status;
        this.location = location;
        this.description = description;
        this.favorite = favorite;
        this.type = type;
        this.contacts = contacts;
        this.company = company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite() {
        this.favorite = true;
    }

    public void unsetFavorite() {
        this.favorite = false;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public void addContact(Contact contact) {
        this.contacts.append(contact);
    }

    // TODO verify this works
    public void removeContact(Contact contact) {
        for (Iterator<Contact> iter = contacts.listIterator(); iter.hasNext(); ) {
            Contact curr = iter.next();
            if (curr.equals(contact)) {
                iter.remove();
            }
        }
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return title + ", " + company.toString();
    }

    public enum Status {
        TODO, IN_PROGRESS, DONE;
    }
}