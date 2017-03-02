package com.example.heavon.vo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by heavon on 2017/2/27.
 */

@Entity
public class Search {
    @Id
    private Long id;
    @Transient
    private int uid;
    @Transient
    private int count;
    @Transient
    private int hot;
    @NotNull
    private Long time;
    @NotNull
    @Unique
    private String content;

    public Search() {

    }

    public Search(String content) {
        this.content = content;
    }

    public Search(String content, Long time) {
        this.content = content;
        this.time = time;
    }

    @Keep
    public Search(Long id, Long time, @NotNull String content) {
        this.id = id;
        this.time = time;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Search{" +
                "uid=" + uid +
                ", count=" + count +
                ", hot=" + hot +
                ", time=" + time +
                ", content='" + content + '\'' +
                '}';
    }
}
