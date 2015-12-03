package io.shick;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Pet {
 
    @Id
    @GeneratedValue
    private Long id;
     
    private String name;
     
    public Pet() {
        super();
    }
    public Pet(String name) {
        this.name = name;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String toString() {
    	return "Bark, I'm " + name;
    }
}
