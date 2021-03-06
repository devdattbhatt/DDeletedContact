/*
 * Copyright (c) 2016. Devdatt s bhatt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.dbhatt.d_deleted_contact.data;

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