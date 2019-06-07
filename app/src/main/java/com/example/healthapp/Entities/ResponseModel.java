package com.example.healthapp.Entities;

public class ResponseModel {
    private String Message;
    private boolean Status;
    private String Data;


    // Getter Methods

    public String getMessage() {
        return Message;
    }

    public boolean getStatus() {
        return Status;
    }

    public String getData() {
        return Data;
    }

    // Setter Methods

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public void setStatus(boolean Status) {
        this.Status = Status;
    }

    public void setData(String Data) {
        this.Data = Data;
    }
}
