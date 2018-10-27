package com.ptit.edu.store.admin.models.data;

import com.ptit.edu.store.customer.models.data.OrderCustomer;
import com.ptit.edu.store.product.models.data.Category;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "branch_category")
public class BranchCategory {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name= "branchID")
    private StoreBranch storeBranch;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name= "categoryID")
    private Category category;

    public BranchCategory() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StoreBranch getStoreBranch() {
        return storeBranch;
    }

    public void setStoreBranch(StoreBranch storeBranch) {
        this.storeBranch = storeBranch;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
