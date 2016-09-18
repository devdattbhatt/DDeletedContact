package org.dbhatt.ddeletedcontact.Data;

/**
 * Created by devsb on 18-09-2016.
 */
public class Contact {
    String id, raw_id, name, deleted;

    Contact() {
        id = raw_id = name = deleted = "";
    }

    Contact(String id, String raw_id, String name, String deleted) {
        this.id = this.raw_id = this.name = this.deleted = "";
        this.id = id;
        this.raw_id = raw_id;
        this.name = name;
        this.deleted = deleted;
    }

    public String getId() {
        return id;
    }

    public String getRaw_id() {
        return raw_id;
    }

    public String getName() {
        return name;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRaw_id(String raw_id) {
        this.raw_id = raw_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

}
