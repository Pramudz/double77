/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invsys.entities.compositkeys;

import invsys.entities.OverheadCategory;
import java.io.Serializable;
import java.sql.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Kanishka
 */
@Embeddable
public class DailyOverheadId implements Serializable{
    
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "overhead_category_id", referencedColumnName = "overhead_category_id")
    private OverheadCategory overheadCategory;
    @Column(name="date_of_entry")
    private java.sql.Date dateOfEntry;    


    public DailyOverheadId(OverheadCategory overheadCategory, Date dateOfEntry) {
        this.overheadCategory = overheadCategory;
        this.dateOfEntry = dateOfEntry;
    }

    public OverheadCategory getOverheadCategory() {
        return overheadCategory;
    }

    public void setOverheadCategory(OverheadCategory overheadCategory) {
        this.overheadCategory = overheadCategory;
    }

    public Date getDateOfEntry() {
        return dateOfEntry;
    }

    public void setDateOfEntry(Date dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    } 
}
