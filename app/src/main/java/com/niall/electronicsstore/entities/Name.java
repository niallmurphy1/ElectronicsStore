package com.niall.electronicsstore.entities;

import java.util.List;

public class Name {

    private String firstName;
    private String lastName;

    public Name(NameBuilder nameBuilder){

        this.firstName = nameBuilder.firstName;
        this.lastName = nameBuilder.lastName;
    }

    public static class NameBuilder {

        private String firstName;
        private String lastName;

        public NameBuilder(String firstName, String lastName){
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public NameBuilder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }


        public NameBuilder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Name build(){
            return new Name(this);
        }
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
