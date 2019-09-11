package com.lizl.demo.passwordbox.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "AccountModel")
public class AccountModel implements Parcelable, Comparable<AccountModel>
{
    @Id(autoincrement = true)
    @Unique
    private Long id;
    @Property(nameInDb = "description")
    private String description; // 账号描述
    @Property(nameInDb = "account")
    private String account; // 账号
    @Property(nameInDb = "password")
    private String password; // 密码
    @Property(nameInDb = "desPinyin")
    private String desPinyin;

    @Keep
    public AccountModel(Long id, String description, String account, String password, String desPinyin)
    {
        this.id = id;
        this.description = description;
        this.account = account;
        this.password = password;
        this.desPinyin = desPinyin;
    }

    public AccountModel()
    {
    }

    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getAccount()
    {
        return this.account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getDesPinyin()
    {
        return this.desPinyin;
    }

    public void setDesPinyin(String desPinyin)
    {
        this.desPinyin = desPinyin;
    }

    protected AccountModel(Parcel source)
    {
        setId(source.readLong());
        setDescription(source.readString());
        setAccount(source.readString());
        setPassword(source.readString());
        setDesPinyin(source.readString());
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(id);
        dest.writeString(description);
        dest.writeString(account);
        dest.writeString(password);
        dest.writeString(desPinyin);
    }


    public static final Parcelable.Creator<AccountModel> CREATOR = new Parcelable.Creator<AccountModel>()
    {
        @Override
        public AccountModel createFromParcel(Parcel source)
        {
            return new AccountModel(source);
        }

        @Override
        public AccountModel[] newArray(int size)
        {
            return new AccountModel[size];
        }
    };

    @Override
    public int compareTo(@NonNull AccountModel o)
    {
        char firstLetter = this.desPinyin.charAt(0);
        char toFirstLetter = o.desPinyin.charAt(0);

        if (!Character.isLetter(firstLetter))
        {
            firstLetter = '#';
        }

        if (!Character.isLetter(toFirstLetter))
        {
            toFirstLetter = '#';
        }

        if (firstLetter < toFirstLetter)
        {
            return -1;
        }
        else if (firstLetter > toFirstLetter)
        {
            return 1;
        }
        return 0;
    }
}
