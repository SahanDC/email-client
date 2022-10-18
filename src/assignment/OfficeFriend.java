package assignment;

public class OfficeFriend extends Official implements IGreet{
    private Date birthday = new Date();

    public OfficeFriend(String name, String email, String designation, Date birthday) {
        super(name, email, designation);
        this.birthday = birthday;
    }

    public Date getBirthday() {
        return birthday;
    }
}
