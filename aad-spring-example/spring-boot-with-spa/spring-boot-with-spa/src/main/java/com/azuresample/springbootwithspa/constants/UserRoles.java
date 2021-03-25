package com.azuresample.springbootwithspa.constants;

public enum UserRoles {
    READ("UI_READ"),
    WRITE("UI_WRITE");

    private String accessRole;

    UserRoles(String accessRole){
        this.accessRole = accessRole;
    }
    public String getAccessRoles(){
        return this.accessRole;
    }
}

