package com.challengesstore.data.model.register;


import com.google.gson.annotations.SerializedName;

public class UserSignUp {

    @SerializedName("first_name")
    private String name;

    @SerializedName("last_name")
    private String surname;

    @SerializedName("username")
    private String userName;

    @SerializedName("check_terms")
    private boolean checkTerms = false;

    @SerializedName("email")
    private String email;

    @SerializedName("plainPassword")
    private String password;

    private transient String passwordRepeat;

    public UserSignUp() {

    }

    public UserSignUp(String name, String surname, String userName, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public UserSignUp(String name, String surname, String userName, String email, String password, String passwordRepeat) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.passwordRepeat = passwordRepeat;
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isCheckTerms() {
        return checkTerms;
    }

    public void setCheckTerms(boolean checkTerms) {
        this.checkTerms = checkTerms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserSignUp that = (UserSignUp) o;

        if (checkTerms != that.checkTerms) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (surname != null ? !surname.equals(that.surname) : that.surname != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null)
            return false;
        return passwordRepeat != null ? passwordRepeat.equals(that.passwordRepeat) : that.passwordRepeat == null;

    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (checkTerms ? 1 : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (passwordRepeat != null ? passwordRepeat.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserSignUp{" +
                "userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", checkTerms=" + checkTerms +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", passwordRepeat='" + passwordRepeat + '\'' +
                '}';
    }
}
