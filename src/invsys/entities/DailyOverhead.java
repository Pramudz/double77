/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invsys.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import invsys.entities.compositkeys.DailyOverheadId;
import java.io.Serializable;
import javax.persistence.EmbeddedId;

/**
 *
 * @author Kanishka
 */
@Entity
@Table(name = "daily_overhead")
public class DailyOverhead implements Serializable{
    
    @EmbeddedId
    private DailyOverheadId dailyOverheadId;
    @Column(name="amount")
    private double amount;
    private String description;
    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private Users user;

    public DailyOverhead() {
    }

 
    public DailyOverhead(DailyOverheadId dailyOverheadId, double amount, String description, Users user) {
        this.dailyOverheadId = dailyOverheadId;
        this.amount = amount;
        this.description = description;
        this.user = user;
    }

    public DailyOverheadId getDailyOverheadId() {
        return dailyOverheadId;
    }

    public void setDailyOverheadId(DailyOverheadId dailyOverheadId) {
        this.dailyOverheadId = dailyOverheadId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

}
