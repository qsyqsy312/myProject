package com.test.model.base;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class BaseModel implements Serializable {

    @Id
    @GeneratedValue(generator = "customIdGenerate")
    @GenericGenerator(name = "customIdGenerate", strategy = "com.test.util.CustomIdGenerate")
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(
            name = "createTime"
    )
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(
            name = "lastModifyTime"
    )
    private Date lastModifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}
