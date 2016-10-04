package org.dbhatt.d_deleted_contact.Data;

/**
 * Created by devsb on 18-09-2016.
 */
public class Contact {
    private int id, raw_id;
    private String name,  account_type;

    Contact() {
        id = raw_id = 0;
        name = account_type = "";
    }

    public Contact(int id, int raw_id, String name, String account_type) {
        this.id = this.raw_id = 0;
        this.name = this.account_type = "";
        this.id = id;
        this.raw_id = raw_id;
        this.name = name;
        this.account_type = account_type;
    }

    public int getId() {
        return id;
    }

    public int getRaw_id() {
        return raw_id;
    }

    public String getName() {
        return name;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setRaw_id(int raw_id) {
        this.raw_id = raw_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }
}